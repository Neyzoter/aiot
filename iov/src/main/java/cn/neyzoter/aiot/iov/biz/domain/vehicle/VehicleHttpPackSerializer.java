package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * VehicleHttpPack Serializer
 * @author Neyzoter Song
 * @date 2019/9/11
 */
public class VehicleHttpPackSerializer implements Serializer<VehicleHttpPack> {
    private final static Logger logger = LoggerFactory.getLogger(VehicleHttpPackSerializer.class);
    @Override
    public void configure(Map<String, ?> config, boolean isKey){
        // do nothing
    }
    @Override
    public byte[] serialize(String topic, VehicleHttpPack vehicleHttpPack){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(vehicleHttpPack);
            byte[] bytes = baos.toByteArray();
            oos.close();
            return bytes;
        }catch (Exception e){
            logger.error("",e);
        }finally {

        }
        return null;
    }
    @Override
    public void close() {
        //do nothing
    }
}
