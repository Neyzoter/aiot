package cn.neyzoter.aiot.fddp.biz.service.bean;

/**
 * properties lables
 * @author Neyzoter Song
 * @date 2020-2-25
 */
public class PropertiesLables {
    /**
     * properties path
     */
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
     * tensorflow model path
     */
    public static final String TENSORFLOW_MODEL_PATH = "tensorflow.model.path";
    /**
     * tensorflow model max alive time
     */
    public static final String TENSORFLOW_MODEL_MAX_ALIVE_TIME = "tensorflow.model.max-alive-time";
    /**
     * scheduled executor running interval , the unit also can be set.
     */
    public static final String TF_MODEL_CHECK_PERIOD = "tensorflow.model.check-period";
    /**
     * scheduled executor running unit
     */
    public static final String TF_MODEL_CHECK_UNIT = "tensorflow.model.check-unit";
    /**
     * matrix max step<br/>
     * matrix is (max-step,feature-num,feature-num,win-num)
     */
    public static final String DATA_MATRIX_STEP = "data.matrix.max-step";
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
