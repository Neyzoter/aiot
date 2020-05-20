package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Rt data bound table
 * @author Neyzoter Song
 * @date 2020-5-20
 */
@Component
public class RtDataBoundTable implements Serializable {
    private static final long serialVersionUID = -4477300658723541224L;
    public static final Logger logger = LoggerFactory.getLogger(RtDataBoundTable.class);

    private Map<String, RtDataBound> boundMap;
    private PropertiesUtil propertiesUtil;

    @Autowired
    public RtDataBoundTable (PropertiesUtil propertiesUtil) {
        this.boundMap = new ConcurrentHashMap<>();
        this.propertiesUtil = propertiesUtil;
    }

    /**
     * get a bound
     * @param key key
     * @return Rt Data Bound
     */
    public RtDataBound getRtDataBound(String key) {
        return boundMap.get(key);
    }

    /**
     * size of table
     * @return size of table
     */
    public int size () {
        return boundMap.size();
    }
    /**
     * put new bound into hash
     * @param key key
     * @param bound value - Rt Data Bound
     */
    public void putRtDataBound(String key, RtDataBound bound) {
        boundMap.put(key, bound);
    }

    /**
     * inc all bound and check, if timeout then rm<br/>
     * do not use maxAliveTime of {@link RtDataBound}, but use the maxAliveTime in properties file
     * @return {@link List} removed bounds' key list
     */
    public List<String> aliveIncCheck () {
        Iterator<Map.Entry<String, RtDataBound>> iter = boundMap.entrySet().iterator();
        List<String> rmList = new LinkedList<>();
        // get max alive time from properties, properties update periodicity
        int maxAliveTime = getMaxAliveTime();
        // get the scheduled executor's period
        int incTime = getCheckPeriod();
        for (;iter.hasNext();) {
            Map.Entry<String, RtDataBound> entry = iter.next();
            // inc the alive time
            entry.getValue().aliveTimeInc(incTime);
            // if time out
            if (entry.getValue().isTimeout(maxAliveTime)) {
                rmList.add(entry.getKey());
                // remove the bound
                iter.remove();
            }
        }
        return rmList;
    }
    /**
     * get model path
     * @param propertiesUtil properties util
     * @return model path
     */
    public String getBoundPath (PropertiesUtil propertiesUtil) {
        String path = propertiesUtil.readValue(PropertiesLables.TENSORFLOW_MODEL_PATH);
        return path;
    }

    /**
     * set max alive time
     * @param key key - vtype
     * @param time time , unit is same with maxAliveTime of {@link RtDataBound}
     */
    public void setMaxAliveTime (String key , int time) {
        boundMap.get(key).setMaxAliveTime(time);
    }

    /**
     * load and put a RtDataBound into map
     * @param key key - vtype
     * @param path path of model
     * @param time max alive time, if alive time bigger than time, the model will be GC
     */
    public void loadPut (String key, String path, int time) {
        try {
            logger.info(String.format("Start loading bound: %s", path));
            long startime = System.currentTimeMillis();
            RtDataBound rtDataBound = new RtDataBound(path, key, time);
            this.putRtDataBound(key, rtDataBound);
            logger.info(String.format("Loaded model: %s finished in %d ms", path, System.currentTimeMillis() - startime));
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    /**
     * put a model manager into map with a default max alive time
     * @param key key - vtype
     * @param path path of model
     */
    public void loadPut (String key, String path) {
        this.loadPut(key, path, Integer.MAX_VALUE);
    }

    /**
     * get RtDataBound and Reset it's aliveTime to zero
     * @param key key
     * @return {@link RtDataBound}
     */
    public RtDataBound getRtDataBoundTimeReset (String key) {
        RtDataBound rtDataBound = this.getRtDataBound(key);
        // reset alive time
        rtDataBound.setAliveTime(0);
        return rtDataBound;
    }

    /**
     * reset alive time to zero
     * @param key key
     */
    public void resetAliveTime (String key) {
        boundMap.get(key).setAliveTime(0);
    }
    /**
     * get time unit from properties file
     * @return {@link TimeUnit}
     */
    public TimeUnit getCheckTimeUnit () {
        String unit = this.propertiesUtil.readValue(PropertiesLables.TF_MODEL_CHECK_UNIT);
        unit = unit.trim();
        switch (unit) {
            case PropertiesValueRange.UNIT_HOUR:
                return TimeUnit.HOURS;
            case PropertiesValueRange.UNIT_MINUTTE:
                return TimeUnit.MINUTES;
            case PropertiesValueRange.UNIT_SECOND:
                return TimeUnit.SECONDS;
            default:
                logger.warn("Period Unit Unrecognized， set as default SECONDS");
                return TimeUnit.SECONDS;

        }
    }

    /**
     * get period
     * @return period
     */
    public int getCheckPeriod () {
        String period = this.propertiesUtil.readValue(PropertiesLables.TF_MODEL_CHECK_PERIOD);
        return Integer.parseInt(period);
    }

    /**
     * get max alive time
     * @return maxAliveTime
     */
    public int getMaxAliveTime () {
        String period = this.propertiesUtil.readValue(PropertiesLables.TENSORFLOW_MODEL_MAX_ALIVE_TIME);
        return Integer.parseInt(period);
    }
}
