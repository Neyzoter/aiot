package cn.neyzoter.aiot.common.tensorflow;

import org.junit.Test;

/**
 * 测试Tensorflow 模型管理
 * @author Neyzoter Song
 * @date 2020-1-30
 */
public class TestModelManager {
//    ModelManager diagnosisModel = new ModelManager("/home/scc/code/java/aiot/common/tf_model/pb/frozen_model30.pb");
    ModelManager diagnosisModel = new ModelManager("/home/scc/code/java/aiot/common/tf_model/saved_model/","mytag");
    @Test
    public void testDiagnosisModel () {
        System.out.println("Starting test.");
        System.out.println(diagnosisModel.getModelBundle().session().toString());
    }

}
