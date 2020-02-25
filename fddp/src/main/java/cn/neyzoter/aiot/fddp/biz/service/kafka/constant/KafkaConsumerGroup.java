package cn.neyzoter.aiot.fddp.biz.service.kafka.constant;

/**
 * kafka consumer group
 * @author Neyzoter Song
 * @date 2020-2-8
 */
public class KafkaConsumerGroup {
    public final static String GROUP_SPARK_CONSUME_VEHICLE_HTTP_PACKET = "VehicleHttpPackageSparkConsumer";

    /**
     * post vhp to influxdb
     */
    public final static String GROUP_POST_VEHICLE_HTTP_PACKET = "VehicleHttpPackagePoster";

}
