package com.yline.sm.test;

import android.util.Base64;

import com.yline.sm.crypt.SM2Impl;
import com.yline.sm.crypt.SM2Utils;
import com.yline.utils.LogUtil;
import com.yline.utils.crypt.HexUtils;

public class SM2Test {
    /**
     * SM2，秘钥对，是原始文档中提供的
     * 当，SM2中的，ecc_param，为测试参数时，能够校验成功
     * 当，SM2中的，ecc_param，为正式参数时，会校验失败【因为，密钥对是错误的】
     */
    public void testSM2Provider() {
        String privateKey = "128B2FA8BD433C6C068C8D803DFF79792A519A55171B1B650C23661D15897263";
        byte[] privateBytes = HexUtils.decodeHex(privateKey.toCharArray());
        String publicKey = "040AE4C7798AA0F119471BEE11825BE46202BB79E2A5844495E97C04FF4DF2548A7C0240F88F1CD4E16352A73C17B7F16F07353E53A176D684A9FE0C6BB798E857";
        byte[] publicBytes = HexUtils.decodeHex(publicKey.toCharArray());
        
        String source = "message DIGEST";
        String userId = "ALICE123@YAHOO.COM";
        
        testSM2(privateBytes, publicBytes, userId, source);
    }
    
    /**
     * SM2，秘钥对，是随机生成的。
     * 当，SM2中的，ecc_param，为测试参数或正式参数，都能够校验成功
     */
    public void testSM2Creator() {
        SM2Impl.SM2KeyPair keyPair = SM2Utils.createKeyPair();
        String privateKey = SM2Utils.getPrivateKey(keyPair);
        if (null == privateKey) {
            LogUtil.e("keyPair = " +keyPair + ", privateKey = " + privateKey);
            return;
        }
        byte[] privateBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        
        String publicKey = SM2Utils.getPublicKey(keyPair);
        byte[] publicBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        
        LogUtil.v("-------------------------------------------------------");
        LogUtil.v("privateKey = " + privateKey);
        LogUtil.v("publicKey = " + publicKey);
        
        String source = "message DIGEST";
        String userId = "ALICE123@YAHOO.COM";
        testSM2(privateBytes, publicBytes, userId, source);
    }
    
    /**
     * 测试，加密、解密、签名、验签过程
     */
    private void testSM2(byte[] privateBytes, byte[] publicBytes, String userId, String source) {
        try {
            byte[] cipherBytes = SM2Utils.encrypt(publicBytes, source.getBytes());
            LogUtil.v("加密：" + Base64.encodeToString(cipherBytes, Base64.NO_WRAP));
            
            byte[] plainBytes = SM2Utils.decrypt(privateBytes, cipherBytes);
            String plainText = null == plainBytes ? null : new String(plainBytes);
            LogUtil.v("解密：" + plainText + ", result = " + source.equals(plainText));
            
            byte[] signBytes = SM2Utils.sign(userId.getBytes(), privateBytes, source.getBytes());
            LogUtil.v("签名：" + Base64.encodeToString(signBytes, Base64.NO_WRAP));
            
            boolean signResult = SM2Utils.verifySign(userId.getBytes(), publicBytes, source.getBytes(), signBytes);
            LogUtil.v("验签：" + signResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
