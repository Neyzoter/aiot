package cn.neyzoter.aiot.fddp.biz.service.biz.service.influxdb;

import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;

/**
 * vehicle http pack influx poster
 * @author Neyzoter Song
 * @date 2020-2-19
 */
public class VPackInfluxPoster {
    public static VehicleHttpPack postVpack2InfluxDB (VehicleHttpPack pack) {
        pack.getVehicle();

        return pack;
    }
}
