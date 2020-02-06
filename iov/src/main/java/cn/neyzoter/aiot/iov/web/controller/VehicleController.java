package cn.neyzoter.aiot.iov.web.controller;

import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
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
public class VehicleController {
    private final static Logger logger = LoggerFactory.getLogger(VehicleController.class);
    private final String apiPrefix = "/iov/api/vehicle";
    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * http test( brower visit http://localhost:[port]/iov/api/vehicle/test)
     * @return {@link String}
     */
    @RequestMapping(value = apiPrefix+"/test", method = RequestMethod.GET)
    public String test(@RequestParam(value = "vehicle_id",required = true) int vehicle_id) {
        logger.info(String.format("vehicle id = %d", vehicle_id));
        return "OK";
    }


    /**
     * server get vehicle data
     * @param @RequestBody VehicleHttpPack
     * @param @Request
     * @return {@link String}
     */
    /**
     * server get vehicle data
     * @param vehicle_id vehicle id
     * @param data_type data type
     * @param vehicleHttpPack VehicleHttpPack
     * @return
     */
    @RequestMapping(value = apiPrefix+"/sendData", method = RequestMethod.POST)
    public Object sendData(@RequestParam(value = "vehicle_id",required = true) String vehicle_id,
                           @RequestParam(value = "data_type",required = true) String data_type,
                           @RequestBody VehicleHttpPack vehicleHttpPack) {  //convert serialization
        //TODO
        // if msg is safe(equals to summary of information in ) then
        // md5 + salt
        String infoSumm = "123";
        if(vehicleHttpPack.getSign().equals(infoSumm)){
            try{
                // get the partition id
                int partition = PartitionAllocator.allocateByRemainder(vehicle_id.hashCode(), kafkaTemplate.partitionsFor("VehicleHttpPack").size());
                // send to kafka-VehicleHttpPack
                // key determines the partition
                kafkaTemplate.send("VehicleHttpPack" , partition ,data_type ,vehicleHttpPack);
            } catch (Exception e) {
                logger.error("",e);
            }

        }
        return "OK";
    }

}