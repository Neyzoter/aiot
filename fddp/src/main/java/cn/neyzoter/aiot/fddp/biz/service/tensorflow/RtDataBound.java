package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.tensorflow.ModelPropertiesLabels;
import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.dal.domain.vehicle.RuntimeData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * Rt data bound
 * @author Charles Song
 * @date 2020-5-20
 */
public class RtDataBound implements Serializable {
    private static final long serialVersionUID = -1764585966084767L;
    /**
     * max Runtime data
     */
    @Getter
    @Setter
    private RuntimeData maxRtData;

    /**
     * min Runtime data
     */
    @Getter
    @Setter
    private RuntimeData minRtData;

    /**
     * normalized e
     */
    @Getter
    @Setter
    private Double normalizedE;
    /**
     * RtDataBound's key
     */
    @Getter
    @Setter
    private String key;

    public RtDataBound (String k, PropertiesUtil p) {
        this.key = k;
        updateMaxOrMinRtData(true, p);
        updateMaxOrMinRtData(false, p);
        updateNormalizedE(p);
    }
    /**
     * update max or min rt data
     * @param isMax is max rt data
     * @param p properties util
     */
    public void updateMaxOrMinRtData (boolean isMax, PropertiesUtil p) {
        RuntimeData data = new RuntimeData();
        Map<String, String> map;
        if (isMax) {
            map = p.getPropertiesMap(p.readValue(ModelPropertiesLabels.MODLE_MAX_VALUE));
        } else {
            map = p.getPropertiesMap(p.readValue(ModelPropertiesLabels.MODLE_MIN_VALUE));
        }
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<String, String> entry = iter.next();
            String varName = entry.getKey();
            String varVal = entry.getValue();
            try {
                data.valFromStr(varVal, varName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (isMax) {
            maxRtData = data;
        } else {
            minRtData = data;
        }
    }

    /**
     * update normalized e
     * @param p PropertiesUtil
     */
    public void updateNormalizedE (PropertiesUtil p) {
        try {
            String eStr = p.readValue(ModelPropertiesLabels.MODLE_NORMALIZED_E);
            normalizedE = Double.parseDouble(eStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
