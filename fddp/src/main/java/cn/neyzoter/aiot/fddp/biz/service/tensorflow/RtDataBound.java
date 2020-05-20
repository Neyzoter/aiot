package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.tensorflow.ModelPropertiesLabels;
import cn.neyzoter.aiot.common.tensorflow.RtdataConfiger;
import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * Rt data bound
 * @author Charles Song
 * @date 2020-5-20
 */
public class RtDataBound extends RtdataConfiger implements Serializable {
    private static final long serialVersionUID = -1764585966084767L;
    public static final Logger logger = LoggerFactory.getLogger(RtDataBound.class);
    /**
     * max Runtime data
     */
    @Getter
    private RuntimeData maxRtData;

    /**
     * min Runtime data
     */
    @Getter
    private RuntimeData minRtData;

    /**
     * max rt data minus min rt data
     */
    @Getter
    @Setter
    private RuntimeData deltaRtData;

    /**
     * create runtime data bound
     * @param path path of the model.properties
     * @param k key
     * @param maxAliveTime max alive time after not been used
     */
    RtDataBound (String path, String k, int maxAliveTime) throws Exception  {
        super(path,k,maxAliveTime);
        this.maxRtData = new RuntimeData();
        this.minRtData = new RuntimeData();
        this.deltaRtData = new RuntimeData();
        this.updateMaxRtData();
        this.updateMinRtData();
        this.updateDeltaRtData();
    }
    /**
     * update max runtime data
     * @return max {@link RuntimeData}
     */
    public RuntimeData updateMaxRtData () throws Exception{
        this.updateMaxValueMap();
        Iterator<Map.Entry<String , String>> iter = this.getMaxValueMap().entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<String, String> entry = iter.next();
            String valName = entry.getKey();
            String valVal = entry.getValue();
            this.maxRtData.valFromStr(valVal, valName);
        }
        return this.maxRtData;
    }
    /**
     * update min runtime data
     * @return min {@link RuntimeData}
     */
    public RuntimeData updateMinRtData () throws Exception {
        this.updateMinValueMap();
        Iterator<Map.Entry<String, String>> iter = this.getMinValueMap().entrySet().iterator();
        for (; iter.hasNext(); ) {
            Map.Entry<String, String> entry = iter.next();
            String valName = entry.getKey();
            String valVal = entry.getValue();
            this.minRtData.valFromStr(valVal, valName);
        }
        return this.minRtData;
    }

    /**
     * update delta runtime data
     * @return RuntimeData
     * @throws Exception maxValueMap's size is not equal to minValueMap's
     * @throws Exception minValueMap does not contain maxValueMap's key
     */
    public RuntimeData updateDeltaRtData () throws Exception  {
        Iterator<Map.Entry<String,String>> maxIter = this.getMaxValueMap().entrySet().iterator();
        if (this.getMaxValueMap().size() != this.getMinValueMap().size()) {
            throw new Exception("maxValueMap's size is not equal to minValueMap's");
        }
        for ( ; maxIter.hasNext() ; ) {
            Map.Entry<String, String> entry = maxIter.next();
            if (!this.getMinValueMap().containsKey(entry.getKey())) {
                throw  new Exception("minValueMap does not contain maxValueMap's key");
            } else {
                String minString = this.getMinValueMap().get(entry.getKey());
                Double min = Double.parseDouble(minString);
                Double max = Double.parseDouble(entry.getValue());
                Double delta = max - min + this.getNormalizedE();
                this.deltaRtData.valFromStr(delta.toString(), entry.getKey());
            }
        }
        return this.deltaRtData;
    }

}
