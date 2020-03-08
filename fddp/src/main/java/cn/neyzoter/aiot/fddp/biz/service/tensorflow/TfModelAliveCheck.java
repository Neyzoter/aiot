package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Tensorflow Model Alive check
 * @author Neyzoter Song
 * @date 2020-3-7
 */
@Setter
@Getter
public class TfModelAliveCheck implements Runnable{
    public static final Logger logger = LoggerFactory.getLogger(TfModelAliveCheck.class);
    private VehicleModelTable vehicleModelTable;

    public TfModelAliveCheck () {
        vehicleModelTable = new VehicleModelTable();
    }
    @Override
    public void run () {
        // check all manager is alive, if not, rm it
        List<String> rmList = vehicleModelTable.aliveCheckUpdate();
        for (String vtype : rmList) {
            logger.info(String.format("GC model of %s", vtype));
        }
    }
}
