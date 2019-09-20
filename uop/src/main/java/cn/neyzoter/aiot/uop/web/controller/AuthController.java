package cn.neyzoter.aiot.uop.web.controller;

import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import cn.neyzoter.aiot.uop.biz.service.auth.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * auth controller
 * @author Neyzoter Song
 * @date 2019/9/20
 */
@RestController
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final String apiPrefix = "/uop/api/auth";

    @Autowired
    AuthServiceImpl authServiceImpl;

    @RequestMapping(value = apiPrefix+"/auth", method = RequestMethod.POST)
    public Object auth(@RequestBody String str){
        logger.info("auth : "+str);
        return authServiceImpl.findUserByName(str);
    }
}
