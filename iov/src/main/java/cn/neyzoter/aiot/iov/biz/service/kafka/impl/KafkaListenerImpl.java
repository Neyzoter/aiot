package cn.neyzoter.aiot.iov.biz.service.kafka.impl;

import cn.neyzoter.aiot.dal.dao.vehicle.Vehicle2InfluxDb;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.iov.biz.service.kafka.constant.KafkaConsumerGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import cn.neyzoter.aiot.iov.biz.service.kafka.constant.KafkaTopic;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka listener implement
 * @author Neyzoter Song
 * @date 2019/9/6
 */
@Component
public class KafkaListenerImpl {
    private final static Logger logger = LoggerFactory.getLogger(KafkaListenerImpl.class);
    private Vehicle2InfluxDb vehicle2InfluxDb = new Vehicle2InfluxDb("zju", "influxdb_bucket", "ms", "yzwAKztIXZLJNSvTPeUuFW7P9z4oWd_NLnGZNcIuoJMY7PCZEm1Lu1s-IIjloYFiSBVhRss7aMaDbh58WdlhGA==");

    @KafkaListener(topics = KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET, groupId = KafkaConsumerGroup.GROUP_CONSUME_VEHICLE_HTTP_PACKET)
    public void processRtData (String jdata) {

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
        tags.put("vehicleId",vehicleHttpPack.getVehicle().getId().toString());
        tags.put("application", vehicleHttpPack.getVehicle().getApp().toString());
        Map<String, String> fields = new HashMap<>(20);
        fields.put("ecuMaxTemp",vehicleHttpPack.getVehicle().getRtData().getEcuMaxTemp().toString());
        fields.put("speed", vehicleHttpPack.getVehicle().getRtData().getSpeed().toString());
        String timestamp = "";
        this.vehicle2InfluxDb.post2InfluxDb("vehicle", tags, fields, timestamp);

    }

}
