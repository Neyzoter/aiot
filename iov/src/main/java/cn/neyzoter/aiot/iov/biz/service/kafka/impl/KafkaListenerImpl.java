package cn.neyzoter.aiot.iov.biz.service.kafka.impl;

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
    @KafkaListener(topics = "VehicleHttpPackage")
    public void processMsg(String content){
        logger.info("Processing Kafka msg : " + content);
    }
}
