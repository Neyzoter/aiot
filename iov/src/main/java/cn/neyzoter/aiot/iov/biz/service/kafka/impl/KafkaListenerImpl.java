package cn.neyzoter.aiot.iov.biz.service.kafka.impl;

import cn.neyzoter.aiot.dal.pojo.Vehicle2InfluxDb;
import cn.neyzoter.aiot.iov.biz.domain.vehicle.VehicleHttpPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener implement
 * @author Neyzoter Song
 * @date 2019/9/6
 */
@Component
public class KafkaListenerImpl {
    private final static Logger logger = LoggerFactory.getLogger(KafkaListenerImpl.class);
    private Vehicle2InfluxDb vehicle2InfluxDb = new Vehicle2InfluxDb("zju", "influxdb_bucket", "s", "yzwAKztIXZLJNSvTPeUuFW7P9z4oWd_NLnGZNcIuoJMY7PCZEm1Lu1s-IIjloYFiSBVhRss7aMaDbh58WdlhGA==");

    /**
     * Listen for partition's msg
     * @param content {@link VehicleHttpPack}
     */
    @KafkaListener(topics = "VehicleHttpPack", groupId = "VehicleHttpPackageConsumer")
    public void processMsg(VehicleHttpPack content){

        logger.info("Processing Kafka msg0 : " + content.toString());
    }

}
