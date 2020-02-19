package cn.neyzoter.aiot.dal.domain.vehicle;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * Vehicle class
 * @author Neyzoter Song
 * @date 2019/9/7
 */
public class Vehicle implements Serializable {
    private Long app;
    private String vid;
    private String vtype;
    private SortedMap<Integer, RuntimeData> rtDataMap;

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
     * get rt data map
     * @return
     */
    public SortedMap<Integer, RuntimeData> getRtDataMap () {
        return this.rtDataMap;
    }

    /**
     * set rt data map
     * @param data
     */
    public void setRtDataMap (SortedMap<Integer, RuntimeData> data) {
        this.rtDataMap = data;
    }

    /**
     * get vehicle id
     * @return vid
     */
    public String getVid () {
        return this.vid;
    }

    /**
     * set vehicle id
     */
    public void setVid (String id) {
        this.vid = id;
    }

    /**
     * get vehicle type
     * @return
     */
    public String getVtype () {
        return this.vtype;
    }

    /**
     * set vehicle type
     * @param type
     */
    public void setVtype (String type) {
        this.vtype = type;
    }

    @Override
    public String toString(){
        String str = new String();
        try {
            str += "{" +
                    "app="+this.app + ","+
                    "vid=" + this.vid + ","+
                    "vtype=" + this.vtype + "," +
                    "rtDataMap={";
            Iterator it = rtDataMap.entrySet().iterator();
            for (;it.hasNext();) {
                Map.Entry data = (Map.Entry)it.next();
                str += String.format("%d=%s%s",data.getKey(),data.getValue().toString(),it.hasNext()?",":"");
            }
            str += "}";
        }catch (Exception e) {
            System.err.println(e);
        }
        return str;
    }
}
