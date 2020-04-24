package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Tensorflow Model Alive check
 * @author Neyzoter Song
 * @date 2020-3-7
 */
@Setter
@Getter
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.tensorflow,cn.neyzoter.aiot.fddp.biz.service.properties")
@Component
public class TfModelAliveChecker implements Runnable{
    public static final Logger logger = LoggerFactory.getLogger(TfModelAliveChecker.class);
    private VehicleModelTable vehicleModelTable;
    private PropertiesManager propertiesUtil;

    @Autowired
    public TfModelAliveChecker (VehicleModelTable vehicleModelTable, PropertiesManager p) {
        this.vehicleModelTable = vehicleModelTable;
        this.propertiesUtil = p;
    }
    @Override
    public void run () {
        // check all manager is alive, if not, rm it
        List<String> rmList = vehicleModelTable.aliveIncCheck();
        for (String vtype : rmList) {
            logger.info(String.format("  %s ModelManager has been timeout", vtype));
        }
    }

}
