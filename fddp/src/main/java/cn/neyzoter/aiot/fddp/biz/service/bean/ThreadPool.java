package cn.neyzoter.aiot.fddp.biz.service.bean;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
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
@ComponentScan("cn.neyzoter.aiot.common.util")
@Component
public class ThreadPool {
    public static ScheduledExecutorService scheduledThreadPool;

    private PropertiesUtil propertiesUtil;
    @Autowired
    ThreadPool (PropertiesUtil p) {
        propertiesUtil = p;
        scheduledThreadPool = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("scheduled-pool-%d").daemon(true).build());
        scheduledThreadPool.scheduleAtFixedRate(new TfModelAliveCheck(),0,
                this.getPeriod(),this.getTimeUnit());
    }

    /**
     * get time unit from properties file
     * @return {@link TimeUnit}
     */
    private TimeUnit getTimeUnit () {
        String unit = propertiesUtil.readValue(PropertiesLables.THREADPOOL_SCHEDULED_EXECUTOR_UNIT);
        switch (unit) {
            case PropertiesValueRange.THREADPOOL_SCHEDULED_EXECUTOR_UNIT_HOUR:
                return TimeUnit.HOURS;
            case PropertiesValueRange.THREADPOOL_SCHEDULED_EXECUTOR_UNIT_SECOND:
                return TimeUnit.SECONDS;
            case PropertiesValueRange.THREADPOOL_SCHEDULED_EXECUTOR_UNIT_MINUTTE:
            default:
                return TimeUnit.MINUTES;
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
