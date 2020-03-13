package cn.neyzoter.aiot.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties Util
 * @author neyzoter song
 * @date 2019-12-11
 */
public class PropertiesUtil implements Serializable {
    private static final long serialVersionUID = -3031705469789764720L;
    private static final int MAX_PROPERTIES_MAP_CAP = 100;
    private static final String PROPERTIES_SEPERATOR = ",";
    private static final String KEY_VAL_SEPERATOR = ":";
    /**
     * properties
     */
    private Properties props=null;

    /**
     * file path
     */
    private String path;

    /**
     * load properties file
     */
    public PropertiesUtil(String path){
            this.updateProps(path);
    }

    /**
     * read property
     * @param key key
     * @return value
     */
    public String readValue(String key) {
        return  props.getProperty(key);
    }

    /**
     * 更新属性
     * @param path properties file
     */
    public void updateProps (String path) {
        this.path = path;
        this.updateProps();

    }
    /**
     * 获取属性
     * @return
     */
    public void updateProps (){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.path));
            props = new Properties();
            props.load(bufferedReader);
        }catch (Exception e) {
            System.err.println(e);
        }
    }
    /**
     * 获取props
     * @return
     */
    public Properties getProps () {
        return props;
    }

    /**
     * trans json format properties to map
     * @param properties json format properties <br/> such as {val1:0.12,val2:2.2}
     * @return properties Map
     */
    public Map<String, String > getPropertiesMap (String properties) {
        Map<String, String> map = new HashMap<>(MAX_PROPERTIES_MAP_CAP);
        properties = properties.trim();
        // rm "{" and "}" and " "
        properties = properties.replaceAll("[\\{|\\}| ]","");
        // split
        String[] propertisArray = properties.split(PROPERTIES_SEPERATOR);
        for (String kv : propertisArray) {
            String[] kvArray = kv.split(KEY_VAL_SEPERATOR,2);
            map.put(kvArray[0],kvArray[1]);
        }
        return map;
    }

}
