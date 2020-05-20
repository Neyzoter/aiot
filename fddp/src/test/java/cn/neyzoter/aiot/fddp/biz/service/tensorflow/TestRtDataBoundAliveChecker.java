package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * test RtDataBoundAliveChecker
 */
public class TestRtDataBoundAliveChecker {
    public static final Logger logger = LoggerFactory.getLogger(TestRtDataBoundAliveChecker.class);
    private RtDataBoundTable table;
    private RtDataBoundAliveChecker checker;
    public static final String KEY = "mazida110";

    public TestRtDataBoundAliveChecker () {
        PropertiesManager p = new PropertiesManager();
        this.table = new RtDataBoundTable(p);
        this.table.loadPut(KEY, "/home/scc/code/java/aiot/common/tf_model/nescar/", table.getMaxAliveTime());
        checker = new RtDataBoundAliveChecker(this.table, p);
        logger.info("Inited OK!");
    }

    @Test
    public void testChecker () {
        TestRtDataBoundAliveChecker test = new TestRtDataBoundAliveChecker();
        int i = 10;
        ScheduledExecutorService scheduledThreadPool = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("scheduled-pool-%d").daemon(true).build());
        scheduledThreadPool.scheduleAtFixedRate(test.checker, 0, 1, test.table.getCheckTimeUnit());
        while (i-- > 0) {
            if (test.checker.getRtDataBoundTable().getRtDataBound(KEY) == null) {
                logger.info(KEY + "'s bound is removed");
                break;
            } else {
                logger.info(KEY + "'s bound is alive");
                logger.info(String.valueOf(test.checker.getRtDataBoundTable().size()));
                logger.info(test.checker.getRtDataBoundTable().getRtDataBound(KEY).getDeltaRtData().toString());
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
