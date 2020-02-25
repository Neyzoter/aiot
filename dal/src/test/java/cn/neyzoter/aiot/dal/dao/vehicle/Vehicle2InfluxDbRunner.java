package cn.neyzoter.aiot.dal.dao.vehicle;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * test Vehicle2InfluxDb runner
 * @author Neyzoter Song
 * @date 2020-2-25
 */
public class Vehicle2InfluxDbRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestVehicle2InfluxDb.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

    }
}
