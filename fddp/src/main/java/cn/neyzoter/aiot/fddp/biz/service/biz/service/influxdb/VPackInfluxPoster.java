package cn.neyzoter.aiot.fddp.biz.service.biz.service.influxdb;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.dal.dao.vehicle.Vehicle2InfluxDb;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * vehicle http pack influx poster
 * @author Neyzoter Song
 * @date 2020-2-19
 */
@Component
public class VPackInfluxPoster {
    public static final Logger logger = LoggerFactory.getLogger(VPackInfluxPoster.class);

    private Vehicle2InfluxDb vehicle2InfluxDb;

    public VPackInfluxPoster () {
        PropertiesUtil propertiesUtil = new PropertiesUtil(PropertiesLables.PROPERTIES_PATH);
        String host = propertiesUtil.readValue(PropertiesLables.INFLUXDB_HOST);
        String port = propertiesUtil.readValue(PropertiesLables.INFLUXDB_PORT);
        String org = propertiesUtil.readValue(PropertiesLables.INFLUXDB_ORG);
        String bucket = propertiesUtil.readValue(PropertiesLables.INFLUXDB_BUCKET);
        String precision = propertiesUtil.readValue(PropertiesLables.INFLUXDB_PRECISION);
        String token = propertiesUtil.readValue(PropertiesLables.INFLUXDB_TOKEN);
        vehicle2InfluxDb = new Vehicle2InfluxDb(org, bucket, precision, token,host,port);
        logger.debug(String.format(vehicle2InfluxDb.getUrl()));
    }

    /**
     * get vehicle2InfluxDb
     * @return Vehicle2InfluxDb
     */
    public Vehicle2InfluxDb getVehicle2InfluxDb() {
        return vehicle2InfluxDb;
    }

    /**
     * set vehicle2InfluxDb
     * @param vehicle2InfluxDb vehicle2InfluxDb
     */
    public void setVehicle2InfluxDb(Vehicle2InfluxDb vehicle2InfluxDb) {
        this.vehicle2InfluxDb = vehicle2InfluxDb;
    }

    /**
     * flush to influxdb
     * @param pack pack
     * @return VehicleHttpPack
     */
    public VehicleHttpPack postVpack2InfluxDB (VehicleHttpPack pack) {
        String[] lines = pack.toInfluxLinesProto();
        vehicle2InfluxDb.postMultilines2InfluxDB(lines);
        return pack;
    }
}
