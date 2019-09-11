package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * VehicleHttpPack Deserializer
 * @author Neyzoter Song
 * @date 2019/9/11
 */
public class VehicleHttpPackDeserializer implements Deserializer<VehicleHttpPack> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // do nothing

    }
    @Override
    public VehicleHttpPack deserialize(String topic, byte[] bytes) {
        ByteArrayInputStream bais = null;
        VehicleHttpPack tmpObject = null;
        try {
            // Deserialize
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            tmpObject = (VehicleHttpPack)ois.readObject();
            return tmpObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void close() {
        // do nothing

    }
}
