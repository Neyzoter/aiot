package cn.neyzoter.aiot.dal.dao.vehicle;

import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import org.junit.Test;

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
}
