package cn.neyzoter.aiot.iov.biz.domain.vehicle;

import java.io.Serializable;

/**
 * Vehicle class
 * @author Neyzoter Song
 * @date 2019/9/7
 */
public class Vehicle implements Serializable {
    private Long app;
    private Long id;
    private RuntimeData rtData;

    /**
     * get Application id
     * @return
     */
    public Long getApp() {
        return this.app;
    }

    /**
     * set appliacction id
     * @param application
     */
    public void setApp(Long application) {
        this.app = application;
    }

    /**
     * set id
     * @param identity
     */
    public void setId(Long identity){
        this.id = identity;
    }

    /**
     * get id
     * @return
     */
    public Long getId(){
        return this.id;
    }

    /**
     * get runtime data
     * @return {@link RuntimeData}
     */
    public RuntimeData getRtData(){
        return this.rtData;
    }

    /**
     * set runtime data
     * @param data
     */
    public void setRtData(RuntimeData data){
        this.rtData = data;
    }

    @Override
    public String toString(){
        String str = "{" +
                "app="+this.app + ","+
                "id=" + this.id + ","+
                "rtData=" +
                this.rtData.toString()+
                "}";
        return str;
    }
}
