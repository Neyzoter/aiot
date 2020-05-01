package cn.neyzoter.aiot.fddp.biz.service.spark.exception;

/**
 * Illegal Win num
 * @author Charles Song
 * @date 2020-5-1
 */
public class IllWinNum extends Exception {
    private static final long serialVersionUID = -8831627734789228103L;
    private int winNum;
    private int winStrLen;
    public IllWinNum (int winNum, int winStrLen) {
        this.winNum = winNum;
        this.winStrLen = winStrLen;
    }
    @Override
    public String toString () {
        return String.format("Illegal Win Num! winNum = %d, winStrNum = %d", this.winNum, this.winStrLen);
    }
}
