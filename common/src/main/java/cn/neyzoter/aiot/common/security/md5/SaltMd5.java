package cn.neyzoter.aiot.common.security.md5;

import org.omg.CORBA.portable.UnknownException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 encryption with salt
 * @author Neyzoter Song
 * @date 2019/9/8
 */
public class SaltMd5{
    public static String getSaltMd5(String info, String salt) throws NoSuchAlgorithmException,UnsupportedEncodingException,UnknownException{
        String digest = null;
        try {
            byte[] keyByte = info.getBytes("UTF-8");
            byte[] saltByte = salt.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] userKeyHash = md.digest(keyByte);

            byte[] cat = new byte[userKeyHash.length + saltByte.length];
            System.arraycopy(userKeyHash, 0, cat, 0, userKeyHash.length);
            System.arraycopy(saltByte, 0, cat, userKeyHash.length, saltByte.length);
            StringBuilder sb = new StringBuilder(2 * cat.length);
            for (byte b : cat) {
                sb.append(String.format("%02x", b & 0xff));
            }

            byte[] hash = md.digest(cat);
            //converting byte array to Hexadecimal String
            sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        }catch (NoSuchAlgorithmException e) {
            throw e;
        }catch (UnsupportedEncodingException e) {
            throw e;
        }catch (Exception e){
            throw (new UnknownException(e));
        }
        return digest.toUpperCase();
    }
}
