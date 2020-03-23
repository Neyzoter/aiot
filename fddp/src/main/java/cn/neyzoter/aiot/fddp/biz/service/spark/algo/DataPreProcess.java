package cn.neyzoter.aiot.fddp.biz.service.spark.algo;

import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.Vehicle;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.spark.exception.IllVehicleHttpPackTime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Data Pre Process Class
 * @author Neyzoter Song
 * @date 2020-2-19
 */
public class DataPreProcess implements Serializable {

    private static final long serialVersionUID = 2357018289949033614L;

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
        Class clazz = VehicleHttpPack.class;
        Field[] fs = clazz.getDeclaredFields();
        SortedMap rtDataMap = pack.getVehicle().getRtDataMap();
        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<Long, RuntimeData>> iter = rtDataMap.entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<Long, RuntimeData> entry = iter.next();
            RuntimeData rtData = entry.getValue();
            for (Field field : fs) {
                field.setAccessible(true);
                try {
                    // this field is null, we need to fill it
                    if (field.get(rtData) == null) {
                        // TODO
                        // just set as zero
                        field.set(rtData, 0.0);
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
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

    /**
     * normalize the pack
     * @param pack {@link VehicleHttpPack}
     * @param minRtD {@link RuntimeData} min RuntimeData
     * @param deltaRtD {@link RuntimeData} max - min + e RuntimeData
     * @return {@link VehicleHttpPack}
     */
    public static VehicleHttpPack normalize(VehicleHttpPack pack, RuntimeData minRtD, RuntimeData deltaRtD) throws Exception{
        Iterator<Map.Entry<Long, RuntimeData>> iter = pack.getVehicle().getRtDataMap().entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<Long, RuntimeData> item = iter.next();
            RuntimeData rtd = item.getValue();
            try {
                rtd.normalize(minRtD, deltaRtD);
            } catch (Exception e) {
                throw e;
            }
        }
        return pack;
    }
}
