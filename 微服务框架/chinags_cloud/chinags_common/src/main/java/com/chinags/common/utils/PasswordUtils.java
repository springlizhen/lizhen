package com.chinags.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringEscapeUtils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 用户密码  生成与正确性验证  工具类
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public class PasswordUtils {

    private static final String SHA1 = "SHA-1";

    public static final int HASH_INTERATIONS = 1024;

    public static final int SALT_SIZE = 8;

    private static SecureRandom random = new SecureRandom();

    /**
     * 生成密文密码，生成随机的16位salt并经过1024次 sha-1 hash
     * @param plainPassword 明文密码
     * @return 16位salt密钥  + 40位hash密码
     */
    public static String encryptPassword(String plainPassword) {
        String plain = decodeHtml(plainPassword);
        byte[] salt = genSalt(SALT_SIZE);
        byte[] hashPassword = sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return encodeHex(salt) + encodeHex(hashPassword);
    }

    /**
     * 验证密码正确性
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        try{
            String plain = decodeHtml(plainPassword);
            byte[] salt = decodeHex(password.substring(0, 16));
            byte[] hashPassword = sha1(plain.getBytes(), salt, HASH_INTERATIONS);
            String pw = encodeHex(salt) + encodeHex(hashPassword);
            return password.equals(pw);
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Html 解码.
     */
    public static String decodeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw unchecked(e);
        }
    }

    /**
     * 对输入字符串进行sha1散列.
     */
    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1, salt, iterations);
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     * @param input 需要散列的字符串
     * @param algorithm 散列算法（"SHA-1"、"MD5"）
     * @param salt
     * @param iterations 迭代次数
     * @return
     */
    public static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(input);

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw unchecked(e);
        }
    }

    /**
     * 生成随机的Byte[]作为salt密钥.
     * @param numBytes byte数组的大小
     */
    public static byte[] genSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }
}
