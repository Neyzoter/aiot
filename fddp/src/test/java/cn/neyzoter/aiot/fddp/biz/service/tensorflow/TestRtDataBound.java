package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * test RtDataBound
 * @author Charles Song
 * @date 2020-5-20
 */
public class TestRtDataBound {
    public static final Logger logger = LoggerFactory.getLogger(TestRtDataBound.class);

    private RtDataBound bound;

    public TestRtDataBound () {
        try {
            bound = new RtDataBound("/home/scc/code/java/aiot/common/tf_model/nescar/", "mazida",100);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Test
    public void TestUpdateMaxRtData () {
        try {
            logger.info("Test Max Rt Data");
            bound.updateMaxRtData();
            logger.info(bound.getMaxRtData().toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }
    @Test
    public void TestUpdateMinRtData () {
        logger.info("Test Min Rt Data");
        try {
            bound.updateMinRtData();
            logger.info(bound.getMinRtData().toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
