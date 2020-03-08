package cn.neyzoter.aiot.fddp.biz.service.kafka.constant;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.kafka.impl.serialization.VehicleHttpPackDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
    @Autowired
    public KafkaListenerContainerFactory<?> vehicleHttpPackBatchFactory(KafkaProperties properties, PropertiesUtil propertiesUtil) {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = properties.buildConsumerProperties();
        // bootstrap server
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,propertiesUtil.readValue(PropertiesLables.KAFKA_BOOTSTRAP_SERVER));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VehicleHttpPackDeserializer.class);
        DefaultKafkaConsumerFactory defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory(props);
        factory.setConsumerFactory(defaultKafkaConsumerFactory);
        // batch listener can receive batch of msg
        factory.setBatchListener(true);
        return factory;
    }

}
