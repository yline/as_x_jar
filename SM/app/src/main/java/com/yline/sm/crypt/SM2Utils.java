package com.yline.sm.crypt;

import android.util.Base64;

import java.io.IOException;
import java.math.BigInteger;

public class SM2Utils {
    /**
     * 生成密钥对，公钥和私钥
     *
     * @return 密钥对
     */
    public static SM2Impl.SM2KeyPair createKeyPair() {
        return new SM2Impl().genKeyPair();
    }
    
    /**
     * 获取私钥（经过Base64转码过）
     *
     * @param keyPair 秘钥对
     * @return 被Base64转码加密过的私钥
     */
    public static String getPrivateKey(SM2Impl.SM2KeyPair keyPair) {
        if (null == keyPair) {
            return null;
        }
        
        BigInteger privateKeyInteger = keyPair.getPrivateKey();
        byte[] privateKeyBytes = SM2.bigInteger2Bytes(privateKeyInteger);
        return (null == privateKeyBytes ? null : Base64.encodeToString(privateKeyBytes, Base64.NO_WRAP));
    }
    
    /**
     * 获取公钥（经过Base64转码加密过）
     *
     * @param keyPair 密钥对
     * @return 被Base64转码加密过的公钥
     */
    public static String getPublicKey(SM2Impl.SM2KeyPair keyPair) {
        if (null == keyPair) {
            return null;
        }
        
        byte[] publicKeyBytes = keyPair.getPublicKey().getEncoded();
        return (null == publicKeyBytes ? null : Base64.encodeToString(publicKeyBytes, Base64.NO_WRAP));
    }
    
    public static byte[] encrypt(byte[] publicKey, byte[] data) throws IOException {
        return new SM2Impl().encrypt(data, publicKey);
    }
    
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        return new SM2Impl().decrypt(encryptedData, privateKey);
    }
    
    public static byte[] sign(byte[] userId, byte[] privateKey, byte[] sourceData) throws IOException {
        return new SM2Impl().sign(userId, privateKey, sourceData);
    }
    
    public static boolean verifySign(byte[] userId, byte[] publicKey, byte[] sourceData, byte[] signData) throws IOException {
        return new SM2Impl().verifySign(userId, publicKey, sourceData, signData);
    }
}
