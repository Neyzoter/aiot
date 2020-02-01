package cn.neyzoter.aiot.common.tensorflow;

import org.tensorflow.*;

import java.util.List;

/**
 * Tensorflow Model Manager
 * @author Neyzoter Song
 * @date 2020-01-05
 */
public class ModelManager {

    private SavedModelBundle modelBundle;

    /**
     * 模型管理构造函数
     * @param path 模型的路径
     */
    public ModelManager (String path, String tag){
        System.out.println(String.format("Start loading model: %s  tag: %s", path, tag));
        try {
            this.loadModelBundle(path, tag);
            System.out.println(String.format("Load model: %s  tag: %s finished", path, tag));
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    /**
     * 加载1个模型
     * @param path
     */
    public void loadModelBundle (String path, String tag) {
            this.modelBundle = SavedModelBundle.load(path, tag);
    }

    /**
     * 获取session
     * @return
     */
    public SavedModelBundle getModelBundle() {
        return this.modelBundle;
    }

    /**
     * 测试模型是否可以运行，只用于一个模型
     *
     */
    public void testModelBundle () {
        Session session = this.modelBundle.session();
        float[][][][] a = new float[5][30][30][3];
        Tensor input_x = Tensor.create(a);
        List<Tensor<?>> out = session.runner().feed("data_input", input_x).fetch("data_output").run();
        System.out.print(String.format("data_output is ") + out.toString() + "\n");

        for (Tensor s : out) {
            float[][][][] t = new float[1][30][30][3];
            s.copyTo(t);
            System.out.println("Output data : ");
            for (int i = 0 ; i < 30; i++) {
                for (int j = 0; j < 30; j ++) {
                    for (int x = 0; x < 3; x ++) {
                        boolean ifPrint = (i == 0 && j < 10) || (i == 29 && j > 20);
                        if (ifPrint){
                            System.out.println(String.format("[ %.4f, %.4f, %.4f]",t[0][i][j][0],t[0][i][j][1],t[0][i][j][2]));
                        }
                    }
                }
                if (i !=0 && i != 29) {
                    System.out.println("   .....     ");
                }
            }
        }
    }
}
