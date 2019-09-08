package cn.neyzoter.aiot.common.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Time relative utils
 * @author Neyzoter Song
 * @date 2019/9/8
 */
public class TimeUtil {
    /**
     * get yyyy-MM-dd'T'HH:mm:ss now
     * @return {@link String}
     */
    public static String getStrIsoSTime() {
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return df.format(calendar.getTime());
    }
    /**
     * get yyyy-MM now
     * @return {@link String}
     */
    public static String getStrIsoMTime() {
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(calendar.getTime());
    }
}
