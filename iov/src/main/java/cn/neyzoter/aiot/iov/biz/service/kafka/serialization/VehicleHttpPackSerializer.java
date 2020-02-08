package cn.neyzoter.aiot.iov.biz.service.kafka.serialization;

import cn.neyzoter.aiot.common.data.serialization.SerializationUtil;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * VehicleHttpPack Serializer
 * @deprecated
 * @author Neyzoter Song
 * @date 2019/9/11
 */
@Service
public class VehicleHttpPackSerializer implements Serializer<VehicleHttpPack> {
    private final static Logger logger = LoggerFactory.getLogger(VehicleHttpPackSerializer.class);
    @Override
    public void configure(Map<String, ?> config, boolean isKey){
        // do nothing
    }
    @Override
    public byte[] serialize(String topic, VehicleHttpPack vehicleHttpPack){
        try{
            return SerializationUtil.serialize(vehicleHttpPack);
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }
    @Override
    public void close() {
        //do nothing
    }
}
