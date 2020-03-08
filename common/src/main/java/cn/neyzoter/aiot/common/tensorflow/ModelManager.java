package cn.neyzoter.aiot.common.tensorflow;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.tensorflow.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

/**
 * Tensorflow Model Manager
 * @author Neyzoter Song
 * @date 2020-01-05
 */
public class ModelManager implements Serializable {
    private static final long serialVersionUID = 4361723474900005046L;
    /**
     * model
     */
    private SavedModelBundle modelBundle;
    /**
     * alive time from last contact, when data sended , alive time will update to be 0
     */
    private int aliveTime;
    /**
     * model's max alive time from last contact
     */
    private int maxAliveTime;

    /**
     * model manager's key
     */
    private String key;

    /**
     * Class ModelManager build, maxAliveTime is 2
     * @param path model's path
     * @param tag model's tag
     * @param k key
     */
    public ModelManager (String path, String tag, String k) throws Exception {
        try {
            ModelManagerInit(path,tag,k,Integer.MAX_VALUE);
        }catch (Exception e) {
            throw e;
        }
    }
    /**
     * Class ModelManager build
     * @param path model's path
     * @param tag model's tag
     * @param k key
     * @param maxAliveTime model's max alive time from last contact
     */
    public ModelManager (String path, String tag,String k, int maxAliveTime)  throws Exception{
        try {
            ModelManagerInit(path,tag,k,maxAliveTime);
        }catch (Exception e) {
            throw e;
        }
    }

    /**
     * ModelManager init func
     * @param path path
     * @param tag tag
     * @param k key
     * @param maxAliveTime maxAliveTime
     */
    private void ModelManagerInit (String path, String tag, String k, int maxAliveTime) throws Exception{
        try {
            this.loadModelBundle(path, tag);
            this.aliveTime = 0;
            this.maxAliveTime = maxAliveTime;
            this.key = k;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * load a model
     * @param path path
     */
    private void loadModelBundle (String path, String tag) {
            this.modelBundle = SavedModelBundle.load(path, tag);
    }

    /**
     * get model bundle
     * @return {@link SavedModelBundle}
     */
    public SavedModelBundle getModelBundle() {
        return this.modelBundle;
    }


    /**
     * test model
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

    /**
     * if alive time is bigger than max alive time, that is timeout, need to be GC
     * @return true or false
     */
    public boolean isTimeout () {
        return this.aliveTime > this.maxAliveTime;
    }

    /**
     * if alive time is bigger than max alive time(set by user), that is timeout, need to be GC
     * @return true or false
     */
    public boolean isTimeout (int time) {
        return this.aliveTime > time;
    }
    /**
     * increase the alive time by 1
     * @return alive time
     */
    public int aliveTimeInc () {
        this.aliveTime ++;
        return this.aliveTime;
    }

    /**
     * increase the alive time by time
     * @param time increase time
     * @return alive time
     */
    public int aliveTimeInc (int time) {
        this.aliveTime += time;
        return this.aliveTime;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public void setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
    }


    public int getMaxAliveTime() {
        return maxAliveTime;
    }
    public void setMaxAliveTime(int maxAliveTime) {
        this.maxAliveTime = maxAliveTime;
    }

    /**
     * model manager's key
     * @return String key
     */
    public String getKey() {
        return key;
    }

    /**
     * model manager's key
     * @param key key
     */
    private void setKey(String key) {
        this.key = key;
    }
}
