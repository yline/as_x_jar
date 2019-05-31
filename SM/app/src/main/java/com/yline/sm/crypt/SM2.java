package com.yline.sm.crypt;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement.Fp;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class SM2 {
    // 测试参数
    /*public static final String[] ecc_param = {
            "8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3",
            "787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498",
            "63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A",
            "8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7",
            "421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D",
            "0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2"
    };*/
    
    // 正式参数
	public static final String[] ecc_param = {
		"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
		"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",
		"28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",
		"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",
		"32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",
		"BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0"
	};
    
    public final BigInteger ecc_p;
    public final BigInteger ecc_a;
    public final BigInteger ecc_b;
    public final BigInteger ecc_n;
    public final BigInteger ecc_gx;
    public final BigInteger ecc_gy;
    
    public final ECCurve ecc_curve;
    public final ECPoint ecc_point_g;
    
    public final ECDomainParameters ecc_bc_spec;
    public final ECKeyPairGenerator ecc_key_pair_generator;
    
    public final ECFieldElement ecc_gx_fieldelement;
    public final ECFieldElement ecc_gy_fieldelement;
    
    public SM2() {
        this.ecc_p = new BigInteger(ecc_param[0], 16);
        this.ecc_a = new BigInteger(ecc_param[1], 16);
        this.ecc_b = new BigInteger(ecc_param[2], 16);
        this.ecc_n = new BigInteger(ecc_param[3], 16);
        this.ecc_gx = new BigInteger(ecc_param[4], 16);
        this.ecc_gy = new BigInteger(ecc_param[5], 16);
        
        this.ecc_gx_fieldelement = new Fp(this.ecc_p, this.ecc_gx);
        this.ecc_gy_fieldelement = new Fp(this.ecc_p, this.ecc_gy);
        
        this.ecc_curve = new ECCurve.Fp(this.ecc_p, this.ecc_a, this.ecc_b);
        this.ecc_point_g = new ECPoint.Fp(this.ecc_curve, this.ecc_gx_fieldelement, this.ecc_gy_fieldelement);
        
        this.ecc_bc_spec = new ECDomainParameters(this.ecc_curve, this.ecc_point_g, this.ecc_n);
        
        this.ecc_key_pair_generator = new ECKeyPairGenerator();
        this.ecc_key_pair_generator.init(new ECKeyGenerationParameters(this.ecc_bc_spec, new SecureRandom()));
    }
    
    public byte[] sm2GetZ(byte[] userId, ECPoint userKey) {
        SM3Digest sm3 = new SM3Digest();
        
        int len = userId.length * 8;
        sm3.update((byte) (len >> 8 & 0xFF));
        sm3.update((byte) (len & 0xFF));
        sm3.update(userId, 0, userId.length);
        
        byte[] p = bigInteger2Bytes(ecc_a);
        sm3.update(p, 0, p.length);
        
        p = bigInteger2Bytes(ecc_b);
        sm3.update(p, 0, p.length);
        
        p = bigInteger2Bytes(ecc_gx);
        sm3.update(p, 0, p.length);
        
        p = bigInteger2Bytes(ecc_gy);
        sm3.update(p, 0, p.length);
        
        p = bigInteger2Bytes(userKey.getX().toBigInteger());
        sm3.update(p, 0, p.length);
        
        p = bigInteger2Bytes(userKey.getY().toBigInteger());
        sm3.update(p, 0, p.length);
        
        byte[] md = new byte[sm3.getDigestSize()];
        sm3.doFinal(md, 0);
        return md;
    }
    
    public byte[] sm2Sign(byte[] md, BigInteger privateKey) {
        BigInteger e = new BigInteger(1, md);
        BigInteger k = null;
        ECPoint kp = null;
        BigInteger r = null;
        BigInteger s = null;
        do {
            do {
                // 正式环境
                AsymmetricCipherKeyPair keypair = ecc_key_pair_generator.generateKeyPair();
                ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) keypair.getPrivate();
                ECPublicKeyParameters ecpub = (ECPublicKeyParameters) keypair.getPublic();
                k = ecpriv.getD();
                kp = ecpub.getQ();
                
                // 国密规范测试 随机数k
                /*String kS = "6CB28D99385C175C94F94E934817663FC176D925DD72B727260DBAAE1FB2F96F";
                k = new BigInteger(kS, 16);
                kp = this.ecc_point_g.multiply(k);*/
                
                // System.out.println("计算曲线点X1: " + kp.getX().toBigInteger().toString(16));
                // System.out.println("计算曲线点Y1: " + kp.getY().toBigInteger().toString(16));
                
                // r
                r = e.add(kp.getX().toBigInteger());
                r = r.mod(ecc_n);
            } while (r.equals(BigInteger.ZERO) || r.add(k).equals(ecc_n));
            
            // (1 + dA)~-1
            BigInteger da_1 = privateKey.add(BigInteger.ONE);
            da_1 = da_1.modInverse(ecc_n);
            
            // s
            s = r.multiply(privateKey);
            s = k.subtract(s).mod(ecc_n);
            s = da_1.multiply(s).mod(ecc_n);
        } while (s.equals(BigInteger.ZERO));
    
        // System.out.println("r: " + r.toString(16));
        // System.out.println("s: " + s.toString(16));
        
        ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(new DERInteger(r));
        v2.add(new DERInteger(s));
        
        return new DERSequence(v2).getDEREncoded();
    }
    
    public boolean sm2Verify(byte md[], ECPoint publicKey, BigInteger r, BigInteger s) {
        // System.out.println("r: " + r.toString(16));
        // System.out.println("s: " + s.toString(16));
        
        BigInteger e = new BigInteger(1, md);
        BigInteger t = r.add(s).mod(ecc_n);
        
        if (t.equals(BigInteger.ZERO)) {
            return false;
        } else {
            ECPoint x1y1 = ecc_point_g.multiply(s);
            // System.out.println("计算曲线点X0: " + x1y1.getX().toBigInteger().toString(16));
            // System.out.println("计算曲线点Y0: " + x1y1.getY().toBigInteger().toString(16));
            
            x1y1 = x1y1.add(publicKey.multiply(t));
            // System.out.println("计算曲线点X1: " + x1y1.getX().toBigInteger().toString(16));
            // System.out.println("计算曲线点Y1: " + x1y1.getY().toBigInteger().toString(16));
            BigInteger r2 = e.add(x1y1.getX().toBigInteger()).mod(ecc_n);
            // System.out.println("r2: " + r2.toString(16));
            return r.equals(r2);
        }
    }
    
    /**
     * 大数字转换字节流（字节数组）型数据
     *
     * @param value 大数
     * @return 32位byte
     */
    public static byte[] bigInteger2Bytes(BigInteger value) {
        if (null == value) {
            return null;
        }
        
        byte[] valueBytes = value.toByteArray();
        if (valueBytes.length > 32) {
            byte[] result = new byte[32];
            System.arraycopy(valueBytes, valueBytes.length - 32, result, 0, 32);
            return result;
        } else if (valueBytes.length == 32) {
            return valueBytes;
        } else {
            byte[] result = new byte[32];
            Arrays.fill(result, (byte) 0);
            System.arraycopy(valueBytes, 0, result, 32 - valueBytes.length, valueBytes.length);
            return result;
        }
    }
}
