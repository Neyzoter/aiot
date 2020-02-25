package cn.neyzoter.aiot.dal.dao.vehicle;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Vehicle2InfluxDb
 * @author Neyzoter Song
 * @date 2019/9/15
 */
public class TestVehicle2InfluxDb {
    private final static Logger logger = LoggerFactory.getLogger(TestVehicle2InfluxDb.class);
    Vehicle2InfluxDb vehicle2InfluxDb = new Vehicle2InfluxDb("zju", "influxdb_bucket", "s", "yzwAKztIXZLJNSvTPeUuFW7P9z4oWd_NLnGZNcIuoJMY7PCZEm1Lu1s-IIjloYFiSBVhRss7aMaDbh58WdlhGA==","localhost");
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
}
