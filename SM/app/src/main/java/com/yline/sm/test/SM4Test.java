package com.yline.sm.test;

import com.yline.sm.crypt.SM4Utils;
import com.yline.utils.LogUtil;

public class SM4Test {
    
    public void testSM4() {
        String sourceText = "abcdefghi";
        
        byte[] keyBytes = SM4Utils.createSM4Key();
        
        LogUtil.v("ECB模式");
        String cipherText = SM4Utils.encryptECB(sourceText, keyBytes);
        LogUtil.v("密文：" + cipherText);
        
        String plainText = SM4Utils.decryptECB(cipherText, keyBytes);
        LogUtil.v("校验：" + sourceText.equals(plainText));
        
        LogUtil.v("CBC模式");
        cipherText = SM4Utils.encryptCBC(sourceText, keyBytes);
        LogUtil.v("密文：" + cipherText);
        
        plainText = SM4Utils.decryptCBC(cipherText, keyBytes);
        LogUtil.v("校验：" + sourceText.equals(plainText));
    }
}
