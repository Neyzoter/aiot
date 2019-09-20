package cn.neyzoter.aiot.dal.dao.user;

import cn.neyzoter.aiot.dal.domain.user.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * user dall
 * @author Neyzoter Song
 * @date 2019/9/20
 */
public interface UserDao {
    /**
     * fine user
     * @param userName
     * @return
     */
    List<User> findByName(@Param("userName") String userName);
}
