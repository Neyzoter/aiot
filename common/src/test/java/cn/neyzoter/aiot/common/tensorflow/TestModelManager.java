package cn.neyzoter.aiot.common.tensorflow;

import org.junit.Test;

/**
 * 测试Tensorflow 模型管理
 * @author Neyzoter Song
 * @date 2020-1-30
 */
public class TestModelManager {
    ModelManager diagnosisModel;
//    ModelManager diagnosisModel = new ModelManager("/home/scc/code/java/aiot/common/tf_model/model2/","mytag");
    @Test
    public void testDiagnosisModel () {
        try {
            diagnosisModel = new ModelManager("/home/scc/code/java/aiot/common/tf_model/saved_model/","mytag", "mazida");
            System.out.println("==== testDiagnosisModel ====");
            diagnosisModel.testModelBundle();
        }catch (Exception e) {
            System.err.println(e);
        }

    }

}
