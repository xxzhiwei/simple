package com.aojiaodage.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String encode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");

            // instance.digest(target.getBytes(StandardCharsets.UTF_8)); 并不是最终的摘要结果，还需要将byte数组转为十六进制的数字
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            BigInteger no = new BigInteger(1, bytes);
            StringBuilder hashText = new StringBuilder(no.toString(16));

            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }

            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String r = Md5Util.encode("123");
        System.out.println(r);
    }
}
