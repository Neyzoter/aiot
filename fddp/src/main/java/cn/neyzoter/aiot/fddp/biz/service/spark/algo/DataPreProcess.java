package cn.neyzoter.aiot.fddp.biz.service.spark.algo;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import cn.neyzoter.aiot.fddp.biz.service.spark.exception.IllVehicleHttpPackTime;
import cn.neyzoter.aiot.fddp.biz.service.spark.exception.IllWinNum;
import org.springframework.beans.factory.annotation.Autowired;
import scala.Int;
import scala.util.Try;

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
     * properties
     */
    public static PropertiesUtil propertiesUtil = new PropertiesUtil(PropertiesLables.PROPERTIES_PATH);

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

    /**
     * 转化为矩阵
     * @param pack VehicleHttpPack
     * @return 特征转化为相关矩阵
     */
    public static Double[][][][] trans2Matrix (VehicleHttpPack pack) throws IllWinNum{
        // feature num
        int featureNum = Integer.parseInt(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_FEATURE_NUM));
        // win num
        int winNum = Integer.parseInt(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_WIN_NUM));
        // max step
        int maxStep = Integer.parseInt(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_STEP));
        // gap time between [X][feature num][feature num][i] and [X][feature num][feature num][i + 1]
        int gapTime = Integer.parseInt(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_GAP_NUM));
        Double[][][][] matrix = new Double[maxStep][featureNum][featureNum][winNum];

        // get data
        Double[][] data = pack.getVehicle().toArrayT();
        int dataNum = data[0].length;

        // get win
        String[] winStrs = propertiesUtil.getPropertiesList(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_WIN));
        if (winStrs.length != winNum) {
            throw new IllWinNum(winNum, winStrs.length);
        }

//        // start time
//        long start = pack.getVehicle().getRtDataMap().firstKey();
//        // end time
//        long end = pack.getVehicle().getRtDataMap().lastKey();
        for (int step = 0 ; step < maxStep; step ++) {
            for (int winIdx = 0; winIdx < winNum; winIdx ++) {
                int winInt = Integer.parseInt(winStrs[winIdx]);
                for (int i = 0; i < featureNum; i ++) {
                    for (int j = i; j < featureNum ; j ++) {
                        // must leave window
                        for (int t = 0; t <= dataNum - winInt; t += gapTime) {
//                            Double[] array1 = new Double[winInt];
//                            Double[] array2 = new Double[winInt];
//                            System.arraycopy(data[i], t, array1, 0, winInt);
//                            System.arraycopy(data[j], t, array2, 0, winInt);
                            for (int win = 0; win < winInt; win ++) {
                                matrix[step][i][j][winIdx] += data[i][t + win] * data[j][t + win];
                            }
                            matrix[step][i][j][winIdx] /= winInt;
                            matrix[step][j][i][winIdx] = matrix[step][i][j][winIdx];
                            // TODO
//                            matrix[step][i][j][winInt] = pack.getVehicle().getRtDataMap().get((Long) (start + t));
                        }

                    }
                }
                // TODO
            }
        }

        return matrix;
    }

}
