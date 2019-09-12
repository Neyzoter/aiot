package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import cn.neyzoter.aiot.iov.IovApplication;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestVehicleHttpPackSerializer {
    private final static Logger logger = LoggerFactory.getLogger(IovApplication.class);
    @Test
    public void testSerialize(){
        VehicleHttpPack vehicleHttpPack = new VehicleHttpPack();
        Vehicle vehicle = new Vehicle();RuntimeData runtimeData = new RuntimeData();
        runtimeData.setEcuMaxTemp(40);runtimeData.setSpeed(100);
        vehicle.setId(100);vehicle.setRtData(runtimeData);
        vehicleHttpPack.setDay("1");vehicleHttpPack.setMonth("2");vehicleHttpPack.setSign("eakdiex");
        vehicleHttpPack.setSecond("1234");vehicleHttpPack.setYear("2019");vehicleHttpPack.setVehicle(vehicle);

        VehicleHttpPackSerializer vehicleHttpPackSerializer = new VehicleHttpPackSerializer();
        byte[] vehicleHttpPackByte = vehicleHttpPackSerializer.serialize("vehicleHttpPack", vehicleHttpPack);

        VehicleHttpPackDeserializer vehicleHttpPackDeserializer = new VehicleHttpPackDeserializer();
        VehicleHttpPack vehicleHttpPackDeserialized = vehicleHttpPackDeserializer.deserialize("vehicleHttpPack", vehicleHttpPackByte);

//        logger.info(String.format("Application ID : %d\r\n",vehicleHttpPackDeserialized.getVehicle().getApplication()));
        logger.info(vehicleHttpPackDeserialized.toString());
    }

}
