package cn.neyzoter.aiot.iov.biz.service.kafka.impl;

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

    /**
     * Listen for partition's msg
     * @param content {@link VehicleHttpPack}
     */
    @KafkaListener(topics = "VehicleHttpPack", groupId = "VehicleHttpPackageConsumer")
    public void processMsg0(VehicleHttpPack content){
        logger.info("Processing Kafka msg0 : " + content.toString());
    }
    /**
     * Listen for partition's msg
     * @param content {@link VehicleHttpPack}
     */
    @KafkaListener(topics = "VehicleHttpPack", groupId = "VehicleHttpPackageConsumer")
    public void processMsg1(VehicleHttpPack content){
        logger.info("Processing Kafka msg2 : " + content.toString());
    }
}
