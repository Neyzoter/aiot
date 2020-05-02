package cn.neyzoter.aiot.common.tensorflow;

import cn.neyzoter.aiot.common.util.PathUtil;
import cn.neyzoter.aiot.common.util.PropertiesUtil;
import org.tensorflow.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Tensorflow Model Manager
 * @author Neyzoter Song
 * @date 2020-01-05
 */
public class ModelManager implements Serializable {
    private static final long serialVersionUID = 4361723474900005046L;

    private static final String OPERATION_DATA_INPUT = "data_input";
    private static final String OPERATION_DATA_OUTPUT = "data_output";
    /**
     * model
     */
    private SavedModelBundle modelBundle;

    /**
     * model properteis
     */
    private PropertiesUtil propertiesUtil;

    /**
     * max value map
     */
    private Map<String, String> maxValueMap;

    /**
     * min value map
     */
    private Map<String, String> minValueMap;

    /**
     * normalized e
     */
    private Double normalizedE;
    /**
     * alive time from last contact, when data sended , alive time will update to be 0
     */
    private int aliveTime;
    /**
     * model's max alive time from last contact
     */
    private int maxAliveTime;

    /**
     * model manager's key
     */
    private String key;

    /**
     * Class ModelManager build, maxAliveTime is 2
     * @param path model's path
     * @param tag model's tag
     * @param k key
     */
    public ModelManager (String path, String tag, String k) throws Exception {
        ModelManagerInit(path,tag,k,Integer.MAX_VALUE);
    }
    /**
     * Class ModelManager build
     * @param path model's path
     * @param tag model's tag
     * @param k key
     * @param maxAliveTime model's max alive time from last contact
     */
    public ModelManager (String path, String tag,String k, int maxAliveTime)  throws Exception{
        ModelManagerInit(path,tag,k,maxAliveTime);
    }

    /**
     * ModelManager init func
     * @param path path
     * @param tag tag
     * @param k key
     * @param maxAliveTime maxAliveTime
     */
    private void ModelManagerInit (String path, String tag, String k, int maxAliveTime) throws Exception{
        path = PathUtil.getPathEndWithSlash(path);
        this.loadModelBundle(path, tag);
        this.aliveTime = 0;
        this.maxAliveTime = maxAliveTime;
        this.key = k;
        this.propertiesUtil = new PropertiesUtil(path + ModelPropertiesLabels.MODEL_PROPERTIES_NAME);
        // below must put after propertiesUtil's init
//        this.updateMaxValueMap();
        String maxValues = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MAX_VALUE);
        Map maxMap = propertiesUtil.getPropertiesMap(maxValues);
        this.setMaxValueMap(maxMap);

//        this.updateMinValueMap();
        String minValues = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MIN_VALUE);
        Map minMap = propertiesUtil.getPropertiesMap(minValues);
        this.setMinValueMap(minMap);

