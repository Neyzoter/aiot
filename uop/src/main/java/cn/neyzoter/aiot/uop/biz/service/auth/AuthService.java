package cn.neyzoter.aiot.uop.biz.service.auth;


import cn.neyzoter.aiot.dal.domain.user.User;

import java.util.List;

/**
 * auth service
 * @author Neyzoter Song
 * @date 2019/9/20
 *
 */
public interface AuthService {
    List<User> findUserByName(String userName);
}
