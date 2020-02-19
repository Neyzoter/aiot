package cn.neyzoter.aiot.fddp.biz.service.kafka.impl;

import cn.neyzoter.aiot.dal.dao.vehicle.Vehicle2InfluxDb;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaConsumerGroup;
import cn.neyzoter.aiot.fddp.biz.service.kafka.constant.KafkaTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kafka listener implement
 * @author Neyzoter Song
 * @date 2019/9/6
 */
//@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.kafka.constant")
//@Component
public class KafkaListenerImpl {
    private final static Logger logger = LoggerFactory.getLogger(KafkaListenerImpl.class);
    private Vehicle2InfluxDb vehicle2InfluxDb = new Vehicle2InfluxDb("zju", "influxdb_bucket", "ms", "yzwAKztIXZLJNSvTPeUuFW7P9z4oWd_NLnGZNcIuoJMY7PCZEm1Lu1s-IIjloYFiSBVhRss7aMaDbh58WdlhGA==");

    /**
     * process real time data
     * @param vehicleHttpPack
     * @param key
     * @param partition
     * @param topic
     * @param ts
     */
    @KafkaListener(id = "listener1",topics = KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME, groupId = KafkaConsumerGroup.GROUP_CONSUME_VEHICLE_HTTP_PACKET,containerFactory = "vehicleHttpPackBatchFactory")
    public void processRtData (@Payload List<VehicleHttpPack> vehicleHttpPack,
                               @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {

        logger.info(String.format("\n[listener 1]\nvehicleHttpPack : %s\nkey : %s\npartition : %d\ntopic : %s\ntime stamp : %d", vehicleHttpPack.toString(), key ,partition, topic, ts));
    }
    @KafkaListener(id = "listener2", topics = KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME, groupId = KafkaConsumerGroup.GROUP_CONSUME_VEHICLE_HTTP_PACKET,containerFactory = "vehicleHttpPackBatchFactory")
    public void processData (@Payload List<VehicleHttpPack> vehicleHttpPack,
                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
        logger.info(String.format("\n[listener 2]\nprocessData : vehicleHttpPack : %s\nkey : %s\npartition : %d\ntopic : %s\ntime stamp : %d", vehicleHttpPack.toString(), key ,partition, topic, ts));
    }
    /**
     * Listen for partition's msg
     * @deprecated
     * @param vehicleHttpPack {@link VehicleHttpPack}
     */
//    @KafkaListener(topics = KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET, groupId = KafkaConsumerGroup.GROUP_CONSUME_VEHICLE_HTTP_PACKET)
    public void processMsg(VehicleHttpPack vehicleHttpPack){
        logger.info("Processing Kafka msg : " + vehicleHttpPack.toString());
        Map<String, String> tags = new HashMap<>(20);
        tags.put("vid",vehicleHttpPack.getVehicle().getVid());
        tags.put("application", vehicleHttpPack.getVehicle().getApp().toString());
        Map<String, String> fields = new HashMap<>(20);
        fields.put("ecuMaxTemp",vehicleHttpPack.getVehicle().getRtDataMap().toString());
        fields.put("speed", vehicleHttpPack.getVehicle().getRtDataMap().toString());
        String timestamp = "";
        this.vehicle2InfluxDb.postOnePoint2InfluxDb("vehicle", tags, fields, timestamp);

    }

}
