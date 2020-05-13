//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.tensorflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Session implements AutoCloseable, Serializable {
    private final Graph graph;
    private final org.tensorflow.Graph.Reference graphRef;
    private final Object nativeHandleLock;
    private long nativeHandle;
    private int numActiveRuns;

    public Session(Graph g) {
        this(g, (byte[])null);
    }

    public Session(Graph g, byte[] config) {
        this.nativeHandleLock = new Object();
        this.graph = g;
        org.tensorflow.Graph.Reference r = g.ref();

        try {
            this.nativeHandle = config == null ? allocate(r.nativeHandle()) : allocate2(r.nativeHandle(), (String)null, config);
            this.graphRef = g.ref();
        } finally {
            r.close();
        }

    }

    Session(Graph g, long nativeHandle) {
        this.nativeHandleLock = new Object();
        this.graph = g;
        this.nativeHandle = nativeHandle;
        this.graphRef = g.ref();
    }

    @Override
    public void close() {
        this.graphRef.close();
        synchronized(this.nativeHandleLock) {
            if (this.nativeHandle != 0L) {
                while(this.numActiveRuns > 0) {
                    try {
                        this.nativeHandleLock.wait();
                    } catch (InterruptedException var4) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                delete(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }

    public Session.Runner runner() {
        return new Session.Runner();
    }

    private static native long allocate(long var0);

    private static native long allocate2(long var0, String var2, byte[] var3);

    private static native void delete(long var0);

    private static native byte[] run(long var0, byte[] var2, long[] var3, long[] var4, int[] var5, long[] var6, int[] var7, long[] var8, boolean var9, long[] var10);

    public static final class Run {
        public List<Tensor<?>> outputs;
        public byte[] metadata;

        public Run() {
        }
    }

    public final class Runner {
        private ArrayList<Output<?>> inputs = new ArrayList();
        private ArrayList<Tensor<?>> inputTensors = new ArrayList();
        private ArrayList<Output<?>> outputs = new ArrayList();
        private ArrayList<GraphOperation> targets = new ArrayList();
        private byte[] runOptions = null;

        public Runner() {
        }

        public Session.Runner feed(String operation, Tensor<?> t) {
            return this.feed((Operand)this.parseOutput(operation), t);
        }

        public Session.Runner feed(String operation, int index, Tensor<?> t) {
            Operation op = this.operationByName(operation);
            if (op != null) {
                this.inputs.add(op.output(index));
                this.inputTensors.add(t);
            }

            return this;
        }

        public Session.Runner feed(Operand<?> operand, Tensor<?> t) {
            this.inputs.add(operand.asOutput());
            this.inputTensors.add(t);
            return this;
        }

        public Session.Runner fetch(String operation) {
            return this.fetch(this.parseOutput(operation));
        }

        public Session.Runner fetch(String operation, int index) {
            Operation op = this.operationByName(operation);
            if (op != null) {
                this.outputs.add(op.output(index));
            }

            return this;
        }

        public Session.Runner fetch(Output<?> output) {
            this.outputs.add(output);
            return this;
        }

        public Session.Runner fetch(Operand<?> operand) {
            return this.fetch(operand.asOutput());
        }

        public Session.Runner addTarget(String operation) {
            GraphOperation op = this.operationByName(operation);
            if (op != null) {
                this.targets.add(op);
            }

            return this;
        }

        public Session.Runner addTarget(Operation operation) {
            if (!(operation instanceof GraphOperation)) {
                throw new IllegalArgumentException("Operation of type " + operation.getClass().getName() + " is not supported in graph sessions");
            } else {
                this.targets.add((GraphOperation)operation);
                return this;
            }
        }

        public Session.Runner addTarget(Operand<?> operand) {
            return this.addTarget(operand.asOutput().op());
        }

        public Session.Runner setOptions(byte[] options) {
            this.runOptions = options;
            return this;
        }

        public List<Tensor<?>> run() {
            return this.runHelper(false).outputs;
        }

        public Session.Run runAndFetchMetadata() {
            return this.runHelper(true);
        }

        private Session.Run runHelper(boolean wantMetadata) {
            long[] inputTensorHandles = new long[this.inputTensors.size()];
            long[] inputOpHandles = new long[this.inputs.size()];
            int[] inputOpIndices = new int[this.inputs.size()];
            long[] outputOpHandles = new long[this.outputs.size()];
            int[] outputOpIndices = new int[this.outputs.size()];
            long[] targetOpHandles = new long[this.targets.size()];
            long[] outputTensorHandles = new long[this.outputs.size()];
            int idx = 0;

            Iterator var10;
            Tensor t;
            for(var10 = this.inputTensors.iterator(); var10.hasNext(); inputTensorHandles[idx++] = t.getNativeHandle()) {
                t = (Tensor)var10.next();
            }

            idx = 0;

            Output o;
            for(var10 = this.inputs.iterator(); var10.hasNext(); ++idx) {
                o = (Output)var10.next();
                inputOpHandles[idx] = o.getUnsafeNativeHandle();
                inputOpIndices[idx] = o.index();
            }

            idx = 0;

            for(var10 = this.outputs.iterator(); var10.hasNext(); ++idx) {
                o = (Output)var10.next();
                outputOpHandles[idx] = o.getUnsafeNativeHandle();
                outputOpIndices[idx] = o.index();
            }

            idx = 0;

            GraphOperation op;
            for(var10 = this.targets.iterator(); var10.hasNext(); targetOpHandles[idx++] = op.getUnsafeNativeHandle()) {
                op = (GraphOperation)var10.next();
            }

            Session.Runner.Reference runRef = new Session.Runner.Reference();
            t = null;

            byte[] metadata;
            try {
                metadata = Session.run(Session.this.nativeHandle, this.runOptions, inputTensorHandles, inputOpHandles, inputOpIndices, outputOpHandles, outputOpIndices, targetOpHandles, wantMetadata, outputTensorHandles);
            } finally {
                runRef.close();
            }

            ArrayList outputs = new ArrayList();
            long[] var13 = outputTensorHandles;
            int var14 = outputTensorHandles.length;

            for(int var15 = 0; var15 < var14; ++var15) {
                long h = var13[var15];

                try {
                    outputs.add(Tensor.fromHandle(h));
                } catch (Exception var24) {
                    Iterator var19 = outputs.iterator();

                    while(var19.hasNext()) {
                        Tensor<?> tx = (Tensor)var19.next();
                        tx.close();
                    }

                    outputs.clear();
                    throw var24;
                }
            }

            Session.Run ret = new Session.Run();
            ret.outputs = outputs;
            ret.metadata = metadata;
            return ret;
        }

        private GraphOperation operationByName(String opName) {
            GraphOperation op = Session.this.graph.operation(opName);
            if (op == null) {
                throw new IllegalArgumentException("No Operation named [" + opName + "] in the Graph");
            } else {
                return op;
            }
        }

        private Output<?> parseOutput(String opName) {
            int colon = opName.lastIndexOf(58);
            if (colon != -1 && colon != opName.length() - 1) {
                try {
                    String op = opName.substring(0, colon);
                    int index = Integer.parseInt(opName.substring(colon + 1));
                    return new Output(this.operationByName(op), index);
                } catch (NumberFormatException var5) {
                    return new Output(this.operationByName(opName), 0);
                }
            } else {
                return new Output(this.operationByName(opName), 0);
            }
        }

        private class Reference implements AutoCloseable {
            public Reference() {
                synchronized(Session.this.nativeHandleLock) {
                    if (Session.this.nativeHandle == 0L) {
                        throw new IllegalStateException("run() cannot be called on the Session after close()");
                    } else {
                        ++Session.this.numActiveRuns;
                    }
                }
            }

            @Override
            public void close() {
                synchronized(Session.this.nativeHandleLock) {
                    if (Session.this.nativeHandle != 0L) {
                        if (--Session.this.numActiveRuns == 0) {
                            Session.this.nativeHandleLock.notifyAll();
                        }

                    }
                }
            }
        }
    }
}
