package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.Vehicle;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.iov.IovApplication;
import cn.neyzoter.aiot.iov.biz.service.kafka.serialization.VehicleHttpPackDeserializer;
import cn.neyzoter.aiot.iov.biz.service.kafka.serialization.VehicleHttpPackSerializer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;


/**
 * test VehicleHttpPackSerializer and VehicleHttpPackDeserializer
 * @author Neyzoter Song
 * @date 2019/9/11
 */
public class TestVehicleHttpPackSerializer {
    private final static Logger logger = LoggerFactory.getLogger(IovApplication.class);

    /**
     * test for serialize and deserialize
     * if original toString euqauls to vehicleHttpPackDeserialized.toString ,then correct
     */
    @Test
    public void testSerialize(){
        VehicleHttpPack vehicleHttpPack = new VehicleHttpPack();
        Vehicle vehicle = new Vehicle();
        RuntimeData runtimeData = new RuntimeData();
        runtimeData.setEcuMaxTemp(40);runtimeData.setSpeed(100);
        SortedMap<Integer, RuntimeData> rtDataMap = new TreeMap<>();
        rtDataMap.put(0, runtimeData);
        vehicle.setId(new Long(100));vehicle.setRtDataMap(rtDataMap);vehicle.setApp((long)10);
        vehicleHttpPack.setDay("1");vehicleHttpPack.setMonth("2");vehicleHttpPack.setSign("eakdiex");
        vehicleHttpPack.setYear("2019");vehicleHttpPack.setVehicle(vehicle);

        VehicleHttpPackSerializer vehicleHttpPackSerializer = new VehicleHttpPackSerializer();
        byte[] vehicleHttpPackByte = vehicleHttpPackSerializer.serialize("vehicleHttpPack", vehicleHttpPack);

        VehicleHttpPackDeserializer vehicleHttpPackDeserializer = new VehicleHttpPackDeserializer();
        VehicleHttpPack vehicleHttpPackDeserialized = vehicleHttpPackDeserializer.deserialize("vehicleHttpPack", vehicleHttpPackByte);

        // if original toString euqauls to vehicleHttpPackDeserialized.toString
        assertEquals(vehicleHttpPackDeserialized.toString(),vehicleHttpPack.toString());
        logger.info("vehicleHttpPack.toString()             : " + vehicleHttpPack.toString());
        logger.info("vehicleHttpPackDeserialized.toString() : " + vehicleHttpPackDeserialized.toString());
    }

}
