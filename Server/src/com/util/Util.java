
package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(plainText.getBytes());

            return new String(result);

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isStrEmpty(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
