package com.yline.sm.crypt;

import android.util.Base64;

import java.security.SecureRandom;

public class SM4Utils {
    /* 使用CBC模式，需要一个向量iv，可增加加密算法的强度 */
    private static final String PARAMETER_SPEC = "1234567890123456";
    
    /**
     * ECB 模式，加密
     *
     * @param sourceText 原始数据
     * @param keyBytes   秘钥，长度 = 16
     * @return 加密后的数据，Base64处理过的，可能为null
     */
    public static String encryptECB(String sourceText, byte[] keyBytes) {
        byte[] cipherBytes = encryptECBInner(sourceText.getBytes(), keyBytes);
        if (null == cipherBytes) {
            return null;
        } else {
            return Base64.encodeToString(cipherBytes, Base64.NO_WRAP);
        }
    }
    
    /**
     * ECB 模式，解密
     *
     * @param cipherText 加密的数据，被Base64处理过的
     * @param keyBytes   秘钥，长度 = 16
     * @return 解密后的数据，可能为null
     */
    public static String decryptECB(String cipherText, byte[] keyBytes) {
        byte[] cipherBytes = Base64.decode(cipherText, Base64.NO_WRAP);
        byte[] sourceBytes = decryptECBInner(cipherBytes, keyBytes);
        return null == sourceBytes ? null : new String(sourceBytes);
    }
    
    /**
     * CBC 模式，加密
     *
     * @param sourceText 原始数据
     * @param keyBytes   秘钥，长度 = 16
     * @return 加密后的数据，Base64处理过的，可能为null
     */
    public static String encryptCBC(String sourceText, byte[] keyBytes) {
        byte[] cipherBytes = encryptCBCInner(sourceText.getBytes(), keyBytes, PARAMETER_SPEC.getBytes());
        if (null == cipherBytes) {
            return null;
        } else {
            return Base64.encodeToString(cipherBytes, Base64.NO_WRAP);
        }
    }
    
    /**
     * CBC 模式，解密
     *
     * @param cipherText 加密的数据，被Base64处理过的
     * @param keyBytes   秘钥，长度 = 16
     * @return 解密后的数据，可能为null
     */
    public static String decryptCBC(String cipherText, byte[] keyBytes) {
        byte[] cipherBytes = Base64.decode(cipherText, Base64.NO_WRAP);
        byte[] sourceBytes = decryptCBCInner(cipherBytes, keyBytes, PARAMETER_SPEC.getBytes());
        return null == sourceBytes ? null : new String(sourceBytes);
    }
    
    /**
     * 随机生成一个 SM4 秘钥
     *
     * @return 字符流，长度为16
     */
    public static byte[] createSM4Key() {
        return createSM4KeyInner();
    }
    
    /* ------------------------ 内部实现 ----------------------- */
    
    /**
     * 随机生成一个 SM4 秘钥
     *
     * @return byte数组，长度为16
     */
    private static byte[] createSM4KeyInner() {
        byte[] keyBytes = new byte[16];
    
        SecureRandom random = new SecureRandom();
        random.nextBytes(keyBytes);
        return keyBytes;
    }
    
    /**
     * CBC 模式，加密
     *
     * @param srcBytes 原始数据
     * @param keyBytes 秘钥，长度 = 16
     * @param ivBytes  偏移量，长度 = 16
     * @return 加密后的数据
     */
    private static byte[] encryptCBCInner(byte[] srcBytes, byte[] keyBytes, byte[] ivBytes) {
        try {
            SM4 sm4 = new SM4();
            long[] secretKey = sm4.sm4_setkey_enc(keyBytes);
            return sm4.sm4_crypt_cbc(SM4.SM4_ENCRYPT, secretKey, true, ivBytes, srcBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * CBC 模式，解密
     *
     * @param encryptedBytes 加密后的数据
     * @param keyBytes       秘钥，长度 = 16
     * @param ivBytes        偏移量，长度 = 16
     * @return 解密后的数据
     */
    private static byte[] decryptCBCInner(byte[] encryptedBytes, byte[] keyBytes, byte[] ivBytes) {
        try {
            SM4 sm4 = new SM4();
            long[] secretKey = sm4.sm4_setkey_dec(keyBytes);
            return sm4.sm4_crypt_cbc(SM4.SM4_DECRYPT, secretKey, true, ivBytes, encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * ECB 模式，加密
     *
     * @param srcBytes 原始数据
     * @param keyBytes 秘钥，长度 = 16
     * @return 加密后的数据
     */
    private static byte[] encryptECBInner(byte[] srcBytes, byte[] keyBytes) {
        try {
            SM4 sm4 = new SM4();
            long[] secretKey = sm4.sm4_setkey_enc(keyBytes);
            return sm4.sm4_crypt_ecb(SM4.SM4_ENCRYPT, secretKey, true, srcBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * ECB 模式，解密
     *
     * @param encryptBytes 加密后的数据
     * @param keyBytes     秘钥，长度 = 16
     * @return 解密后的数据
     */
    private static byte[] decryptECBInner(byte[] encryptBytes, byte[] keyBytes) {
        try {
            SM4 sm4 = new SM4();
            long[] secretKey = sm4.sm4_setkey_dec(keyBytes);
            return sm4.sm4_crypt_ecb(SM4.SM4_DECRYPT, secretKey, true, encryptBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
