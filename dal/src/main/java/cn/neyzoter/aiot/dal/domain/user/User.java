package cn.neyzoter.aiot.dal.domain.user;

import java.io.Serializable;

/**
 * User
 * @author Neyzoter Song
 * @date 2019/9/10
 */
public class User implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * main key
     */
    private String userName;

    /**
     * password
     */
    private String password;

    /**
     * get user name
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * set user name
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * get password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        String str = "User = {"+
                "userName:"+this.userName+"  "+
                "password:"+this.password+
                "}";
        return str;
    }
}
