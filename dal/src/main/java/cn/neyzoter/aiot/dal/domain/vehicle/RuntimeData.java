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
public class RuntimeData implements Serializable, RuntimeDataIf<RuntimeData> {
    private static final long serialVersionUID = 7876266795641274621L;
    public Double val1;
    public Double val2;
    public Double val3;
    public Double val4;
    public Double val5;
    public Double val6;
    public Double val7;
    public Double val8;
    public Double val9;
    public Double val10;
    public Double val11;
    public Double val12;
    public Double val13;
    public Double val14;
    public Double val15;
    public Double val16;
    public Double val17;
    public Double val18;
    public Double val19;
    public Double val20;
    public Double val21;
    public Double val22;
    public Double val23;
    public Double val24;
    public Double val25;
    public Double val26;
    public Double val27;
    public Double val28;
    public Double val29;
    public Double val30;


    public RuntimeData () {
        fieldsSetAccessible();
    }
    /**
     * fields set accessible
     */
    public void fieldsSetAccessible () {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
    }
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
    public void valFromStr (String val, String valName) throws NoSuchFieldException, IllegalAccessException {
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

    /**
     * normalize
     * @param minVal min val
     * @param maxVal max val
     * @param e small constant for normalizing
     */
    @Override
    public void normalize(RuntimeData minVal, RuntimeData maxVal, Double e) throws Exception{
        Field[] fields = this.getClass().getDeclaredFields();
        Double delta;Double max;Double min;Double thisVal;
        try {
            for (Field field : fields) {
                // except out of Double.class
                if (field.getType().isAssignableFrom(Double.class)) {
                    max = (Double) field.get(maxVal);
                    min = (Double) field.get(minVal);
                    thisVal = (Double) field.get(this);
                    delta = max - min + e;
                    field.set(this, (thisVal - min) / delta);
                }
            }
        } catch (Exception ex ) {
            throw ex;
        }
    }

    /**
     * normalize
     * @param minVal min val
     * @param deltaVal (max - min + e), e is a small constant for normalizing
     */
    public void normalize(RuntimeData minVal, RuntimeData deltaVal) throws Exception {
        Field[] fields = this.getClass().getDeclaredFields();
        Double delta;Double min;Double thisVal;
        try {
            for (Field field : fields) {
                // except out of Double.class
                if (field.getType().isAssignableFrom(Double.class)) {
                    min = (Double) field.get(minVal);
                    thisVal = (Double) field.get(this);
                    delta = (Double) field.get(deltaVal);
                    field.set(this, (thisVal - min) / delta);
                }
            }
        } catch (Exception ex ) {
            throw ex;
        }
    }
}
