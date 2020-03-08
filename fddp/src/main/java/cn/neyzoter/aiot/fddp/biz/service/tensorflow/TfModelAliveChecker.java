package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.util.PropertiesUtil;
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
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.tensorflow")
@Component
public class TfModelAliveChecker implements Runnable{
    public static final Logger logger = LoggerFactory.getLogger(TfModelAliveChecker.class);
    private VehicleModelTable vehicleModelTable;
    private PropertiesUtil propertiesUtil;

    @Autowired
    public TfModelAliveChecker (VehicleModelTable vehicleModelTable, PropertiesUtil p) {
        this.vehicleModelTable = vehicleModelTable;
        this.propertiesUtil = p;
    }
    @Override
    public void run () {
        // check all manager is alive, if not, rm it
        List<String> rmList = vehicleModelTable.aliveIncCheck();
        for (String vtype : rmList) {
            logger.info(String.format("GC model of %s", vtype));
        }
    }

}
