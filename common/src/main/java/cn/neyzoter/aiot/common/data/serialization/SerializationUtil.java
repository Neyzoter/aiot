package cn.neyzoter.aiot.common.data.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialization Utils
 * @author Neyzoter Song
 * @date 2019/9/12
 */
public class SerializationUtil{

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
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
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
            // Deserialize
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            tmpObject = (Object)ois.readObject();
            ois.close();
            return tmpObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