//        this.updateNormalizedE();
        String eStr = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_NORMALIZED_E);
        Double e = Double.parseDouble(eStr);
        this.setNormalizedE(e);
    }

    /**
     * load a model
     * @param path path
     */
    private void loadModelBundle (String path, String tag) {
            this.modelBundle = SavedModelBundle.load(path, tag);
    }

    /**
     * get model bundle
     * @return {@link SavedModelBundle}
     */
    public SavedModelBundle getModelBundle() {
        return this.modelBundle;
    }

    /**
     * compute the result
     * @param input data
     * @param step result 0-Dimension
     * @param featureNum result 1-Dimension and 2-Dimension
     * @param winSize result 3-Dimension
     * @return result
     */
    public Double[][][][] getOutput (Double[][][][] input, int step, int featureNum, int winSize) {
        long time = System.currentTimeMillis();
        Session session = this.modelBundle.session();
        Tensor inputTensor = Tensor.create(input);
        List<Tensor<?>> outList = session.runner().feed(OPERATION_DATA_INPUT, inputTensor).fetch(OPERATION_DATA_OUTPUT).run();
        System.out.print(String.format("data_output is %s , compute with %d ms\n" , outList.toString() ,System.currentTimeMillis() - time) );
        Tensor outTs = outList.get(0);
        Double[][][][] outMatrix = new Double[step][featureNum][featureNum][winSize];
        outTs.copyTo(outMatrix);
        return outMatrix;
    }
    /**
     * test model
     *
     */
    public void testModelBundle () {
        try {

            Session session = this.modelBundle.session();
            float[][][][] a = new float[5][30][30][3];
            FileInputStream fin = new FileInputStream("/home/scc/code/java/aiot/common/tf_model/nescar/train_data_20_txt.txt");
            InputStreamReader reader = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(reader);
            String line = buffReader.readLine();
            String[] data_str_float = line.split(",");
            System.out.println("data_str_float len is " + data_str_float.length);
            int x1 = 0,x2 = 0,x3 = 0, x4 = 0;
            for (int i = 0 ; i < data_str_float.length ; i ++) {
                float aFloat = Float.parseFloat(data_str_float[i]);
                a[x1][x2][x3][x4] = aFloat;
//                System.out.print(aFloat);
//                System.out.print(", ");
                x4 ++;
                if (x4 == 3) {
                    x4 = 0;
                    x3 ++;
//                    System.out.print("\n");
                }
                if (x3 == 30) {
                    x3 = 0;
                    x2 ++;
                }
                if (x2 == 30) {
                    x2 = 0;
                    x1 ++;
                }
            }
            System.out.println(String.format("x1 : %d , x2 : %d , x3 : %d , x4 : %d",x1, x2,x3,x4));
            long time = System.currentTimeMillis();
            Tensor input_x = Tensor.create(a);
            List<Tensor<?>> out = session.runner().feed(OPERATION_DATA_INPUT, input_x).fetch(OPERATION_DATA_OUTPUT).run();
            System.out.print(String.format("data_output is %s , compute with %d ms\n" , out.toString() ,System.currentTimeMillis() - time) );
            for (Tensor s : out) {
                float[][][][] t = new float[1][30][30][3];
                s.copyTo(t);
                System.out.println("Output data : ");
                for (int i = 0 ; i < 30; i++) {
                    for (int j = 0; j < 30; j ++) {
                        boolean ifPrint = (i == 0 && j < 10) || (i == 29 && j > 20);
                        if (ifPrint){
                            System.out.println(String.format("data_output[0][%d][%d][0:2] = [ %.4f, %.4f, %.4f]",i,j,t[0][i][j][0],t[0][i][j][1],t[0][i][j][2]));
                        }
                    }
                    if (i !=0 && i != 29) {
                        System.out.println("   .....     ");
                    }
                }
            }
        }catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * if alive time is bigger than max alive time, that is timeout, need to be GC
     * @return true or false
     */
    public boolean isTimeout () {
        return this.aliveTime > this.maxAliveTime;
    }

    /**
     * if alive time is bigger than max alive time(set by user), that is timeout, need to be GC
     * @return true or false
     */
    public boolean isTimeout (int time) {
        return this.aliveTime > time;
    }
    /**
     * increase the alive time by 1
     * @return alive time
     */
    public int aliveTimeInc () {
        this.aliveTime ++;
        return this.aliveTime;
    }

    /**
     * increase the alive time by time
     * @param time increase time
     * @return alive time
     */
    public int aliveTimeInc (int time) {
        this.aliveTime += time;
        return this.aliveTime;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public void setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
    }


    public int getMaxAliveTime() {
        return maxAliveTime;
    }
    public void setMaxAliveTime(int maxAliveTime) {
        this.maxAliveTime = maxAliveTime;
    }

    /**
     * model manager's key
     * @return String key
     */
    public String getKey() {
        return key;
    }

    /**
     * model manager's key
     * @param key key
     */
    private void setKey(String key) {
        this.key = key;
    }

    /**
     * get model's properties
     * @return {@link PropertiesUtil}
     */
    public PropertiesUtil getPropertiesUtil() {
        return propertiesUtil;
    }

    /**
     * update properties util
     */
    public void updatePropertiesUtil (){
        this.propertiesUtil.updateProps();
    }

    /**
     * get max value map
     * @return max value map
     */
    public Map<String, String> getMaxValueMap() {
        return maxValueMap;
    }
    /**
     * set max value map
     * @param map max value map
     */
    private void setMaxValueMap(Map map) {
        this.maxValueMap = map;
    }
    /**
     * update max value map from disk
     * @return max value map
     */
    public Map<String, String> updateMaxValueMap (){
        propertiesUtil.updateProps();
        String maxValues = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MAX_VALUE);
        Map map = propertiesUtil.getPropertiesMap(maxValues);
        this.setMaxValueMap(map);
        return maxValueMap;
    }
    /**
     * get min value map
     * @return min value map
     */
    public Map<String, String> getMinValueMap() {
        return minValueMap;
    }
    /**
     * set min value map
     * @param map max value map
     */
    private void setMinValueMap(Map map) {
        this.minValueMap = map;
    }
    /**
     * update min value map from disk
     * @return max value map
     */
    public Map<String, String> updateMinValueMap (){
        propertiesUtil.updateProps();
        String minValues = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MIN_VALUE);
        Map map = propertiesUtil.getPropertiesMap(minValues);
        this.setMinValueMap(map);
        return minValueMap;
    }

    /**
     * get normalized e
     * @return normalizedE
     */
    public Double getNormalizedE() {
        return normalizedE;
    }

    /**
     * set normalized e
     * @param normalizedE small constant for normalizing
     */
    public void setNormalizedE(Double normalizedE) {
        this.normalizedE = normalizedE;
    }

    /**
     * update normalizedE from disk
     * @return normalizedE
     */
    public Double updateNormalizedE () {
        propertiesUtil.updateProps();
        String eStr = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_NORMALIZED_E);
        Double e = Double.parseDouble(eStr);
        this.setNormalizedE(e);
        return normalizedE;
    }
}
