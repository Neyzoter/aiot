package cn.neyzoter.aiot.iov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Iov Application
 * @author Neyzoter Song
 * @date 2019/9/7
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class IovApplication {
    private final static Logger logger = LoggerFactory.getLogger(IovApplication.class);
    public static void main(String[] args){
        logger.info("Iov Application Start...");
        SpringApplication.run(IovApplication.class, args);
    }
}
