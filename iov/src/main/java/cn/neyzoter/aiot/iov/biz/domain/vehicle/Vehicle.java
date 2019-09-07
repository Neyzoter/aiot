package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import java.io.Serializable;

/**
 * Vehicle class
 * @author Neyzoter Song
 * @date 2019/9/7
 */
public class Vehicle implements Serializable {
    private long id;
    private RuntimeData rtData;

    /**
     * set id
     * @param identity
     */
    public void setId(long identity){
        this.id = identity;
    }

    /**
     * get id
     * @return
     */
    public long getId(){
        return this.id;
    }

    /**
     * get data
     * @return {@link RuntimeData}
     */
    public RuntimeData getData(){
        return this.rtData;
    }

    @Override
    public String toString(){
        return "Vehicle{" +
                "id=" + id +
                "data={" +
                "speed="+rtData.speed +
                "ecuMaxTemp="+rtData.ecuMaxTemp+
                "}"+
                "}";
    }
}
