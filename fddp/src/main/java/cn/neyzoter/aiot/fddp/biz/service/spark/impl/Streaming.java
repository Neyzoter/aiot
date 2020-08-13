package cn.neyzoter.aiot.fddp.biz.service.spark.impl;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.dal.domain.feature.DataMatrix;
import cn.neyzoter.aiot.dal.domain.feature.InputCorrMatrix;
import cn.neyzoter.aiot.dal.domain.feature.OutputCorrMatrix;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesValueRange;
import cn.neyzoter.aiot.fddp.biz.service.hadoop.hdfs.HdfsUtil;
import cn.neyzoter.aiot.fddp.biz.service.influxdb.VPackInfluxPoster;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaConsumerGroup;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaTopic;
import cn.neyzoter.aiot.fddp.biz.service.kafka.impl.serialization.VehicleHttpPackDeserializer;
import cn.neyzoter.aiot.fddp.biz.service.spark.algo.DataPreProcess;
import cn.neyzoter.aiot.fddp.biz.service.spark.constant.SparkStreamingConf;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.RtDataBound;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.RtDataBoundMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import scala.Serializable;
import scala.Tuple2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


/**
 * Spark Stream processor
 * @author Neyzoter Song
 * @date 2020-2-17
 */
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.influxdb,cn.neyzoter.aiot.fddp.biz.service.properties")
@Component
public class Streaming implements Serializable {
    private static final long serialVersionUID = 8231463007986745839L;
    public final static Logger logger= LoggerFactory.getLogger(Streaming.class);
    public final static String VID_KEY_PREFIX= "vid=";
    public final static int SPARK_STEAMING_DURATION_SECOND = 5;

    private JavaStreamingContext jssc;
    private Set<String> topicsSet;
    private Map<String, Object> kafkaParams;
    private RtDataBoundMap rtDataBoundMap;
    @Autowired
    public Streaming(PropertiesUtil propertiesUtil, VPackInfluxPoster vPackInfluxPoster, RtDataBoundMap rtDataBoundMap) {
        this.rtDataBoundMap = rtDataBoundMap;
        try {
            conf(propertiesUtil);

            testPrepare(rtDataBoundMap);

//            JavaPairDStream<String, InputCorrMatrix> input = prepare(vPackInfluxPoster, rtDataBoundMap);
//
//            JavaPairDStream<String, OutputCorrMatrix> loss = compute(input);
//            loss.print();
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
        // 设置和集群核心数相同
        sparkConf.set("spark.default.parallelism", "8");
        // use kryo
//        sparkConf.set("spark.kryo.registrationRequired", "true");
//        Class[] classArray = {VehicleHttpPack.class,DataPreProcess.class,VehicleHttpPack.class, RestTemp.class,VPackInfluxPoster.class,
//                RuntimeData.class, Vehicle2InfluxDb.class, SerializationUtil.class, Vehicle.class, ModelManager.class, Streaming.class,
//                Long[].class, RtDataBoundMap.class}; // , SavedModelBundle.class, Object.class, Session.class, Graph.class
//        sparkConf.registerKryoClasses(classArray);
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
    private JavaPairDStream<String, InputCorrMatrix> prepare (VPackInfluxPoster vPackInfluxPoster, RtDataBoundMap rtDataBoundMap) {
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

        // outlier values process
        record = record.mapValues(DataPreProcess::outlierHandling);
        // multi Sampling Rate Process
        record = record.mapValues(DataPreProcess::multiSamplingRateProcess);
        // normalize
        record = record.mapValues(x -> DataPreProcess.normalize(x,
                ((RtDataBound)rtDataBoundMap.get(x.getVehicle().getVtype())).getMinRtData(),
                ((RtDataBound)rtDataBoundMap.get(x.getVehicle().getVtype())).getMaxRtData(),
                ((RtDataBound)rtDataBoundMap.get(x.getVehicle().getVtype())).getNormalizedE()));
        JavaPairDStream<String, DataMatrix> dataMatrix = record.mapValues(DataPreProcess::toDataMatrix);
        JavaPairDStream<String, InputCorrMatrix> corrMatrix = dataMatrix.mapValues(DataPreProcess::toInputCorrMatrix);

        return corrMatrix;
    }

    public static void hdfsStream(JavaStreamingContext jssc) {
        String hdfsFile = "hdfs://hdfs:8020/input/";//checkpoint存放数据的文件夹
        JavaDStream<String> jds = jssc.textFileStream(hdfsFile);
        jds.print();
//        jds.foreachRDD(System.out::println);
    }

    public static void hdfsRead() {
        String hdfsFile = "hdfs://hdfs:8020/input/hhh.txt";
        HdfsUtil hdfsUtil = new HdfsUtil();
        hdfsUtil.readHDFSFile(hdfsFile);
    }
    public JavaPairDStream<String, VehicleHttpPack> testPrepare(RtDataBoundMap rtDataBoundMap) {
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

        // outlier values process
        record = record.mapValues(DataPreProcess::outlierHandling);

        // 测试hdfs
//        hdfsStream(jssc);
        hdfsRead();
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://hdfs:8020");
        conf.set("fs.defaultFS", "hdfs://hdfs:8020");
        conf.set("mapreduce.jobtracker.address", "hdfs://hdfs");
        conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
        conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
        String remoteFilePath = "/input/hhh.txt"; // HDFS路径
        try {
            HdfsUtil.cat(conf, remoteFilePath);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return record;
    }

    /**
     * compute loss
     * @param input input
     * @return
     */
    private JavaPairDStream<String, OutputCorrMatrix> compute (JavaPairDStream<String, InputCorrMatrix> input) {
        JavaPairDStream<String, OutputCorrMatrix> corrMatrixLoss = input.mapValues(x -> DataPreProcess.toCorrMatrixLoss(x));
        return corrMatrixLoss;
    }

    /**
     * 将输入的相关矩阵发送到tensorflow serving
     * @param input 输入相关矩阵
     */
    public void tfserving(JavaPairDStream<String, InputCorrMatrix> input) {
        input.foreachRDD(rdd -> rdd.foreachPartition(partitionRecords -> {
            partitionRecords.forEachRemaining(System.out::println);
        }));
//        input.foreachRDD(rdd -> rdd.foreachPartition(partitionRecords -> {
//            // 建立连接
//            String urlNameString = "tf-serving:9091";
//            URL realUrl = new URL(urlNameString);
//            // 打开和URL之间的连接
//            URLConnection connection = realUrl.openConnection();
//            // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 发送POST请求必须设置如下两行
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            PrintWriter out = new PrintWriter(connection.getOutputStream());
//            // flush输出流的缓冲
//            out.flush();
//            // 建立实际的连接
//            connection.connect();
//            // 发送数据
//            partitionRecords.forEachRemaining(record -> connection);
//        }));
    }

    /**
     * flush to influxDB
     * @deprecated 通过Kafka生产者将数据写入到influxDB中
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
