package com.yline.sm.crypt;

import com.yline.utils.crypt.HexUtils;

public class SM3Utils {
    /**
     * 字符串 加密成 64位十六进制字符串
     *
     * @param sourceBytes such as "yline".getBytes()
     * @return such as 66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0
     */
    public static String encrypt(byte[] sourceBytes) {
        byte[] digestBytes = encryptInner(sourceBytes);
        return new String(HexUtils.encodeHex(digestBytes, false));
    }
    
    /**
     * 字符串 加密成 64位十六进制字符串
     *
     * @param sourceBytes such as "yline".getBytes()
     * @param toLowerCase 是否小写输出
     * @return such as 66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0
     */
    public static String encrypt(byte[] sourceBytes, boolean toLowerCase) {
        byte[] digestBytes = encryptInner(sourceBytes);
        return new String(HexUtils.encodeHex(digestBytes, toLowerCase));
    }
    
    /**
     * SM3 加密
     *
     * @param sourceBytes 原始数组
     * @return 加密后的，32位数组
     */
    private static byte[] encryptInner(byte[] sourceBytes) {
        SM3Digest digest = new SM3Digest();
        digest.update(sourceBytes, 0, sourceBytes.length);
        
        byte[] result = new byte[32];
        digest.doFinal(result, 0);
        return result;
    }
}
