package cn.neyzoter.aiot.common.tensorflow;

import org.junit.Test;

/**
 * 测试Tensorflow 模型管理
 * @author Neyzoter Song
 * @date 2020-1-30
 */
public class TestModelManager {
    ModelManager diagnosisModel = new ModelManager("/home/scc/code/java/aiot/common/tf_model/saved_model/","mytag");
//    ModelManager diagnosisModel = new ModelManager("/home/scc/code/java/aiot/common/tf_model/model2/","mytag");
    @Test
    public void testDiagnosisModel () {
        System.out.println("Starting test.");
        System.out.println(diagnosisModel.getModelBundle().session().toString());
    }

}
