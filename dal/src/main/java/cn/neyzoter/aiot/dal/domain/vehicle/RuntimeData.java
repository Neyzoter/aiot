package cn.neyzoter.aiot.dal.domain.vehicle;

import java.io.Serializable;

/**
 * All type runtime data for vehicle
 * @author Neyzoter Song
 * @date 2019/9/7
 */
public class RuntimeData implements Serializable {
    /**
     * car speed
     */
    protected Integer speed;

    /**
     * car temperature
     */
    protected Integer ecuMaxTemp;

    /**
     * set speed
     * @param spd
     */
    public void setSpeed(int spd){
        this.speed = spd;
    }
    /**
     * get speed
     * @return
     */
    public Integer getSpeed(){
        return this.speed;
    }

    /**
     * set ecuMaxTemp
     * @param temp
     */
    public void setEcuMaxTemp(int temp){
        this.ecuMaxTemp = temp;
    }

    /**
     * get ecuMaxTemp
     * @return
     */
    public Integer getEcuMaxTemp(){
        return this.ecuMaxTemp;
    }

    @Override
    public String toString(){
        String str = "{"+
                "speed="+speed + "," +
                "ecuMaxTemp="+ecuMaxTemp+
                "}";
        return str;
    }
}
