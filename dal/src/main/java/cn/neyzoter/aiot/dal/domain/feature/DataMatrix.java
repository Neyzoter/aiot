package cn.neyzoter.aiot.dal.domain.feature;

import lombok.Getter;

import java.io.Serializable;

/**
 * data matrix<br/>
 * [var num][time series]
 * @author Charles Song
 * @date 2020-4-25
 */
public class DataMatrix implements Serializable {
    private static final long serialVersionUID = -4793223507478566000L;
    @Getter
    Double[][] matrix;
    /**
     * start time
     */
    @Getter
    private Long startTime;
    @Getter
    private String vtype;
    public DataMatrix (Double[][] data, Long startTime, String type) {
        this.matrix = data;
        this.startTime = startTime;
        this.vtype = type;
    }
}
