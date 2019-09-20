package cn.neyzoter.aiot.uop.biz.service.auth.impl;

import cn.neyzoter.aiot.dal.dao.user.UserDao;
import cn.neyzoter.aiot.dal.domain.user.User;
import cn.neyzoter.aiot.uop.biz.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * auth service implement
 * @author Neyzoter Song
 * @date 2019/9/20
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findUserByName(String userName){
        return userDao.findByName(userName);
    }
}
