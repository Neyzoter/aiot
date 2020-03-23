package cn.neyzoter.aiot.dal.dao.vehicle;

import cn.neyzoter.aiot.common.tensorflow.ModelManager;
import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * Runtime data Tester
 * @author Neyzoter Song
 * @date 2020-3-6
 */
public class TestRuntimeData {
    @Test
    public void testRuntimeDataToFields () {
        RuntimeData runtimeData = new RuntimeData();
        runtimeData.setVal1(1.2);runtimeData.setVal2(1.2);runtimeData.setVal3(1.3);
        System.out.println(runtimeData.toFields());
        System.out.println(runtimeData.toString());
    }

    /**
     * test fields
     */
    @Test
    public void testFields () {
        Field[] fields = RuntimeData.class.getDeclaredFields();
        for (Field f : fields) {
            System.out.print(f.getName() + " ");
        }
        try {
            RuntimeData runtimeData = new RuntimeData();
//            ModelManager modelManager = new ModelManager("/home/scc/code/java/aiot/common/tf_model/nescar/","mytag", "mazida");
            long startTime = System.currentTimeMillis();
            String prefix = "val";
            String val;
            for (int i = 100; i > 0; i --) {
                for (int j = 30; j > 0 ; j --) {
                    val = prefix + j;
                    Field field = RuntimeData.class.getDeclaredField(val);
                    field.set(runtimeData, 1.4);
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("\nSpend time : " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
