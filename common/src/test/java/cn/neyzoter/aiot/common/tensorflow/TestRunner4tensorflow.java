package cn.neyzoter.aiot.common.tensorflow;


import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

import org.junit.runner.Result;

/**
 * tensorflow的测试Runner
 * @author Neyzoter Song
 * @date 2020-1-30
 */
public class TestRunner4tensorflow {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestModelManager.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

    }
}
