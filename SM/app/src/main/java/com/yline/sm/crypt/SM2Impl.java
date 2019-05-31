package com.yline.sm.crypt;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;

public class SM2Impl {
    public SM2KeyPair genKeyPair() {
        SM2 sm2 = new SM2();
        
        AsymmetricCipherKeyPair keyPair = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ec_private = (ECPrivateKeyParameters) keyPair.getPrivate();
        if (ec_private.getD().toByteArray().length != 32) {
            return null;
        }
        
        ECPublicKeyParameters ec_public = (ECPublicKeyParameters) keyPair.getPublic();
        return new SM2KeyPair(ec_private.getD(), ec_public.getQ());
    }
    
    public byte[] encrypt(byte[] sourceBytes, byte[] publicKeyBytes) throws IOException {
        if (null == sourceBytes || sourceBytes.length == 0) {
            return null;
        }
        
        if (null == publicKeyBytes || publicKeyBytes.length == 0) {
            return null;
        }
        
        SM2 sm2 = new SM2();
        
        SM2Cipher cipher = new SM2Cipher();
        ECPoint publicKeyPoint = sm2.ecc_curve.decodePoint(publicKeyBytes);
        ECPoint cipherPoint = cipher.init_enc(sm2, publicKeyPoint);
        cipher.encrypt(sourceBytes);
        
        byte[] cipher3 = new byte[32];
        cipher.doFinal(cipher3);
        
        DERInteger x = new DERInteger(cipherPoint.getX().toBigInteger());
        DERInteger y = new DERInteger(cipherPoint.getY().toBigInteger());
        DEROctetString derDig = new DEROctetString(cipher3);
        DEROctetString derEnc = new DEROctetString(sourceBytes);
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(x);
        v.add(y);
        v.add(derDig);
        v.add(derEnc);
        DERSequence seq = new DERSequence(v);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DEROutputStream dos = new DEROutputStream(bos);
        dos.writeObject(seq);
        return bos.toByteArray();
    }
    
    public byte[] decrypt(byte[] encryptedBytes, byte[] privateKeyBytes) throws IOException {
        if (privateKeyBytes == null || privateKeyBytes.length == 0) {
            return null;
        }
        
        if (encryptedBytes == null || encryptedBytes.length == 0) {
            return null;
        }
        
        SM2 sm2 = new SM2();
        BigInteger privateKey = new BigInteger(1, privateKeyBytes);
        
        ByteArrayInputStream bis = new ByteArrayInputStream(encryptedBytes);
        ASN1InputStream dis = new ASN1InputStream(bis);
        DERObject derObj = dis.readObject();
        ASN1Sequence asn1 = (ASN1Sequence) derObj;
        DERInteger x = (DERInteger) asn1.getObjectAt(0);
        DERInteger y = (DERInteger) asn1.getObjectAt(1);
        ECPoint ecPoint = sm2.ecc_curve.createPoint(x.getValue(), y.getValue(), true);
        
        SM2Cipher cipher = new SM2Cipher();
        cipher.init_dec(privateKey, ecPoint);
        
        DEROctetString data = (DEROctetString) asn1.getObjectAt(3);
        encryptedBytes = data.getOctets();
        cipher.decrypt(encryptedBytes);
        byte[] c3 = new byte[32];
        cipher.doFinal(c3);
        
        return encryptedBytes;
    }
    
    public byte[] sign(byte[] userId, byte[] privateKeyBytes, byte[] sourceBytes) throws IOException {
        if (privateKeyBytes == null || privateKeyBytes.length == 0) {
            return null;
        }
        
        if (sourceBytes == null || sourceBytes.length == 0) {
            return null;
        }
        
        SM2 sm2 = new SM2();
        BigInteger privateKey = new BigInteger(privateKeyBytes);
        ECPoint privatePoint = sm2.ecc_point_g.multiply(privateKey);
        // System.out.println("椭圆曲线点X: " + privatePoint.getX().toBigInteger().toString(16));
        // System.out.println("椭圆曲线点Y: " + privatePoint.getY().toBigInteger().toString(16));
        
        SM3Digest digest = new SM3Digest();
        byte[] z = sm2.sm2GetZ(userId, privatePoint);
        
        digest.update(z, 0, z.length);
        digest.update(sourceBytes, 0, sourceBytes.length);
        byte[] md = new byte[32];
        digest.doFinal(md, 0);
        // System.out.println("SM3摘要值: " + md);
        
        return sm2.sm2Sign(md, privateKey);
    }
    
    public boolean verifySign(byte[] userId, byte[] publicKeyBytes, byte[] sourceBytes, byte[] signBytes) throws IOException {
        if (publicKeyBytes == null || publicKeyBytes.length == 0) {
            return false;
        }
        
        if (sourceBytes == null || sourceBytes.length == 0) {
            return false;
        }
        
        SM2 sm2 = new SM2();
        ECPoint publicKey = sm2.ecc_curve.decodePoint(publicKeyBytes);
        
        SM3Digest digest = new SM3Digest();
        byte[] z = sm2.sm2GetZ(userId, publicKey);
        digest.update(z, 0, z.length);
        digest.update(sourceBytes, 0, sourceBytes.length);
        
        byte[] md = new byte[32];
        digest.doFinal(md, 0);
        
        ByteArrayInputStream bis = new ByteArrayInputStream(signBytes);
        ASN1InputStream dis = new ASN1InputStream(bis);
        DERObject derObj = dis.readObject();
        
        Enumeration<DERInteger> e = ((ASN1Sequence) derObj).getObjects();
        BigInteger r = e.nextElement().getValue();
        BigInteger s = e.nextElement().getValue();
        
        return sm2.sm2Verify(md, publicKey, r, s);
    }
    
    public static class SM2KeyPair {
        private BigInteger privateKey;
        private ECPoint publicKey;
        
        private SM2KeyPair(BigInteger privateKey, ECPoint publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
        
        public BigInteger getPrivateKey() {
            return privateKey;
        }
        
        public ECPoint getPublicKey() {
            return publicKey;
        }
    }
}
