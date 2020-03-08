package cn.neyzoter.aiot.fddp.biz.service.bean;

/**
 * properties lables
 * @author Neyzoter Song
 * @date 2020-2-25
 */
public class PropertiesLables {
    public static final String PROPERTIES_PATH = "/home/scc/code/java/aiot/fddp/fddp.properties";

    public static final String INFLUXDB_HOST = "influxdb.host";
    public static final String INFLUXDB_PORT = "influxdb.port";
    public static final String INFLUXDB_ORG = "influxdb.org";
    public static final String INFLUXDB_BUCKET = "influxdb.bucket";
    public static final String INFLUXDB_PRECISION = "influxdb.precision";
    public static final String INFLUXDB_TOKEN = "influxdb.token";

    public static final String KAFKA_BOOTSTRAP_SERVER = "kafka.bootstrap-server";

    public static final String SPARK_MASTER = "spark.master";

    // scheduled executor running interval , the unit also can be set.
    public static final String THREADPOOL_SCHEDULED_EXECUTOR_PERIOD = "threadpool.scheduled-executor.period";
    public static final String THREADPOOL_SCHEDULED_EXECUTOR_UNIT = "threadpool.scheduled-executor.unit";

    // properties update period
    public static final String PROPERTIES_UPDATE_PERIOD = "properties.update-period";
    public static final String PROPERTIES_UPDATE_UNIT = "properties.update-unit";

    public static final String TENSORFLOW_MODEL_PATH = "tensorflow.model.path";


}
