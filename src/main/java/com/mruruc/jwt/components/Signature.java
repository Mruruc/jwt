package com.mruruc.jwt.component;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Signature {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final Mac mac;

    static {
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize HMAC SHA-256 algorithm.", e);
        }
    }

    public static String sign(String header, String payload, String key) throws InvalidKeyException {
        byte[] hashSignatureAsBytes = getSignedHash(header, payload, key);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashSignatureAsBytes);
    }

    private static byte[] getSignedHash(String header, String payload, String key) throws InvalidKeyException {
        SecretKeySpec secretKeySpec = createKeySpec(key);
        mac.init(secretKeySpec);
        String dataToSign = header + "." + payload;
        return mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
    }

    private static SecretKeySpec createKeySpec(String key) {
        return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
    }

}
