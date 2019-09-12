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
    @KafkaListener(topics = "VehicleHttpPack")
    public void processMsg(VehicleHttpPack content){
        logger.info("Processing Kafka msg : " + content.toString());
    }
}
