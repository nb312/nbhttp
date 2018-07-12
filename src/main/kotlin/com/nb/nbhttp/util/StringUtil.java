package com.nb.nbhttp.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：字符串操作类.
 */
public class StringUtil {
    public static final String UTF_8 = "UTF-8";

    /**
     * 将url进行转码.
     *
     * @param str         需要转码的字符串.
     * @param charsetName 类型：一般为'UTF-8'.
     * @return 处理后的字符串.
     */
    public static String getURLEncode(String str, String charsetName) {
        try {
            str = URLEncoder.encode(str, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将url进行转码，charsetName采用UTF-8.
     *
     * @param str 需要转码的字符串.
     * @return 处理后的字符串.
     */
    public static String getURLEncode(String str) {
        return getURLEncode(str, UTF_8);
    }

    /**
     * 将url进行转码.
     *
     * @param str         需要转码的字符串.
     * @param charsetName 类型：一般为'UTF-8'.
     * @return 处理后的字符串。
     */
    public static String getURLDecode(String str, String charsetName) {
        try {
            str = URLDecoder.decode(str, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将url进行转码，charsetName采用UTF-8.
     *
     * @param str 需要转码的字符串.
     * @return 处理后的字符串.
     */
    public static String getURLDecode(String str) {
        return getURLDecode(str, UTF_8);
    }

    /**
     * 字符串逆序.
     *
     * @param str 需要逆序的字符串.
     * @return 处理后的字符串.
     */
    public static String reverseString(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        return stringBuilder.reverse().toString();
    }

    /**
     * 测试字符串，返回当前字符串中子字符串的重复数量.
     *
     * @param strTest 测试字符串.
     * @param strSub  子字符串.
     * @return 处理后的字符串.
     */
    public static int subStringCount(String strTest, String strSub) {
        int count = 0, start = 0;
        while ((start = strTest.indexOf(strSub, start)) >= 0) {
            start += strSub.length();
            count++;
        }
        return count;
    }

    /**
     * 字符串加密.
     *
     * @param str 需要加密的字符串
     * @return 处理后的字符串
     */
    public static String encode(String str) {
        String result;
        if (str == null) {
            result = "";
        } else {
            result = str;
        }
        try {
            result = reverseString(result);
            result = new String(Base64.encode(result.getBytes(UTF_8), Base64.DEFAULT), UTF_8);
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(result);
            result = m.replaceAll("");
            result = reverseString(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串解密.
     *
     * @param str 需要解密的字符串
     * @return 处理后的字符串
     */
    public static String decode(String str) {
        String result;
        if (str == null) {
            result = "";
        } else {
            result = str;
        }
        try {
            result = reverseString(result);
            result = new String(Base64.decode(result.getBytes(UTF_8), Base64.DEFAULT), UTF_8);
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(result);
            result = m.replaceAll("");
            result = reverseString(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将url末尾的文件名分离并返回文件名.
     *
     * @param strUrl 需要处理的url字符串
     * @return 返回文件名，并且会进行URLDecode，执行失败会返回原始字符串。
     */
    public static String getUrlFileName(String strUrl) {
        String fileName;
        if (strUrl.contains("/")) {
            int lastIndex = strUrl.lastIndexOf("/");
            fileName = strUrl.substring(lastIndex + 1);
        } else if (strUrl.contains("\\")) {
            int lastIndex = strUrl.lastIndexOf("\\");
            fileName = strUrl.substring(lastIndex + 1);
        } else {
            fileName = strUrl;
        }
        return getURLDecode(fileName);
    }

    /**
     * 将字符串做MD5.
     *
     * @param string 需要处理的字符串
     * @return 32位MD5字符串。
     */
    public static String getMD5Bit32(String string) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] stringByte = string.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(stringByte);
            byte[] md5Byte = messageDigest.digest();
            int j = md5Byte.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byteValue = md5Byte[i];
                str[k++] = hexDigits[byteValue >>> 4 & 0xf];
                str[k++] = hexDigits[byteValue & 0xf];
            }
            return (new String(str));
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }
    }

    /**
     * 将字符串做MD5.
     *
     * @param string 需要处理的字符串
     * @return 16位MD5字符串。
     */
    public static String getMD5Bit16(String string) {
        return getMD5Bit32(string).substring(8, 24);
    }

    /**
     * 生成随机字符串.
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
