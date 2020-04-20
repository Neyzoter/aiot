package cn.neyzoter.aiot.common.data.serialization;

import java.io.*;

/**
 * Serialization Utils
 * @author Neyzoter Song
 * @date 2019/9/12
 */
public class SerializationUtil implements Serializable {

    private static final long serialVersionUID = 1065846828160869484L;

    /**
     * serialize
     * @param obj
     * @return
     * @throws Exception
     */
    public static byte[] serialize(Object obj) throws Exception{
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try{
            // create byte array output stream
            baos = new ByteArrayOutputStream();
            // create obj output stream, write into baos
            oos = new ObjectOutputStream(baos);
            // write obj into baos
            oos.writeObject(obj);
            // trans to byte[]
            byte[] bytes = baos.toByteArray();
            oos.close();
            return bytes;
        }catch (Exception e){
            throw e;
        }
    }

    public static Object deserialize(byte[] bytes){
        ByteArrayInputStream bais = null;
        Object tmpObject = null;
        try {
            // trans to ByteArrayInputStream
            bais = new ByteArrayInputStream(bytes);
            // trans to obj InputStream
            ObjectInputStream ois = new ObjectInputStream(bais);
            // trans to obj
            tmpObject = (Object)ois.readObject();
            ois.close();
            return tmpObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
