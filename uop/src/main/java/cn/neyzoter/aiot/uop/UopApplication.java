package cn.neyzoter.aiot.uop;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User-oriented platform
 * @author Neyzoter Song
 * @date 2019/9/20
 */
@MapperScan({"cn.neyzoter.aiot.dal.dao.user"})
@SpringBootApplication
public class UopApplication {
    private final static Logger logger = LoggerFactory.getLogger(UopApplication.class);
    public static void main(String[] args){
        logger.info("Uop Application Start...");
        SpringApplication.run(UopApplication.class, args);
    }
}
