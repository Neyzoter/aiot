package cn.neyzoter.aiot.fddp.biz.service.kafka.constant;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

/**
 * kafka container
 * @author Neyzoter Song
 * @date 2020-2-9
 */
@Configuration
public class KafkaContainer {
    @Bean
    public KafkaListenerContainerFactory<?> vehicleHttpPackBatchFactory(KafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = properties.buildConsumerProperties();
        DefaultKafkaConsumerFactory defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory(props);
        factory.setConsumerFactory(defaultKafkaConsumerFactory);
        // batch listener can receive batch of msg
        factory.setBatchListener(true);
        return factory;
    }

}
