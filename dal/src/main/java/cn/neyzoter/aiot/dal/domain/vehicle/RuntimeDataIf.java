package cn.neyzoter.aiot.dal.domain.vehicle;

import com.sun.istack.internal.NotNull;

/**
 * RuntimeData Interface, Data must implement this interface
 * @author Neyzoter Song
 * @date 2020-3-11
 */
public interface RuntimeDataIf {
    /**
     * transform to fields, compatible to influx
     * @return String
     */
    public String toFields () ;

    /**
     * set val from String name<br/>if your fields has different type var, need to take measures against the problem<br/> there is a simple example:<br/>
     * <PRE>
     * public void valFromStr (String val, String valName) throws NoSuchFieldException, IllegalAccessException{
     *     Field field = this.getClass().getDeclaredField(valName);
     *     boolean isAccessible = field.isAccessible();
     *     try {
     *         field.setAccessible(true);
     *         if (field.getType().isAssignableFrom(Double.class)) {
     *             field.set(this, Double.parseDouble(val));
     *         } else if (field.getType().isAssignableFrom(Float.class)) {
     *             field.set(this, Float.parseFloat(val));
     *         } else if (field.getType().isAssignableFrom(Integer.class)) {
     *             field.set(this, Integer.parseInt(val));
     *         } else if (field.getType().isAssignableFrom(Long.class)) {
     *             field.set(this, Long.parseLong(val));
     *         } else if (field.getType().isAssignableFrom(String.class)) {
     *             field.set(this, val);
     *         }
     *     } finally {
     *         field.setAccessible(isAccessible);
     *     }
     * }
     * </PRE>
     * @param val val
     * @param valName valName
     * @throws NoSuchFieldException no such field exception
     * @throws IllegalAccessException illegal access exception
     */
    public void valFromStr (@NotNull String val, @NotNull String valName) throws NoSuchFieldException, IllegalAccessException;
}
