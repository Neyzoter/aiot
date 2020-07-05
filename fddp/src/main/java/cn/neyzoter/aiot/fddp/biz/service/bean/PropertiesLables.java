package cn.neyzoter.aiot.fddp.biz.service.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * properties lables
 * @author Neyzoter Song
 * @date 2020-2-25
 */
public class PropertiesLables {
    /**
     * properties path
     */
    // TODO
//    @Value("${fddp.properties.path}")
    public static final String PROPERTIES_PATH = "/home/scc/code/java/aiot/fddp/fddp.properties";

    /**
     * influxdb host
     */
    public static final String INFLUXDB_HOST = "influxdb.host";
    /**
     * influxdb post
     */
    public static final String INFLUXDB_PORT = "influxdb.port";
    /**
     * influxdb org
     */
    public static final String INFLUXDB_ORG = "influxdb.org";
    /**
     * influxdb bucket
     */
    public static final String INFLUXDB_BUCKET = "influxdb.bucket";
    /**
     * influxdb precision
     */
    public static final String INFLUXDB_PRECISION = "influxdb.precision";
    /**
     * influxdb token
     */
    public static final String INFLUXDB_TOKEN = "influxdb.token";

    /**
     * kafka bootstrap server
     */
    public static final String KAFKA_BOOTSTRAP_SERVER = "kafka.bootstrap-server";
    /**
     * spark master
     */
    public static final String SPARK_MASTER = "spark.master";

    /**
     * properties update period
     */
    public static final String PROPERTIES_UPDATE_PERIOD = "properties.update-period";
    /**
     * properties update unit
     */
    public static final String PROPERTIES_UPDATE_UNIT = "properties.update-unit";

    /**
     * data bound path
     */
    public static final String DATA_BOUND_PATH = "data.bound.path";
    /**
     * matrix max step<br/>
     * matrix is (max-step,feature-num,feature-num,win-num)
     */
    public static final String DATA_MATRIX_STEP = "data.matrix.max-step";
    /**
     * which step to be used to compute loss 1-DATA_MATRIX_STEP, not start from 0
     */
    public static final String DATA_MATRIX_STEP_TO_COMPUTE_LOSS = "data.matrix.step-to-compute-loss";
    /**
     * matrix win num<br/>
     * matrix is (max-step,feature-num,feature-num,win-num)
     */
    public static final String DATA_MATRIX_WIN_NUM = "data.matrix.win-num";
    /**
     * matrix win
     */
    public static final String DATA_MATRIX_WIN = "data.matrix.win";
    /**
     * matrix feature num<br/>
     * matrix is (max-step,feature-num,feature-num,win-num)
     */
    public static final String DATA_MATRIX_FEATURE_NUM = "data.matrix.feature-num";

    /**
     * data trans to matrix with gap-time
     */
    public static final String DATA_MATRIX_GAP_NUM = "data.matrix.gat-time";


}
