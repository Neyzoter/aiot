package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.tensorflow.ModelManager;
import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * vehicle model table
 * @author Neyzoter Song
 * @date 2020-3-7
 */
public class VehicleModelTable {
    private Map<String, ModelManager> modelMap;
    VehicleModelTable () {
        modelMap = new ConcurrentHashMap<>();
    }

    /**
     * get a model manager
     * @param key key
     * @return ModelManager {@link ModelManager}
     */
    public ModelManager getModelManager(String key) {
        return modelMap.get(key);
    }

    /**
     * put new model manager into hash
     * @param key key
     * @param manager value - manager
     */
    public void putModelManager(String key, ModelManager manager) {
        modelMap.put(key, manager);
    }

    /**
     * check all model manager is time out or not, if time out , remove it
     * @return {@link List} removed managers' key list
     */
    public List<String> aliveCheckUpdate () {
        Iterator<Map.Entry<String, ModelManager>> iter = modelMap.entrySet().iterator();
        List<String> rmList = new LinkedList<>();
        for (;iter.hasNext();) {
            ModelManager manager = iter.next().getValue();
            // if time out
            if (manager.isTimeout()) {
                String key = iter.next().getKey();
                rmList.add(key);
                // remove the model manager
                modelMap.remove(key);
            }
        }
        return rmList;
    }

    /**
     * refresh alive time, if alive time exceed the max alive time , this k-v will be GC
     * @param key key
     */
    public void resetAliveTime (String key) {
        modelMap.get(key).setAliveTime(0);
    }

    /**
     * set max alive time
     * @param key key - vtype
     * @param time time , unit is same with maxAliveTime of {@link ModelManager}
     */
    public void setMaxAliveTime (String key , int time) {
        modelMap.get(key).setMaxAliveTime(time);
    }

    /**
     * put a model manager into map
     * @param key key - vtype
     * @param path path of model
     * @param tag tag of model
     * @param time max alive time, if alive time bigger than time, the model will be GC
     */
    public void put (String key, String path, String tag, int time) {
        modelMap.put(key, new ModelManager(path, tag, time));
    }

    /**
     * put a model manager into map with a default max alive time
     * @param key key - vtype
     * @param path path of model
     * @param tag tag of model
     */
    public void put (String key, String path, String tag) {
        modelMap.put(key, new ModelManager(path, tag));
    }

    /**
     * get model path
     * @param propertiesUtil properties util
     * @return model path
     */
    public String getModelPath (PropertiesUtil propertiesUtil) {
        String path = propertiesUtil.readValue(PropertiesLables.TENSORFLOW_MODEL_PATH);
        return path;
    }
}
