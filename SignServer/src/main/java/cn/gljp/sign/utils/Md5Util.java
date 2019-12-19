package cn.gljp.sign.utils;

import java.security.MessageDigest;

public class Md5Util {
        public Md5Util() {
        }

        public static final String MD5(String inStr) {
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception var8) {
                System.out.println(var8.toString());
                var8.printStackTrace();
                return "";
            }

            char[] charArray = inStr.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for(int i = 0; i < charArray.length; ++i) {
                byteArray[i] = (byte)charArray[i];
            }

            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();

            for(int i = 0; i < md5Bytes.length; ++i) {
                int val = md5Bytes[i] & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();
        }
}
