package cn.neyzoter.aiot.fddp.biz.service.bean;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * beans
 * @author Neyzoter Song
 * @date 2020-2-25
 */
@Configuration
public class BeanConfig {
    @Bean(name = "propertiesUtil")
    public PropertiesUtil propertiesUtil() {
        return new PropertiesUtil(PropertiesLables.PROPERTIES_PATH);
    }
}
