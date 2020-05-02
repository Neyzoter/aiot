package cn.neyzoter.aiot.dal.domain.feature;

import lombok.Getter;

import java.io.Serializable;

/**
 * Output Correlation Matrix
 * @author Charles Song
 * @date 2020-5-2
 */
public class OutputCorrMatrix implements Serializable {
    private static final long serialVersionUID = -8398064420352090633L;
    @Getter
    private Double[][][][] corrMatrix;
    @Getter
    private Long startTime;
    @Getter
    private String vtype;
    /**
     * Output Correlation Matrix
     * @param matrix correlation matrix
     * @param startTime start time
     */
    public OutputCorrMatrix (Double[][][][] matrix, Long startTime, String type) {
        this.corrMatrix = matrix;
        this.startTime = startTime;
        this.vtype = type;
    }
}
