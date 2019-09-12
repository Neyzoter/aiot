package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import java.io.Serializable;

/**
 * Runtime data for vehicle
 * @author Neyzoter Song
 * @date 2019/9/7
 */
public class RuntimeData implements Serializable {
    /**
     * car speed
     */
    protected int speed;

    /**
     * car temperature
     */
    protected int ecuMaxTemp;

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
    public int getSpeed(){
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
    public int getEcuMaxTemp(){
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
