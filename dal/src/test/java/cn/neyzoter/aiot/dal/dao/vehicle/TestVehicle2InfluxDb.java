package cn.neyzoter.aiot.dal.dao.vehicle;

import org.junit.Test;

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
    @Test
    public void testPost2InfluxDb(){
        String measurement = "vehicle";
        Map<String, String> tags = new HashMap<>();tags.put("vehicleId", "100");
        Map<String, Float> fields = new HashMap<>();fields.put("speed",new Float(43.21));
        String timestamp = "";
        Vehicle2InfluxDb vehicle2InfluxDb = new Vehicle2InfluxDb("zju", "influxdb_bucket", "s", "yzwAKztIXZLJNSvTPeUuFW7P9z4oWd_NLnGZNcIuoJMY7PCZEm1Lu1s-IIjloYFiSBVhRss7aMaDbh58WdlhGA==");
        vehicle2InfluxDb.post2InfluxDb(measurement, tags,fields,timestamp);
    }
}
