package cn.neyzoter.aiot.common.tensorflow;

import org.apache.commons.io.IOUtils;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.FileInputStream;
import java.nio.FloatBuffer;

/**
 * Tensorflow Model Manager
 * @author Neyzoter Song
 * @date 2020-01-05
 */
public class ModelManager {

    private Session session;

    /**
     * 模型管理构造函数
     * @param path 模型的路径
     */
    public ModelManager (String path){
        System.out.println(String.format("Loading model: %s", path));
        try {
            this.setSession(path);
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    /**
     * 读取一个session
     * @param path
     */
    public void setSession (String path) throws Exception{
        try (Graph graph = new Graph()) {
            byte[] graphBytes = IOUtils.toByteArray(new FileInputStream(path));
            graph.importGraphDef(graphBytes);
            session = new Session(graph);
            long[] shape = new long[]{5, 32, 32, 3};
            float[] data = new float[]{0};
            Tensor data_input = Tensor.create(shape, FloatBuffer.wrap(data));
            System.out.println(session.runner().feed("Placeholder",data_input).fetch("zeros").run());

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取session
     * @return
     */
    public Session getSession() {
        return this.session;
    }
}
