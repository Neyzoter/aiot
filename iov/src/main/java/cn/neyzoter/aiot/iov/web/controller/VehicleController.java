package cn.neyzoter.aiot.iov.web.controller;


import cn.neyzoter.aiot.iov.biz.domain.vehicle.Vehicle;
import org.springframework.web.bind.annotation.*;


/**
 * Vehicle-relative controller
 * @author Neyzoter Song
 * @date 2019/9/7
 */
@RestController
public class VehicleController {
    private final String apiPrefix = "/iov/api/vehicle";

    /**
     * http test( brower visit http://localhost:[port]/iov/api/vehicle/test)
     * @return {@link String}
     */
    @RequestMapping(value = apiPrefix+"/test", method = RequestMethod.GET)
    public String test() {
        return "OK";
    }

    /**
     * get vehicle data
     * @param id
     * @param speed
     * @param ecuMaxTemp
     * @return
     */
    @RequestMapping(value = apiPrefix+"/sendData", method = RequestMethod.POST)
    public Object sendData(@RequestBody long id,
                           @RequestBody int speed,
                           @RequestBody int ecuMaxTemp) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.getData().setSpeed(speed);
        vehicle.getData().setEcuMaxTemp(ecuMaxTemp);

        //dosomething


        return vehicle.toString();
    }
}
