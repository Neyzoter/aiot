package cn.neyzoter.aiot.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取文件中的参数
 * @author neyzoter song
 * @daa 2019-12-11
 */
public class PropertiesUtil {
    /**
     * 属性
     */
    private Properties props=null;

    private String path;

    /**
     * 装载属性文件
     * @throws IOException
     */
    public PropertiesUtil(String path){
            this.updateProps(path);
    }

    /**
     * 读取属性
     * @param key
     * @return
     * @throws IOException
     */
    public String readValue(String key) {
        return  props.getProperty(key);
    }

    /**
     * 更新属性
     * @param path
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

}
