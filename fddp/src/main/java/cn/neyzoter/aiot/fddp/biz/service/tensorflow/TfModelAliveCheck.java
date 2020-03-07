package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import lombok.Getter;
import lombok.Setter;

/**
 * Tensorflow Model Alive check
 * @author Neyzoter Song
 * @date 2020-3-7
 */
@Setter
@Getter
public class TfModelAliveCheck implements Runnable{

    private VehicleModelTable vehicleModelTable;

    public TfModelAliveCheck () {
        vehicleModelTable = new VehicleModelTable();
    }
    @Override
    public void run () {
        // check all manager is alive, if not, rm it
        vehicleModelTable.aliveCheckUpdate();
    }
}
