package cn.neyzoter.aiot.dal.domain.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * All type runtime data for vehicle
 * @author Neyzoter Song
 * @date 2019/9/7
 */
@Setter
@Getter
@ToString
public class RuntimeData implements Serializable, RuntimeDataIf {
    private static final long serialVersionUID = 7876266795641274621L;
    protected Double val1;
    protected Double val2;
    protected Double val3;
    protected Double val4;
    protected Double val5;
    protected Double val6;
    protected Double val7;
    protected Double val8;
    protected Double val9;
    protected Double val10;
    protected Double val11;
    protected Double val12;
    protected Double val13;
    protected Double val14;
    protected Double val15;
    protected Double val16;
    protected Double val17;
    protected Double val18;
    protected Double val19;
    protected Double val20;
    protected Double val21;
    protected Double val22;
    protected Double val23;
    protected Double val24;
    protected Double val25;
    protected Double val26;
    protected Double val27;
    protected Double val28;
    protected Double val29;
    protected Double val30;


    /**
     * transform to fields, compatible to influx
     * @return String
     */
    @Override
    public String toFields () {
        String str = this.toString();
        // match "RuntimeData(" or ")" or " "
        str = str.replaceAll("RuntimeData\\(| |\\)","");
        return str;
    }

    /**
     * set val from String name
     * @param val val
     * @param valName valName
     * @throws NoSuchFieldException no such field exception
     * @throws IllegalAccessException illegal access exception
     */
    @Override
    public void valFromStr (String val, String valName) throws NoSuchFieldException, IllegalAccessException{
        Field field = this.getClass().getDeclaredField(valName);
        boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            if (field.getType().isAssignableFrom(Double.class)) {
                field.set(this, Double.parseDouble(val));
            } else if (field.getType().isAssignableFrom(Float.class)) {
                field.set(this, Float.parseFloat(val));
            } else if (field.getType().isAssignableFrom(Integer.class)) {
                field.set(this, Integer.parseInt(val));
            } else if (field.getType().isAssignableFrom(Long.class)) {
                field.set(this, Long.parseLong(val));
            } else if (field.getType().isAssignableFrom(String.class)) {
                field.set(this, val);
            }
        } finally {
            field.setAccessible(isAccessible);
        }
    }
}
