package cn.neyzoter.aiot.fddp.biz.service.spark.algo;

import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.spark.exception.IllVehicleHttpPackTime;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Data Pre Process Class
 * @author Neyzoter Song
 * @date 2020-2-19
 */
public class DataPreProcess {

    /**
     * compact two pack
     * @apiNote requir Year Month Day is the same
     * @param pack1 {@link VehicleHttpPack}
     * @param pack2 {@link VehicleHttpPack}
     * @return {@link VehicleHttpPack}
     */
    public static VehicleHttpPack compact (VehicleHttpPack pack1, VehicleHttpPack pack2) throws IllVehicleHttpPackTime {
        if (!pack1.getDay().equals(pack2.getDay())) {
            throw new IllVehicleHttpPackTime(IllVehicleHttpPackTime.ILL_DAY);
        }
        if (!pack1.getMonth().equals(pack2.getMonth())) {
            throw new IllVehicleHttpPackTime(IllVehicleHttpPackTime.ILL_MONTH);
        }
        if (!pack1.getYear().equals(pack2.getYear())) {
            throw new IllVehicleHttpPackTime(IllVehicleHttpPackTime.ILL_YEAR);
        }
        SortedMap<Long, RuntimeData> pack1Map = pack1.getVehicle().getRtDataMap();
        SortedMap<Long, RuntimeData> pack2Map = pack2.getVehicle().getRtDataMap();
        Set<Map.Entry<Long, RuntimeData>> pack2MapSet= pack2Map.entrySet();
        for (Map.Entry<Long, RuntimeData> map : pack2MapSet) {
            pack1Map.put(map.getKey(),map.getValue());
        }
        return pack1;
    }

    /**
     * outlier handling
     * @param pack {@link VehicleHttpPack}
     * @return {@link VehicleHttpPack}
     */
    public static VehicleHttpPack outlierHandling (VehicleHttpPack pack) {
        // TODO
        SortedMap rtDataMap = pack.getVehicle().getRtDataMap();

        return pack;
    }

    /**
     * missing value process
     * @param pack {@link VehicleHttpPack}
     * @return {@link VehicleHttpPack}
     */
    public static VehicleHttpPack missingValueProcess (VehicleHttpPack pack) {
        // TODO
        return pack;
    }

    /**
     * multi sampling rate process
     * @param pack VehicleHttpPack
     * @return {@link VehicleHttpPack}
     */
    public static VehicleHttpPack multiSamplingRateProcess (VehicleHttpPack pack) {
        // TODO
        return pack;
    }
}
