package cn.neyzoter.aiot.dal.domain.feature;

import lombok.Getter;

import java.io.Serializable;

/**
 * Input Correlation Matrix
 * @author Charles Song
 * @date 2020-5-2
 */
public class InputCorrMatrix implements Serializable {
    private static final long serialVersionUID = 5428219142630863963L;
    @Getter
    private Double[][][][] corrMatrix;
    @Getter
    private Long start;

    /**
     * Input Correlation Matrix
     * @param matrix correlation matrix
     * @param startTime start time
     */
    public InputCorrMatrix (Double[][][][] matrix, Long startTime) {
        this.corrMatrix = matrix;
        this.start = startTime;
    }
}
