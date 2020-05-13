//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.tensorflow;

import java.io.Serializable;

/**
 * Model Bundle
 * @author Charles Song
 * @date 2020-5-13
 */
public class SavedModelBundle implements AutoCloseable, Serializable {
    private static final long serialVersionUID = 3609416542138421700L;
    private final Graph graph;
    private final Session session;
    private final byte[] metaGraphDef;

    public static SavedModelBundle load(String exportDir, String... tags) {
        return loader(exportDir).withTags(tags).load();
    }

    public static SavedModelBundle.Loader loader(String exportDir) {
        return new SavedModelBundle.Loader(exportDir);
    }

    public byte[] metaGraphDef() {
        return this.metaGraphDef;
    }

    public Graph graph() {
        return this.graph;
    }

    public Session session() {
        return this.session;
    }

    @Override
    public void close() {
        this.session.close();
        this.graph.close();
    }

    private SavedModelBundle(Graph graph, Session session, byte[] metaGraphDef) {
        this.graph = graph;
        this.session = session;
        this.metaGraphDef = metaGraphDef;
    }

    private static SavedModelBundle fromHandle(long graphHandle, long sessionHandle, byte[] metaGraphDef) {
        Graph graph = new Graph(graphHandle);
        Session session = new Session(graph, sessionHandle);
        return new SavedModelBundle(graph, session, metaGraphDef);
    }

    private static native SavedModelBundle load(String var0, String[] var1, byte[] var2, byte[] var3);

    static {
        TensorFlow.init();
    }

    public static final class Loader {
        private String exportDir;
        private String[] tags;
        private byte[] configProto;
        private byte[] runOptions;

        public SavedModelBundle load() {
            return SavedModelBundle.load(this.exportDir, this.tags, this.configProto, this.runOptions);
        }

        public SavedModelBundle.Loader withRunOptions(byte[] options) {
            this.runOptions = options;
            return this;
        }

        public SavedModelBundle.Loader withConfigProto(byte[] configProto) {
            this.configProto = configProto;
            return this;
        }

        public SavedModelBundle.Loader withTags(String... tags) {
            this.tags = tags;
            return this;
        }

        private Loader(String exportDir) {
            this.exportDir = null;
            this.tags = null;
            this.configProto = null;
            this.runOptions = null;
            this.exportDir = exportDir;
        }
    }
}
