package cn.neyzoter.aiot.iov.web.controller;

import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.iov.biz.service.kafka.constant.KafkaTopic;
import cn.neyzoter.aiot.iov.biz.service.kafka.util.PartitionAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


/**
 * Vehicle-relative controller
 * @author Neyzoter Song
 * @date 2019/9/7
 */
@RestController
@RequestMapping(path = "/iov/api/runtime-data")
public class VehicleController {
    private final static Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public static long startime;
    public static long endtime;
    public static boolean start = false;
    public static long num = 0;
    public static boolean stopprint = false;


    /**
     * http test( brower visit http://localhost:[port]/iov/api/runtime-data/test)
     * @return {@link String}
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestBody VehicleHttpPack vehicleHttpPack) {
        try {
            logger.info(vehicleHttpPack.toString());
            return IovHttpRtn.OK;
        } catch (Exception e) {
            logger.error("", e);
            return IovHttpRtn.ERROR;
        }
    }

    /**
     * server get vehicle data
     * @param vehicleHttpPack json(String type) data
     * @return
     */
    @RequestMapping(value = "/vehicleHttpPack", method = RequestMethod.POST)
    public Object sendData(@RequestBody VehicleHttpPack vehicleHttpPack) {
        if (!start) {
            start = true;
            startime = System.currentTimeMillis();
            endtime = startime + 1000 * 10;
        } else {
            if (endtime < System.currentTimeMillis() && !stopprint) {
                logger.info("Package num : " + num + "\nRPS : " + num / 10);
                stopprint = true;
            }
        }
        num ++;
        try {
            int partition = PartitionAllocator.allocateByRemainder(Math.abs(vehicleHttpPack.getVehicle().getVtype().hashCode()), kafkaTemplate.partitionsFor(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME).size());
            kafkaTemplate.send(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME , partition ,vehicleHttpPack.getVehicle().getVid() ,vehicleHttpPack);
            // TODO
            // 返回最近一次的故障诊断结果
            return IovHttpRtn.OK;
        } catch (Exception e) {
            logger.error("", e);
            return IovHttpRtn.ERROR;
        }
    }

}