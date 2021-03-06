package cn.neyzoter.aiot.dal.domain.vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Vehicle http package body
 * @author Neyzoter Song
 * @date 2019/9/8
 */
public class VehicleHttpPack implements Serializable {

    private static final long serialVersionUID = 6141593762732181713L;
    public static final String INFLUXDB_MEASUREMENT = "vehicle";
    /**
     * frame timestamp
     */
    private String year;
    private String month;
    private String day;
    /**
     * signature : used to certification
     */
    private String sign;
    /**
     * vehicle infomation and runtime data
     */
    private Vehicle vehicle;

    /**
     * get year
     * @return
     */
    public String getYear(){
        return this.year;
    }

    /**
     * set year
     * @param y
     */
    public void setYear(String y){
        this.year = y;
    }

    /**
     * get month
     * @return
     */
    public String getMonth(){
        return this.month;
    }

    /**
     * set month
     * @param m
     */
    public void setMonth(String m){
        this.month = m;
    }

    /**
     * get day
     * @return
     */
    public String getDay(){
        return this.day;
    }

    /**
     * set day
     * @param d
     */
    public void setDay(String d){
        this.day = d;
    }

    /**
     * set sign
     * @param s
     */
    public void setSign(String s){
        this.sign = s;
    }

    /**
     * get sign
     * @return
     */
    public String getSign(){
        return this.sign;
    }

    /**
     * get vehicle
     * @return {@link Vehicle}
     */
    public Vehicle getVehicle(){
        return this.vehicle;
    }

    /**
     * set vehicle
     * @param v {@link Vehicle}
     */
    public void setVehicle(Vehicle v){
        this.vehicle = v;
    }

    @Override
    public String toString(){
        String str = "{"+
                "year="+this.year+"," +
                "month="+this.month+"," +
                "day="+this.day+"," +
                "sign="+this.sign+"," +
                "vehicle="+
                this.vehicle.toString()+
                "}";
        return str;
    }

    /**
     * transform to "measurement tags fields timestamp"<br/>
     * "measurement tag[,tag...] field[,field...] timestamp"
     * @return String[]
     */
    public String[] toInfluxLinesProto () {
        List<String> strs = this.getVehicle().toTagsFieldsTimestamp();
        int size = strs.size();
        String[] lines = new String[size];
        for (int i = 0; i < size; i ++) {
            // "measurement tag[,tag...] field[,field...] timestamp"
            lines[i] = INFLUXDB_MEASUREMENT + "," + strs.get(i);
        }
        return lines;
    }

    /**
     * transform VehicleHttpPack List to InfluxLinesProto
     * @param packList packList
     * @return List<String>
     */
    public static List<String> vhplToInfluxLinesProto (List<VehicleHttpPack> packList) {
        List<String> linesList = new ArrayList<>();
        for (VehicleHttpPack pack : packList) {
            List<String> strs = pack.getVehicle().toTagsFieldsTimestamp();
            int size = strs.size();
            for (int i = 0 ; i < size ; i ++) {
                linesList.add(INFLUXDB_MEASUREMENT + "," + strs.get(i));
            }
        }
        return linesList;
    }
}
