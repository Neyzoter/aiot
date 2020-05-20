package cn.neyzoter.aiot.common.tensorflow;

import cn.neyzoter.aiot.common.util.PathUtil;
import cn.neyzoter.aiot.common.util.PropertiesUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * Runtime Data Configer
 * @author Neyzoter Song
 * @date 2020-05-20
 */
public class RtdataConfiger implements Serializable {
    private static final long serialVersionUID = -6238424403217820513L;

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
     * RtdataConfiger's max alive time from last contact
     */
    private int maxAliveTime;

    /**
     * RtdataConfiger's key
     */
    private String key;

    /**
     *
     * @param path properties file directory
     * @param k key
     * @param maxAliveTime
     * @throws Exception
     */
    public RtdataConfiger (String path, String k, int maxAliveTime) throws Exception  {
        ConfigerInit(path,k,maxAliveTime);
    }


    public RtdataConfiger (String path, String k) throws Exception {
        ConfigerInit(path,k,Integer.MAX_VALUE);
    }
    /**
     * ModelManager init func
     * @param path path
     * @param k key
     * @param maxAliveTime maxAliveTime
     */
    private void ConfigerInit (String path, String k, int maxAliveTime) throws Exception{
        path = PathUtil.getPathEndWithSlash(path);
        this.aliveTime = 0;
        this.maxAliveTime = maxAliveTime;
        this.key = k;
        this.propertiesUtil = new PropertiesUtil(path + ModelPropertiesLabels.MODEL_PROPERTIES_NAME);
        // below must put after propertiesUtil's init
        String maxValues = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MAX_VALUE);
        Map maxMap = propertiesUtil.getPropertiesMap(maxValues);
        this.setMaxValueMap(maxMap);

        String minValues = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MIN_VALUE);
        Map minMap = propertiesUtil.getPropertiesMap(minValues);
        this.setMinValueMap(minMap);

        String eStr = propertiesUtil.readValue(ModelPropertiesLabels.MODLE_NORMALIZED_E);
        Double e = Double.parseDouble(eStr);
        this.setNormalizedE(e);
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
     * Rtdata Configer's key
     * @return String key
     */
    public String getKey() {
        return key;
    }

    /**
     * Rtdata Configer's key
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
