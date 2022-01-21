package com.jsy.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;

public class JasyptTest {
    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");  // 加密的算法，这个算法是默认的
        config.setPassword("arli");               // 加密的密钥
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "true";      //加密的数据
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }

    @Test
    public void testDe() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("arli");
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "eZH3eSvrFo8d9QlQzr7VENX6yznWQ5nWLBAvbFBfRuOIiYw1QYHNFQnnsBHPLP10hfk+kEJT+lAlTgguYV02oKjK3PTzjSgnroVtULFPx+gpLgeIRYRUBJZusILeFQNk9aRS1dDXShl6udQO//xW90GPNBJJi8+JtV1Br61etdZodsAY3lJ6KA==";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }
}
