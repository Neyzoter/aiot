package cn.neyzoter.aiot.dal.dao.vehicle;

import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.Vehicle;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Vehicle2InfluxDb
 * @author Neyzoter Song
 * @date 2019/9/15
 */
public class TestVehicle2InfluxDb {
    private final static Logger logger = LoggerFactory.getLogger(TestVehicle2InfluxDb.class);
    Vehicle2InfluxDb vehicle2InfluxDb = new Vehicle2InfluxDb("zju", "influxdb_bucket", "ms", "yzwAKztIXZLJNSvTPeUuFW7P9z4oWd_NLnGZNcIuoJMY7PCZEm1Lu1s-IIjloYFiSBVhRss7aMaDbh58WdlhGA==","localhost");
    @Test
    public void testPostOnepoint2InfluxDb(){
        String measurement = "vehicle";
        Map<String, String> tags = new HashMap<>();tags.put("vehicleId", "50");
        Map<String, String> fields = new HashMap<>();fields.put("speed",new Float(43.21).toString());
        String timestamp = "";

        // test postOnepoints2InfluxDb
        vehicle2InfluxDb.postOnepoint2InfluxDb(measurement, tags,fields,timestamp);

    }
    @Test
    public void testPostMultipoints2InfluxDb () {
        String[] measurements = {"vehicle1","vehicle2","vehicle3"};
        Map<String, String>[] tagses = new Map[3];
        tagses[0] = new HashMap<>();tagses[1] = new HashMap<>();tagses[2] = new HashMap<>();
        tagses[0].put("id","10");tagses[0].put("app", "app1");
        tagses[1].put("id","11");tagses[1].put("app", "app2");
        tagses[2].put("id","12");tagses[2].put("app", "app1");

        Map<String, String>[] fieldses = new Map[3];
        fieldses[0] = new HashMap<>();fieldses[1] = new HashMap<>();fieldses[2] = new HashMap<>();
        fieldses[0].put("speed","40");tagses[0].put("temperature", "35");
        fieldses[1].put("speed","50");tagses[1].put("temperature", "40");
        fieldses[2].put("speed","60");tagses[2].put("temperature", "45");

        String[] timestamps = {"", "", ""};

        vehicle2InfluxDb.postMultipoints2InfluxDB(measurements, tagses,fieldses, timestamps);
    }

//    @Test
//    public void testPostMultilines2InfluxDB () {
//        List<VehicleHttpPack> vl = new ArrayList<>();
//        VehicleHttpPack vtp1 = new VehicleHttpPack();vtp1.setDay("2");vtp1.setMonth("2");vtp1.setSign("100");vtp1.setYear("2020");
//        Vehicle v1 = new Vehicle();v1.setApp((long)1);v1.setVid("12");v1.setVtype("mazida100");
//        RuntimeData data1 = new RuntimeData();data1.setSpeed(100);data1.setEcuMaxTemp(50);
//        SortedMap<Long, RuntimeData> map1 = new TreeMap<>();map1.put(System.currentTimeMillis(), data1);
//        v1.setRtDataMap(map1);vtp1.setVehicle(v1);
//
//        VehicleHttpPack vtp2 = new VehicleHttpPack();vtp2.setDay("2");vtp2.setMonth("2");vtp2.setSign("100");vtp2.setYear("2020");
//        Vehicle v2 = new Vehicle();v2.setApp((long)1);v2.setVid("12");v2.setVtype("mazida100");
//        RuntimeData data2 = new RuntimeData();data2.setSpeed(70);data2.setEcuMaxTemp(40);
//        SortedMap<Long, RuntimeData> map2 = new TreeMap<>();map2.put(System.currentTimeMillis(), data2);
//        v2.setRtDataMap(map2);vtp2.setVehicle(v2);
//
//        vl.add(vtp1);vl.add(vtp2);
//
//        List<String> list = VehicleHttpPack.vhplToInfluxLinesProto(vl);
//        vehicle2InfluxDb.postMultilines2InfluxDB(list);
//    }
}
