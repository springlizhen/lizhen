package com.chinags.common.utils;

import java.util.ArrayList;
import java.util.List;

public class BinaryEncryptionUtils {

    private static final String ENCRYPTION_STRING = Global.getEncryption();

//    public static String stringEncrypt(String proclaimed){
//        proclaimed += "_"+ENCRYPTION_STRING;
//        char[] strChar = proclaimed.toCharArray();
//        StringBuffer ciphertext = new StringBuffer();
//        for (char str : strChar) {
//            ciphertext.append(Integer.toBinaryString(str)+" ");
//        }
//        return ciphertext.toString();
//    }
//
//    public static String stringDeciphering(String ciphertext){
//        char[] strChar = ciphertext.toCharArray();
//        StringBuffer ciphertext = new StringBuffer();
//        for (char str : strChar) {
//            ciphertext.append(str);
//        }
//        return ciphertext.toString();
//    }

    public static void main(String[] args) {
        List<String> menuCode = new ArrayList<>();
        menuCode.add("a");
        menuCode.add("b");
        System.out.println(StringUtils.join(menuCode.toArray(),","));
    }
}
