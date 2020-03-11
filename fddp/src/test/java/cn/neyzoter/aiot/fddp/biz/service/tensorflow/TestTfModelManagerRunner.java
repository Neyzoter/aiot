package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * test Vehicle2InfluxDb runner
 * @author Neyzoter Song
 * @date 2020-2-25
 */
public class TestTfModelManagerRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestTfModelManager.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

    }
}
