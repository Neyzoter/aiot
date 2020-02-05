package cn.neyzoter.aiot.common.tensorflow;

import org.tensorflow.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
            long time = System.currentTimeMillis();
            this.loadModelBundle(path, tag);
            System.out.println(String.format("Load model: %s  tag: %s finished with %d ms", path, tag, System.currentTimeMillis() - time));
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
        try {

            Session session = this.modelBundle.session();
            float[][][][] a = new float[5][30][30][3];
            FileInputStream fin = new FileInputStream("/home/scc/code/java/aiot/common/tf_model/saved_model/train_data_20_txt.txt");
            InputStreamReader reader = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(reader);
            String line = buffReader.readLine();
            String[] data_str_float = line.split(",");
            System.out.println("data_str_float len is " + data_str_float.length);
            int x1 = 0,x2 = 0,x3 = 0, x4 = 0;
            for (int i = 0 ; i < data_str_float.length ; i ++) {
                float aFloat = Float.parseFloat(data_str_float[i]);
                a[x1][x2][x3][x4] = aFloat;
//                System.out.print(aFloat);
//                System.out.print(", ");
                x4 ++;
                if (x4 == 3) {
                    x4 = 0;
                    x3 ++;
//                    System.out.print("\n");
                }
                if (x3 == 30) {
                    x3 = 0;
                    x2 ++;
                }
                if (x2 == 30) {
                    x2 = 0;
                    x1 ++;
                }
            }
            System.out.println(String.format("x1 : %d , x2 : %d , x3 : %d , x4 : %d",x1, x2,x3,x4));
            long time = System.currentTimeMillis();
            Tensor input_x = Tensor.create(a);
            List<Tensor<?>> out = session.runner().feed("data_input", input_x).fetch("data_output").run();
            System.out.print(String.format("data_output is %s , compute with %d ms\n" , out.toString() ,System.currentTimeMillis() - time) );
            for (Tensor s : out) {
                float[][][][] t = new float[1][30][30][3];
                s.copyTo(t);
                System.out.println("Output data : ");
                for (int i = 0 ; i < 30; i++) {
                    for (int j = 0; j < 30; j ++) {
                        boolean ifPrint = (i == 0 && j < 10) || (i == 29 && j > 20);
                        if (ifPrint){
                            System.out.println(String.format("data_output[0][%d][%d][0:2] = [ %.4f, %.4f, %.4f]",i,j,t[0][i][j][0],t[0][i][j][1],t[0][i][j][2]));
                        }
                    }
                    if (i !=0 && i != 29) {
                        System.out.println("   .....     ");
                    }
                }
            }
        }catch (Exception e) {
            System.err.println(e);
        }
    }
}
