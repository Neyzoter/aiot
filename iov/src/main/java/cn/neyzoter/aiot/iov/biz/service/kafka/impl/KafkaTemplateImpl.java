package cn.neyzoter.aiot.iov.biz.service.kafka.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka template implement
 * @author Neyzoter Song
 * @date 2019/9/6
 */
@Component
public class KafkaTemplateImpl {
    private final static Logger logger = LoggerFactory.getLogger(KafkaListenerImpl.class);
    private final KafkaTemplate kafkaTemplate;
    @Autowired
    public KafkaTemplateImpl(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

}
