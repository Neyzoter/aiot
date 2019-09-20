package cn.neyzoter.aiot.uop.biz.service.auth.impl;

import cn.neyzoter.aiot.dal.dao.user.UserDao;
import cn.neyzoter.aiot.dal.domain.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * test authservice implement
 * @author Neyzote Song
 * @date 2019/9/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAuthServiceImpl {
    private final static Logger logger = LoggerFactory.getLogger(TestAuthServiceImpl.class);
    @Autowired
    AuthServiceImpl authServiceImpl;

    @Test
    public void testUserDao(){
        List<User> userList = authServiceImpl.findUserByName("song");
        logger.info("Found : "+userList.toString());
    }

}
