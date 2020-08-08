package cn.neyzoter.aiot.fddp.biz.service.properties;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * properties manager
 * @author Neyzoter Song
 * @date 2020-38
 */
@Component("propertiesManager")
public class PropertiesManager extends PropertiesUtil implements Runnable{
    public static final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);
    private static final long serialVersionUID = 3974266425966219580L;

    public PropertiesManager () {
        super(PropertiesLables.PROPERTIES_PATH);
    }
    @Override
    public void run () {
        this.updateProps();
        logger.info("Updated Properties");
    }

    /**
     * get properties update unit
     * @return {@link TimeUnit}
     */
    public TimeUnit getUpdatePeriodUnit () {
        String unit = this.readValue(PropertiesLables.PROPERTIES_UPDATE_UNIT);
        unit = unit.trim();
        switch (unit) {
            case PropertiesValueRange.UNIT_HOUR:
                return TimeUnit.HOURS;
            case PropertiesValueRange.UNIT_SECOND:
                return TimeUnit.SECONDS;
            case PropertiesValueRange.UNIT_MINUTTE:
                return TimeUnit.MINUTES;
            default:
                logger.warn("Period Unit Unrecognized， set as default MINUTES");
                return TimeUnit.MINUTES;
        }
    }

    /**
     * get properties udpate period
     * @return update period
     */
    public long getPeriod () {
        System.out.println(this.getProps().toString());
        String period = this.readValue(PropertiesLables.PROPERTIES_UPDATE_PERIOD);
        return Long.parseLong(period);
    }
}
