package cn.neyzoter.aiot.fddp.biz.service.spark.algo;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.dal.domain.feature.DataMatrix;
import cn.neyzoter.aiot.dal.domain.feature.InputCorrMatrix;
import cn.neyzoter.aiot.dal.domain.feature.OutputCorrMatrix;
import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.spark.exception.IllVehicleHttpPackTime;
import cn.neyzoter.aiot.fddp.biz.service.spark.exception.IllWinNum;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.RtDataBoundMap;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.TfModelManager;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.VehicleModelTable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

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
    private static PropertiesUtil propertiesUtil = new PropertiesUtil(PropertiesLables.PROPERTIES_PATH);

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
                    printStackTrace(e);
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
     * @param maxRtD {@link RuntimeData} max
     * @param e {@link RuntimeData} small num
     * @return {@link VehicleHttpPack}
     */
    public static VehicleHttpPack normalize(VehicleHttpPack pack, RuntimeData minRtD, RuntimeData maxRtD, Double e) throws Exception{
        Iterator<Map.Entry<Long, RuntimeData>> iter = pack.getVehicle().getRtDataMap().entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<Long, RuntimeData> item = iter.next();
            RuntimeData rtd = item.getValue();
            try {
                rtd.normalize(minRtD, maxRtD, e);
            } catch (Exception ex) {
                throw ex;
            }
        }
        return pack;
    }

    /**
     * trans to Double[][]
     * @param pack VehicleHttpPack
     * @return Double[][]
     */
    public static DataMatrix toDataMatrix (VehicleHttpPack pack) {
        Double[][] arrayT =  pack.getVehicle().toArrayT();
        Long startTime = pack.getVehicle().getRtDataMap().firstKey();
        String vType = pack.getVehicle().getVtype();
        return new DataMatrix(arrayT, startTime, vType);
    }

    /**
     * trans to InputCorrMatrix, which can input to Model directly
     * @param dataMatrix DataMatrix
     * @return 特征转化为相关矩阵
     */
    public static InputCorrMatrix toInputCorrMatrix (DataMatrix dataMatrix) throws IllWinNum{
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
        int dataNum = dataMatrix.getMatrix()[0].length;
        Double[][] data = dataMatrix.getMatrix();

        // get win
        String[] winStrs = propertiesUtil.getPropertiesList(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_WIN));
        if (winStrs.length != winNum) {
            throw new IllWinNum(winNum, winStrs.length);
        }

        for (int winIdx = 0; winIdx < winNum; winIdx ++) {
            int winInt = Integer.parseInt(winStrs[winIdx]);
            for (int i = 0; i < featureNum; i ++) {
                for (int j = i; j < featureNum ; j ++) {
                    // must leave window
                    for (int t = 0, step = 0; t <= dataNum - winInt && step < maxStep; t += gapTime, step ++) {
                        for (int win = 0; win < winInt; win ++) {
                            matrix[step][i][j][winIdx] += data[i][t + win] * data[j][t + win];
                        }
                        matrix[step][i][j][winIdx] /= winInt;
                        matrix[step][j][i][winIdx] = matrix[step][i][j][winIdx];
                    }
                }
            }
        }
        Long startTime = dataMatrix.getStartTime();
        String vType = dataMatrix.getVtype();
        return new InputCorrMatrix(matrix,startTime ,vType);
    }

    /**
     * trans to correlation matrix loss
     * @param input input matrix
     * @return output matrix loss to input matrix
     */
    public static OutputCorrMatrix toCorrMatrixLoss (InputCorrMatrix input) {
        Long startTime = input.getStartTime();
        String vType = input.getVtype();
        Double[][][][] inputMatrix = input.getMatrix();
        int featureNum1 = inputMatrix[0].length;
        int featureNum2 = inputMatrix[0][0].length;
        assert featureNum1 == featureNum2;
        int winNum = inputMatrix[0][0][0].length;
        // TODO call TensorFlow Serving
        Double[][][][] output = inputMatrix;
        // which step to be used to compute loss , which is start from 1, end wich max step, need to -1
        int evalStep = Integer.parseInt(propertiesUtil.readValue(PropertiesLables.DATA_MATRIX_STEP_TO_COMPUTE_LOSS)) - 1;
        Double[][][][] evalInput = new Double[1][featureNum1][featureNum1][winNum];
        System.arraycopy(evalInput[0], 0, inputMatrix[evalStep], 0, featureNum1*featureNum1*winNum );
        Double[][][][] loss = getMatrixLoss(evalInput, output);
        return new OutputCorrMatrix(loss, startTime, vType);
    }

    /**
     * get matrix loss, which will pow to be abs
     * @param input input
     * @param output output
     * @return Double[][][][]
     */
    private static Double[][][][] getMatrixLoss (Double[][][][] input, Double[][][][] output) {
        int d1 = input.length; assert d1 == output.length;
        int d2 = input[0].length; assert d2 == output[0].length;
        int d3 = input[0][0].length; assert d3 == output[0][0].length;
        int d4 = input[0][0][0].length; assert d4 == output[0][0][0].length;
        Double[][][][] loss = new Double[d1][d2][d3][d4];
        for (int stepIdx = 0; stepIdx < d1; stepIdx ++) {
            for (int featureNum1Idx = 0; featureNum1Idx < d2; featureNum1Idx ++) {
                for (int featureNum2Idx = 0; featureNum2Idx < d3; featureNum2Idx ++) {
                    for (int winNumIdx = 0; winNumIdx < d4; winNumIdx ++) {
                        // loss
                        loss[stepIdx][featureNum1Idx][featureNum2Idx][winNumIdx] -= input[stepIdx][featureNum1Idx][featureNum2Idx][winNumIdx];
                        // square to be fabs
                        loss[stepIdx][featureNum1Idx][featureNum2Idx][winNumIdx] = Math.pow(loss[stepIdx][featureNum1Idx][featureNum2Idx][winNumIdx], 2);
                    }
                }
            }
        }
        return loss;
    }
}
