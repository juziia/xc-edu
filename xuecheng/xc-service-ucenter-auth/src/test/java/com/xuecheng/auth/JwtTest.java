package com.xuecheng.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


//@SpringBootTest
//@RunWith(SpringRunner.class)
public class JwtTest {

    @Test
    public void testPublishToken(){
        String keystore = "xc.keystore";       // 密码库
        String keystore_password = "xuechengkeystore";  // 密钥库密码

        // 加载密钥库
        ClassPathResource resource = new ClassPathResource(keystore);

        // 密钥库工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        String alias = "xckey";
        String key_password = "xuecheng";
        // 根据别名和密码获取公钥与私钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        RSAPrivateKey paivateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey aPublic = (RSAPublicKey) keyPair.getPublic();
        System.out.println(aPublic.getAlgorithm());
        String jsonStr = "{name:zhangsan}";
        //
        Jwt jwt = JwtHelper.encode(jsonStr, new RsaSigner(paivateKey));
        String claims = jwt.getClaims();
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
        System.out.println(claims);
    }




    @Test
    public void testJwtToken(){
       // {access_token=, token_type=bearer, refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiJjYTkwMTcyOS04ZmI3LTQ1YTctYjU4MC1hYjQ0ZGY3ZjkwODQiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU4NjM5OTE4MCwianRpIjoiODZlZjNjMGYtNWNiMi00YzkyLTg3ZmItNGNiZTExNTJiYTgyIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.F_qEISO3YAVgrqQTSyKd8TTikctLoRK8ZUJ8qbMPvTkWliz7z7bzPfAE-MerTHFSrZOSmcZqJxUoOIh3J3fhNLzXtU-2qRnxyn50M4_5b3sgVb6b-s4nUC5Tk_YeumRHWSUfQ-EvZyaJsl44BqITNWN5MIJH02IXm3eJy1Qic7qdaVXbIsULLOl6_W1YjefdUrJryryD2txvZ5XMJqZGUnFm7VTJ0KdwvFao7TkDEdMypYLgvKw04N7B1R3jp4vyRNDgB_vDJNQofWL2GdU2KzjXFzKCPaar46TqklZtx0RyzB0O7oqLpyua3FDVA7_cfFAXNrpcFjkc_jdQkaFj1A, expires_in=43199, scope=app, jti=ca901729-8fb7-45a7-b580-ab44df7f9084}

        // token
        //String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.e25hbWU6emhhbmdzYW59.JntYZ4EdaifnLBBBgcjb3S5t_LiNAol3DVrh2sdtt8KALWZDfLVZaRYR7fhxv3jvDVEq7AebbeieZWZboHlfiKgdl3fNGT47yMxkeieDQAi2nNTRehEKSYyv1jOFV5JWnC0ku_3pbVQuTZnvXYHsNqLdgPNh6QfOxo2bByUWAjetmr5YVprGqL4EdiF0HwWbvyFMpeOl2SABLhv5XrGDJZAxMssa4Xw2nJV180FzyM1ZBQ0Lto9EzOLsJDK39ss1GR8Q1TRSrVswyZ3i8-GbzZsjxI_9I4EwTpF9Tell-_aqUWN7w60ZN5iVFQICZJQFxY0mpsJ_akvEhVcqvHqLFg";
        //String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU4NjM5OTE4MCwianRpIjoiY2E5MDE3MjktOGZiNy00NWE3LWI1ODAtYWI0NGRmN2Y5MDg0IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.HNaWNNIr1ozRxBVMeN9MMckc_fUl2tLQKPBjGyspBy275Mx27A7tvTHjNbnP5QT5YgzKzkTZFC1eO0l2EuxkyKcMF3-pAHaAbVgnJRT-XGAAvoHQRA_R2CFAgJzI8GpjKzjbwXVwVdtLkvO7Z8bpsi-Y5IFCU64SNps_VyLSjJ4tj5apuJQ1ylBxpWkj5VEeYq9iCKgZAPQBATT5nQU4KwEJa_bcmAwLfrWERiMv04JXCRMDz53U1SnXPhiOPLyGSZyBpUnWW3gdyIIoKg3CMkKxnBUeUxWJmeWepSZ9If-t3-56LeBnSUlkB6oyIFj8IyRoOVzKMVEYQBsDTpWUEw";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1ODY1MjQwNTUsImF1dGhvcml0aWVzIjpbImNvdXJzZV9maW5kX3BpYyIsImNvdXJzZV9nZXRfYmFzZWluZm8iXSwianRpIjoiNjMzYzdiYzAtNmZjYi00MWVlLThhZDEtMjQ5ODExM2MyNWIzIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.eU85ZJPK5FPbUrvglmwl02WbY-oCXrbvSUUkjp7FCMPYRd1tovnKW_XmRs3SUL1WVBd_z3AShW45NDOldMzcf8fW7UfI2cCom5McfufecYOLwBQ_p_l6W7FHfNMmFfN-UHwCi6_7u74kC9i16swrMTGr8VF5MwNvTFA97rfoco-rgCjwIpZzzoQ3Bu39BVaVZd885_CAuOrUiel8-ooUFUeqrv26M4FlJPc-DHL5phq0adDLLuwosEFOWvT_-YO_BoI3LOBvKk-9-iMM3T0p4mATHS_IIC8hygWXz6s7-JiIgv2oWY591ZHnVrHYJjVnhDHjh0PUARBZOCn0tKjdNg";
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";

        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        String claims = jwt.getClaims();
        System.out.println(claims);


    }




}
