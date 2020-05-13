//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.tensorflow;

import java.io.Serializable;
import java.util.Iterator;

public final class Graph implements ExecutionEnvironment, AutoCloseable, Serializable {
    private static final long serialVersionUID = -4185208707366383639L;
    private final Object nativeHandleLock = new Object();
    private long nativeHandle;
    private int refcount = 0;

    public Graph() {
        this.nativeHandle = allocate();
    }

    Graph(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    @Override
    public void close() {
        synchronized(this.nativeHandleLock) {
            if (this.nativeHandle != 0L) {
                while(this.refcount > 0) {
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

    public GraphOperation operation(String name) {
        synchronized(this.nativeHandleLock) {
            long oph = operation(this.nativeHandle, name);
            return oph == 0L ? null : new GraphOperation(this, oph);
        }
    }

    public Iterator<Operation> operations() {
        return new Graph.OperationIterator(this);
    }

    @Override
    public GraphOperationBuilder opBuilder(String type, String name) {
        return new GraphOperationBuilder(this, type, name);
    }

    public void importGraphDef(byte[] graphDef) throws IllegalArgumentException {
        this.importGraphDef(graphDef, "");
    }

    public void importGraphDef(byte[] graphDef, String prefix) throws IllegalArgumentException {
        if (graphDef != null && prefix != null) {
            synchronized(this.nativeHandleLock) {
                importGraphDef(this.nativeHandle, graphDef, prefix);
            }
        } else {
            throw new IllegalArgumentException("graphDef and prefix cannot be null");
        }
    }

    public byte[] toGraphDef() {
        synchronized(this.nativeHandleLock) {
            return toGraphDef(this.nativeHandle);
        }
    }

    public Output<?>[] addGradients(String prefix, Output<?>[] y, Output<?>[] x, Output<?>[] dx) {
        Output<?>[] dy = new Output[x.length];
        long[] yHandles = new long[y.length];
        int[] yIndices = new int[y.length];
        long[] xHandles = new long[x.length];
        int[] xIndices = new int[x.length];
        long[] dxHandles = null;
        int[] dxIndices = null;
        Graph.Reference ref = this.ref();
        Throwable var13 = null;

        try {
            int i;
            for(i = 0; i < y.length; ++i) {
                yHandles[i] = y[i].getUnsafeNativeHandle();
                yIndices[i] = y[i].index();
            }

            for(i = 0; i < x.length; ++i) {
                xHandles[i] = x[i].getUnsafeNativeHandle();
                xIndices[i] = x[i].index();
            }

            if (dx != null && dx.length > 0) {
                dxHandles = new long[dx.length];
                dxIndices = new int[dx.length];

                for(i = 0; i < dx.length; ++i) {
                    dxHandles[i] = dx[i].getUnsafeNativeHandle();
                    dxIndices[i] = dx[i].index();
                }
            }

            long[] dyHandlesAndIndices = addGradients(ref.nativeHandle(), prefix, yHandles, yIndices, xHandles, xIndices, dxHandles, dxIndices);
            int ndy = dyHandlesAndIndices.length >> 1;
            if (ndy != dy.length) {
                throw new IllegalStateException(ndy + " gradients were added to the graph when " + dy.length + " were expected");
            }

            i = 0;

            for(int j = ndy; i < ndy; ++j) {
                GraphOperation op = new GraphOperation(this, dyHandlesAndIndices[i]);
                dy[i] = new Output(op, (int)dyHandlesAndIndices[j]);
                ++i;
            }
        } catch (Throwable var26) {
            var13 = var26;
            throw var26;
        } finally {
            if (ref != null) {
                if (var13 != null) {
                    try {
                        ref.close();
                    } catch (Throwable var25) {
                        var13.addSuppressed(var25);
                    }
                } else {
                    ref.close();
                }
            }

        }

        return dy;
    }

    public Output<?>[] addGradients(Output<?> y, Output<?>[] x) {
        return this.addGradients((String)null, new Output[]{y}, x, (Output[])null);
    }

    private static long[] buildSubgraph(Graph.WhileSubgraphBuilder subgraphBuilder, long subgraphHandle, long[] inputHandles, int[] inputIndices, long[] outputHandles, int[] outputIndices) {
        Graph subgraph = new Graph(subgraphHandle);
        int ninputs = inputHandles.length;
        int noutputs = outputHandles.length;
        Output<?>[] inputs = new Output[ninputs];
        Output<?>[] outputs = new Output[noutputs];
        long[] outputHandlesAndIndices = new long[noutputs * 2];
        synchronized(subgraph.nativeHandleLock) {
            Graph.Reference ref = subgraph.ref();
            Throwable var15 = null;

            try {
                int i;
                GraphOperation op;
                for(i = 0; i < ninputs; ++i) {
                    op = new GraphOperation(subgraph, inputHandles[i]);
                    inputs[i] = op.output(inputIndices[i]);
                }

                for(i = 0; i < noutputs; ++i) {
                    op = new GraphOperation(subgraph, outputHandles[i]);
                    outputs[i] = op.output(outputIndices[i]);
                }

                subgraphBuilder.buildSubgraph(subgraph, inputs, outputs);
                i = 0;

                for(int j = noutputs; i < noutputs; ++j) {
                    outputHandlesAndIndices[i] = outputs[i].getUnsafeNativeHandle();
                    outputHandlesAndIndices[j] = (long)outputs[i].index();
                    ++i;
                }
            } catch (Throwable var27) {
                var15 = var27;
                throw var27;
            } finally {
                if (ref != null) {
                    if (var15 != null) {
                        try {
                            ref.close();
                        } catch (Throwable var26) {
                            var15.addSuppressed(var26);
                        }
                    } else {
                        ref.close();
                    }
                }

            }

            return outputHandlesAndIndices;
        }
    }

    public Output<?>[] whileLoop(Output<?>[] inputs, Graph.WhileSubgraphBuilder cgBuilder, Graph.WhileSubgraphBuilder bgBuilder, String name) {
        int ninputs = inputs.length;
        long[] inputHandles = new long[ninputs];
        int[] inputIndices = new int[ninputs];
        Output<?>[] outputs = new Output[ninputs];
        synchronized(this.nativeHandleLock) {
            Graph.Reference ref = this.ref();
            Throwable var11 = null;

            try {
                for(int i = 0; i < ninputs; ++i) {
                    inputHandles[i] = inputs[i].getUnsafeNativeHandle();
                    inputIndices[i] = inputs[i].index();
                }

                long[] outputHandlesAndIndices = whileLoop(this.nativeHandle, inputHandles, inputIndices, name, cgBuilder, bgBuilder);
                int i = 0;

                for(int j = ninputs; i < ninputs; ++j) {
                    Operation op = new GraphOperation(this, outputHandlesAndIndices[i]);
                    outputs[i] = op.output((int)outputHandlesAndIndices[j]);
                    ++i;
                }
            } catch (Throwable var25) {
                var11 = var25;
                throw var25;
            } finally {
                if (ref != null) {
                    if (var11 != null) {
                        try {
                            ref.close();
                        } catch (Throwable var24) {
                            var11.addSuppressed(var24);
                        }
                    } else {
                        ref.close();
                    }
                }

            }

            return outputs;
        }
    }

    Graph.Reference ref() {
        return new Graph.Reference();
    }

    private static native long allocate();

    private static native void delete(long var0);

    private static native long operation(long var0, String var2);

    private static native long[] nextOperation(long var0, int var2);

    private static native void importGraphDef(long var0, byte[] var2, String var3) throws IllegalArgumentException;

    private static native byte[] toGraphDef(long var0);

    private static native long[] addGradients(long var0, String var2, long[] var3, int[] var4, long[] var5, int[] var6, long[] var7, int[] var8);

    private static native long[] whileLoop(long var0, long[] var2, int[] var3, String var4, Graph.WhileSubgraphBuilder var5, Graph.WhileSubgraphBuilder var6);

    static {
        TensorFlow.init();
    }

    private static final class OperationIterator implements Iterator<Operation> {
        private final Graph graph;
        private Operation operation;
        private int position;

        OperationIterator(Graph g) {
            this.graph = g;
            this.operation = null;
            this.position = 0;
            this.advance();
        }

        private final void advance() {
            Graph.Reference reference = this.graph.ref();
            this.operation = null;

            try {
                long[] nativeReturn = Graph.nextOperation(reference.nativeHandle(), this.position);
                if (nativeReturn != null && nativeReturn[0] != 0L) {
                    this.operation = new GraphOperation(this.graph, nativeReturn[0]);
                    this.position = (int)nativeReturn[1];
                }
            } finally {
                reference.close();
            }

        }

        @Override
        public boolean hasNext() {
            return this.operation != null;
        }

        @Override
        public Operation next() {
            Operation rhett = this.operation;
            this.advance();
            return rhett;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() is unsupported.");
        }
    }

    class Reference implements AutoCloseable {
        private boolean active;

        private Reference() {
            synchronized(Graph.this.nativeHandleLock) {
                this.active = Graph.this.nativeHandle != 0L;
                if (!this.active) {
                    throw new IllegalStateException("close() has been called on the Graph");
                } else {
                    this.active = true;
                    Graph.this.refcount++;
                }
            }
        }

        @Override
        public void close() {
            synchronized(Graph.this.nativeHandleLock) {
                if (this.active) {
                    this.active = false;
                    if (--Graph.this.refcount == 0) {
                        Graph.this.nativeHandleLock.notifyAll();
                    }

                }
            }
        }

        public long nativeHandle() {
            synchronized(Graph.this.nativeHandleLock) {
                return this.active ? Graph.this.nativeHandle : 0L;
            }
        }
    }

    public interface WhileSubgraphBuilder {
        void buildSubgraph(Graph var1, Output<?>[] var2, Output<?>[] var3);
    }
}
