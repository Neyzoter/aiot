package cn.neyzoter.aiot.fddp.biz.service.spark.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import cn.neyzoter.aiot.common.data.serialization.SerializationUtil;
import cn.neyzoter.aiot.common.tensorflow.ModelManager;
import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.dal.dao.vehicle.Vehicle2InfluxDb;
import cn.neyzoter.aiot.dal.domain.feature.DataMatrix;
import cn.neyzoter.aiot.dal.domain.feature.InputCorrMatrix;
import cn.neyzoter.aiot.dal.domain.feature.OutputCorrMatrix;
import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.dal.util.RestTemp;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesValueRange;
import cn.neyzoter.aiot.fddp.biz.service.influxdb.VPackInfluxPoster;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaConsumerGroup;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaTopic;
import cn.neyzoter.aiot.fddp.biz.service.kafka.impl.serialization.VehicleHttpPackDeserializer;
import cn.neyzoter.aiot.fddp.biz.service.spark.algo.DataPreProcess;
import cn.neyzoter.aiot.fddp.biz.service.spark.constant.SparkStreamingConf;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.TfModelManager;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.VehicleModelTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import scala.Serializable;
import scala.Tuple2;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.Durations;


/**
 * Spark Stream processor
 * @author Neyzoter Song
 * @date 2020-2-17
 */
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.influxdb,cn.neyzoter.aiot.fddp.biz.service.properties")
@Component
public class SparkStream implements Serializable {
    private static final long serialVersionUID = 8231463007986745839L;
    public final static Logger logger= LoggerFactory.getLogger(SparkStream.class);
    public final static String VID_KEY_PREFIX= "vid=";
    public final static int SPARK_STEAMING_DURATION_SECOND = 5;

    private JavaStreamingContext jssc;
    private Set<String> topicsSet;
    private Map<String, Object> kafkaParams;
    private VehicleModelTable vehicleModelTable;
    @Autowired
    public SparkStream(PropertiesUtil propertiesUtil, VPackInfluxPoster vPackInfluxPoster, VehicleModelTable modelTable) {
        this.vehicleModelTable = modelTable;
        try {
            conf(propertiesUtil);
            JavaPairDStream<String, InputCorrMatrix> input = prepare(vPackInfluxPoster, vehicleModelTable);
            JavaPairDStream<String, OutputCorrMatrix> loss = compute(input, vehicleModelTable);
            loss.print();
            start();
        } catch (Exception e) {
            logger.error("", e);
        }

    }

    /**
     * config spark stream
     * @param propertiesUtil properties util
     */
    private void conf (PropertiesUtil propertiesUtil) {
        // App name
        SparkConf sparkConf = new SparkConf().setAppName(SparkStreamingConf.SPARK_STREAMING_NAME);
        // serialize
        // since spark 2.0.0, Kyro is internally used for simple class,
        // we should rigister our own classes to improve serializable efficiency
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConf.set("spark.kryo.registrationRequired", "true");
        Class[] classArray = {VehicleHttpPack.class,DataPreProcess.class,VehicleHttpPack.class, RestTemp.class,VPackInfluxPoster.class,
                RuntimeData.class, Vehicle2InfluxDb.class, SerializationUtil.class, TfModelManager.class, ModelManager.class,SparkStream.class,
                Long[].class};
        sparkConf.registerKryoClasses(classArray);
        // spark master
        String sparkMaster = propertiesUtil.readValue(PropertiesLables.SPARK_MASTER);
        if (!PropertiesValueRange.APP_TEST_ON_IDEA.equals(sparkMaster)) {
            sparkConf.setMaster(sparkMaster);
        }
        topicsSet = new HashSet<>(Arrays.asList(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME));
        jssc = new JavaStreamingContext(sparkConf, Durations.seconds(SPARK_STEAMING_DURATION_SECOND));
        kafkaParams = new HashMap<>(10);
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesUtil.readValue(PropertiesLables.KAFKA_BOOTSTRAP_SERVER));
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConsumerGroup.GROUP_SPARK_CONSUME_VEHICLE_HTTP_PACKET);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VehicleHttpPackDeserializer.class);
    }

    /**
     * prepare
     * @return JavaPairDStream
     */
    private JavaPairDStream<String, InputCorrMatrix> prepare (VPackInfluxPoster vPackInfluxPoster, VehicleModelTable vehicleModelTable) {
        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, VehicleHttpPack>> messages = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams));
        // compact packs group by vid_year_month_and_day
        JavaPairDStream<String, VehicleHttpPack> record = messages.mapToPair(
                x -> new Tuple2<>(VID_KEY_PREFIX + x.key().replace("\"",""),
                        x.value())).reduceByKey(DataPreProcess::compact);
        //count
        JavaDStream<Long> count = messages.count();
        count.print();

//            // post to influxdb
//            flush2Influx(record, vPackInfluxPoster);

        // outlier values process
        record = record.mapValues(DataPreProcess::outlierHandling);
        // multi Sampling Rate Process
        record = record.mapValues(DataPreProcess::multiSamplingRateProcess);
        // normalize
        record = record.mapValues(x -> DataPreProcess.normalize(x,
                vehicleModelTable.getModelManager(x.getVehicle().getVtype()).getMinRtData(),
                vehicleModelTable.getModelManager(x.getVehicle().getVtype()).getMaxRtData()));
        JavaPairDStream<String, DataMatrix> dataMatrix = record.mapValues(DataPreProcess::toDataMatrix);
        JavaPairDStream<String, InputCorrMatrix> corrMatrix = dataMatrix.mapValues(DataPreProcess::toInputCorrMatrix);

        return corrMatrix;
    }

    /**
     * compute loss
     * @param input input
     * @param vehicleModelTable vehicleModelTable
     * @return
     */
    private JavaPairDStream<String, OutputCorrMatrix> compute (JavaPairDStream<String, InputCorrMatrix> input, VehicleModelTable vehicleModelTable) {
        JavaPairDStream<String, OutputCorrMatrix> corrMatrixLoss = input.mapValues(x -> DataPreProcess.toCorrMatrixLoss(x, vehicleModelTable));
        return corrMatrixLoss;
    }

    /**
     * flush to influxDB
     * @param record record
     */
    private void flush2Influx (JavaPairDStream<String, VehicleHttpPack> record, VPackInfluxPoster vPackInfluxPoster) {
        record.mapValues(vPackInfluxPoster::postVpack2InfluxDB);
    }

    /**
     * start the computation
     */
    private void start () throws Exception{
        try {
            // Start the computation
            jssc.start();
            jssc.awaitTermination();
        } catch (Exception e) {
            throw e;
        }

    }

}
