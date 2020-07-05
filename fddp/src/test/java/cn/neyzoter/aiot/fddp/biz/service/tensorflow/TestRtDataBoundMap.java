package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * Test RtDataBoundMap
 * @author Charles Song
 * @date 2020-5-20
 */
public class TestRtDataBoundMap {

    @Test
    @Autowired
    public void testBoundMapRead () {
        PropertiesManager p = new PropertiesManager();
        RtDataBoundMap boundMap = new RtDataBoundMap(p);
        Iterator<Map.Entry<String, RtDataBound>> iter = boundMap.entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<String, RtDataBound> entry = iter.next();
            System.out.println(String.format("vtype : %s", entry.getKey()));
        }
    }

    @Test
    public void testFileList () {
        RtDataBoundMap.getAllPropAbsPath("/home/scc/code/java/aiot/common/tf_model/nescar/");
    }
}
