package cn.neyzoter.aiot.fddp.biz.service.spark.exception;


/**
 * Illegal {@link cn.neyzoter.aiot.dal.domain.vehicle.VehicleHttpPack} Time
 * @author Neyzoter Song
 * @date 2020-2-19
 */
public class IllVehicleHttpPackTime extends Exception {
    public static final String ILL_DAY = "Day";
    public static final String ILL_MONTH = "Month";
    public static final String ILL_YEAR = "Year";
    String detail;
    public IllVehicleHttpPackTime(String timeUnit) {
        detail = timeUnit;
    }
    public IllVehicleHttpPackTime() {
        this("Year or Month or Day");
    }

    @Override
    public String toString () {
        String exceptionMsg = String.format("Illegal VehicleHttpPack Time : %s", this.detail);
        return exceptionMsg;
    }
}
