package cn.neyzoter.aiot.fddp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * fault detection and diagnosis platform
 * @author Neyzoter Song
 * @date 2020-2-10
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FddpApplication {
    private final static Logger logger = LoggerFactory.getLogger(FddpApplication.class);
    public static void main(String[] args){
        logger.info("Fddp Application Start...");
        SpringApplication.run(FddpApplication.class, args);
    }
}
