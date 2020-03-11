package cn.neyzoter.aiot.common.util;

import cn.neyzoter.aiot.common.tensorflow.ModelPropertiesLabels;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

/**
 * test properties Util
 * @author Neyzoter Song
 * @date 2020-3-11
 */
public class TestPropertiesUtil {
    private PropertiesUtil propertiesUtil = new PropertiesUtil("/home/scc/code/java/aiot/common/tf_model/nescar/model.properties");
    @Test
    public void testGetPropertiesMap () {
        Map<String, String> maxValueMap = propertiesUtil.getPropertiesMap(propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MAX_VALUE));
        Map<String, String> minValueMap = propertiesUtil.getPropertiesMap(propertiesUtil.readValue(ModelPropertiesLabels.MODLE_MIN_VALUE));
        Iterator<Map.Entry<String, String>> iterMin = maxValueMap.entrySet().iterator();
        Iterator<Map.Entry<String, String>> iterMax = minValueMap.entrySet().iterator();
        System.out.println("Max Value Map : ");
        for (;iterMax.hasNext();) {
            Map.Entry<String,String> entry = iterMax.next();
            System.out.println(String.format("  %s : %s", entry.getKey(),entry.getValue()));
        }
        System.out.println("Min Value Map : ");
        for (;iterMin.hasNext();) {
            Map.Entry<String,String> entry = iterMin.next();
            System.out.println(String.format("  %s : %s", entry.getKey(),entry.getValue()));
        }
    }
}
