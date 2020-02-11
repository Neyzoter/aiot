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



    /**
     * http test( brower visit http://localhost:[port]/iov/api/runtime-data/test)
     * @return {@link String}
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(@RequestParam(value = "vehicle_id",required = true) int vehicle_id) {
        logger.info(String.format("vehicle id = %d", vehicle_id));
        return IovHttpRtn.OK;
    }

    /**
     * server get vehicle data
     * @param vid vehicle id
     * @param dtype data type
     * @param vehicleHttpPack json(String type) data
     * @return
     */
    @RequestMapping(value = "/vehicleHttpPack", method = RequestMethod.POST)
    public Object sendData(@RequestParam(value = "vid",required = true) String vid,
                           @RequestParam(value = "dtype",required = true) String dtype,
                           @RequestBody VehicleHttpPack vehicleHttpPack) {
        try {
            int partition = PartitionAllocator.allocateByRemainder(vid.hashCode(), kafkaTemplate.partitionsFor(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME).size());
            kafkaTemplate.send(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME , partition ,dtype ,vehicleHttpPack);
            // TODO
            // 返回最近一次的故障诊断结果
            return IovHttpRtn.OK;
        } catch (Exception e) {
            logger.error("", e);
            return IovHttpRtn.ERROR;
        }
    }

}