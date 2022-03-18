package userservice.src.security;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
 
/**
 * Demonstrates how to generate MD5 hash using Java
 * @author JJ
 */
public class Secure {
 
    /**
     * Returns a hexadecimal encoded MD5 hash for the input String.
     * @param data
     * @return 
     */
    private String getMD5Hash (String data) {
        String result = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); 
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
     
    public String encode (String data) {
        return getMD5Hash(data);
    }

    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in upper case.
     * @param hash
     * @return 
     */
    private String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }

    public String getSuffixForEncoding (String email, String userName) {
        return email + userName;
    }
}