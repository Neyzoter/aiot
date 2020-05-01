package cn.neyzoter.aiot.fddp.biz.service.spark.algo;

import cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataPreProcess {
    public static final Logger logger = LoggerFactory.getLogger(TestDataPreProcess.class);

    @Test
    public void testTrans2Matrix () {
        try {
            Double[][][][] maxtrix = DataPreProcess.trans2Matrix(new VehicleHttpPack());
            logger.info(String.format("Matrix[%d][%d][%d][%d]", maxtrix.length, maxtrix[0].length, maxtrix[0][0].length, maxtrix[0][0][0].length));
        } catch (Exception e) {
            logger.error("", e);
        }

    }
}
