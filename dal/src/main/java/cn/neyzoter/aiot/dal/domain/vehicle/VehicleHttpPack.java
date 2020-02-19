package cn.neyzoter.aiot.dal.domain.vehicle;

import java.io.Serializable;

/**
 * Vehicle http package body
 * @author Neyzoter Song
 * @date 2019/9/8
 */
public class VehicleHttpPack implements Serializable {
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
    private Vehicle vehicleTemp;

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
        return this.vehicleTemp;
    }

    /**
     * set vehicle
     * @param v {@link Vehicle}
     */
    public void setVehicle(Vehicle v){
        this.vehicleTemp = v;
    }

    @Override
    public String toString(){
        String str = "{"+
                "year="+this.year+"," +
                "month="+this.month+"," +
                "day="+this.day+"," +
                "sign="+this.sign+"," +
                "vehicle="+
                this.vehicleTemp.toString()+
                "}";
        return str;
    }

}
