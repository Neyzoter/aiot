package cn.neyzoter.aiot.dal.domain.feature;

import java.io.Serializable;

/**
 * Correlation matrix
 * @author Charles Song
 * @date 2020-4-25
 */
public class CorrMatrix implements Serializable {
    private static final long serialVersionUID = -4793223507478566000L;
    double[][] matrix;
    CorrMatrix (int size) {
        this.matrix = new double[size][size];
    }
}
