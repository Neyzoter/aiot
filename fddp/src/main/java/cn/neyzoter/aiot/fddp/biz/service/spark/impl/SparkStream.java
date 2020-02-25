package cn.neyzoter.aiot.fddp.biz.service.spark.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaConsumerGroup;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaTopic;
import cn.neyzoter.aiot.fddp.biz.service.kafka.impl.serialization.VehicleHttpPackDeserializer;
import cn.neyzoter.aiot.fddp.biz.service.spark.algo.DataPreProcess;
import cn.neyzoter.aiot.fddp.biz.service.spark.constant.SparkStreamingConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
@Component
public class SparkStream {
    public final static Logger logger= LoggerFactory.getLogger(SparkStream.class);
    public final static String VID_KEY_PREFIX= "vid=";
    public final static int SPARK_STEAMING_DURATION_SECOND = 20;
    SparkConf sparkConf;
    JavaStreamingContext jssc;
    Set<String> topicsSet;
    Map<String, Object> kafkaParams;
    public SparkStream() {
        try {
            // Configuration
            sparkConf = new SparkConf().setAppName(SparkStreamingConf.SPARK_STREAMING_NAME);
            topicsSet = new HashSet<>(Arrays.asList(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME));
            jssc = new JavaStreamingContext(sparkConf, Durations.seconds(SPARK_STEAMING_DURATION_SECOND));
            kafkaParams = new HashMap<>();
            kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerGroup.COMSUMER_BOOTSTRAP_SERVER);
            kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConsumerGroup.GROUP_SPARK_CONSUME_VEHICLE_HTTP_PACKET);
            kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VehicleHttpPackDeserializer.class);

            // Create direct kafka stream with brokers and topics
            JavaInputDStream<ConsumerRecord<String, VehicleHttpPack>> messages = KafkaUtils.createDirectStream(
                    jssc,
                    LocationStrategies.PreferConsistent(),
                    ConsumerStrategies.Subscribe(topicsSet, kafkaParams));
            // compact packs group by vid_year_month_and_day
            JavaPairDStream<String, VehicleHttpPack> record = messages.mapToPair(
                    x -> new Tuple2<>(String.format(VID_KEY_PREFIX + x.key().replace("\"","")),
                            x.value())).reduceByKey((x1, x2) -> DataPreProcess.compact(x1, x2));
            // missing values process
            record = record.mapValues(x -> DataPreProcess.missingValueProcess(x));
            // outlier values process
            record = record.mapValues(x -> DataPreProcess.outlierHandling(x));
            // multi Sampling Rate Process
            record = record.mapValues(x -> DataPreProcess.multiSamplingRateProcess(x));
            record.print();


            // Start the computation
            jssc.start();
            jssc.awaitTermination();
        } catch (Exception e) {
            logger.error("", e);
        }

    }

}
