package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Tf Model Manager
 * @author Neyzoter Song
 * @date 2020-3-11
 */
public class TestTfModelManager {

    public static final Logger logger = LoggerFactory.getLogger(TestTfModelManager.class);
    private TfModelManager tfModelManager;

    /**
     * Test TfModelManager Build
     */
    public TestTfModelManager () {
        try {
            tfModelManager = new TfModelManager("/home/scc/code/java/aiot/common/tf_model/nescar/","mytag", "mazida",100);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Test
    public void TestUpdateMaxRtData () {
        try {
            logger.info("Test Max Rt Data");
            tfModelManager.updateMaxRtData();
            logger.info(tfModelManager.getMaxRtData().toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }
    @Test
    public void TestUpdateMinRtData () {
        logger.info("Test Min Rt Data");
        try {
            tfModelManager.updateMinRtData();
            logger.info(tfModelManager.getMinRtData().toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
