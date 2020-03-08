package cn.neyzoter.aiot.fddp.biz.service.bean;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.TfModelAliveCheck;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * my thread pool cooker
 * @author Neyzoter Song
 * @date 2020-3-7
 */
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.properties")
@Component
public class ThreadPool {
    public static ScheduledExecutorService scheduledThreadPool;

    private PropertiesManager propertiesUtil;
    @Autowired
    ThreadPool (PropertiesManager propertiesUtil) {
        this.propertiesUtil = propertiesUtil;
        scheduledThreadPool = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("scheduled-pool-%d").daemon(true).build());
        // alive tensorflow model check
        scheduledThreadPool.scheduleAtFixedRate(new TfModelAliveCheck(),0,
                this.getPeriod(),this.getTimeUnit());
        // properteis update
        scheduledThreadPool.scheduleAtFixedRate(this.propertiesUtil, 0,
                propertiesUtil.getPeriod(),
                propertiesUtil.getUpdatePeriodUnit());
    }

    /**
     * get time unit from properties file
     * @return {@link TimeUnit}
     */
    private TimeUnit getTimeUnit () {
        String unit = this.propertiesUtil.readValue(PropertiesLables.THREADPOOL_SCHEDULED_EXECUTOR_UNIT);
        switch (unit) {
            case PropertiesValueRange.UNIT_HOUR:
                return TimeUnit.HOURS;
            case PropertiesValueRange.UNIT_MINUTTE:
                return TimeUnit.MINUTES;
            case PropertiesValueRange.UNIT_SECOND:
            default:
                return TimeUnit.SECONDS;

        }
    }

    /**
     * get period
     * @return period
     */
    private long getPeriod () {
        String period = propertiesUtil.readValue(PropertiesLables.THREADPOOL_SCHEDULED_EXECUTOR_PERIOD);
        return Long.parseLong(period);
    }
}
