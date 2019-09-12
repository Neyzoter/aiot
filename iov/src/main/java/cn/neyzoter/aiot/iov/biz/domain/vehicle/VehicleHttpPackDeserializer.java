package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import cn.neyzoter.aiot.common.data.serialization.SerializationUtil;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * VehicleHttpPack Deserializer
 * @author Neyzoter Song
 * @date 2019/9/11
 */
public class VehicleHttpPackDeserializer implements Deserializer<VehicleHttpPack> {
    private final static Logger logger = LoggerFactory.getLogger(VehicleHttpPackSerializer.class);
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // do nothing

    }
    @Override
    public VehicleHttpPack deserialize(String topic, byte[] bytes) {
        try {
            return (VehicleHttpPack)SerializationUtil.deserialize(bytes);
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }
    @Override
    public void close() {
        // do nothing

    }
}
