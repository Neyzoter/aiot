package cn.neyzoter.aiot.common.util;


import cn.neyzoter.aiot.common.tensorflow.TestModelManager;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * tensorflow的测试Runner
 * @author Neyzoter Song
 * @date 2020-1-30
 */
public class TestRunner4Util {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestPropertiesUtil.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

    }
}
