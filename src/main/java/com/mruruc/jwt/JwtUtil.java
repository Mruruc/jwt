package com.mruruc.jwt;


import com.mruruc.jwt.component.Header;
import com.mruruc.jwt.component.Payload;
import com.mruruc.jwt.component.Signature;
import com.mruruc.jwt.exceptions.JwtException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private JwtUtil() {
    }

    public static String createJwt(Payload payload, String key) throws InvalidKeyException {
        String header = Header.createHeader();
        String claims = payload.createClaimsAsBase64URLString();
        String signature = Signature.sign(header, claims, key);
        return String.join(".",header,claims,signature);
    }

    public static Map<String, String> parseClaims(String jwtToken) {
        String[] urlEncodedJwtArray = getUrlEncodedJwtArray(jwtToken);
        String payload = urlEncodedJwtArray[1];
        return getPayload(payload);
    }

    public static boolean verifyToken(String jwtToken, String secretKey) throws InvalidKeyException {
        String[] urlEncodedJwtArray = getUrlEncodedJwtArray(jwtToken);
        return verify(urlEncodedJwtArray[0], urlEncodedJwtArray[1], secretKey, urlEncodedJwtArray[2]);
    }

    private static boolean verify(String... args) throws InvalidKeyException {
        Map<String, String> payload = getPayload(args[1]);

        LocalDateTime expDate = stringToDate(payload.get("exp"));
        if (LocalDateTime.now().isAfter(expDate)) throw new JwtException("Token is expired");

        boolean generatedSignature = Signature.sign(args[0], args[1], args[2]).equals(args[3]);
        if (!generatedSignature) throw new JwtException("Invalid JWT token.");
        return true;
    }


    private static Map<String, String> getPayload(String urlEncodedPayload) {
        byte[] decodedBytes = Base64.getDecoder().decode(urlEncodedPayload);
        return getClaims(decodedBytes);
    }

    private static HashMap<String, String> getClaims(byte[] decodedBytes) {
        String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] parts = jsonString
                .trim()
                .substring(1, jsonString.length() - 1)
                .split(",");

        HashMap<String, String> claims = new HashMap<>();
        for (String part : parts) {
            String[] split = part.split(":", 2);

            String key = split[0].trim().replaceAll("^\"|\"$", "");
            String value = split[1].trim().replaceAll("^\"|\"$", "");

            claims.put(key, value);
        }
        return claims;
    }

    private static LocalDateTime stringToDate(String strDate) {
        return LocalDateTime.ofEpochSecond(Long.parseLong(strDate) / 1000, 0, ZoneOffset.UTC);
    }

    private static String[] getUrlEncodedJwtArray(String jwtToken) {
        String[] urlEncodedJwtArray = jwtToken
                .trim()
                .replaceAll("\\s+", "")
                .split("\\.");
        if (urlEncodedJwtArray.length != 3) throw new JwtException("Invalid JWT token format.");
        return urlEncodedJwtArray;
    }
}
