package com.mruruc;


import com.mruruc.jwt.JwtUtil;
import com.mruruc.jwt.component.Payload;
import com.mruruc.jwt.exceptions.JwtException;

import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final String KEY = "SECRET-KEY";

    public static void main(String[] args) throws InvalidKeyException {

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiresAt = issuedAt.plusHours(1);
        Map<String, String> claims = new HashMap<>();
        claims.put("username", "john@doe.com");
        claims.put("role", "USER");

        Payload payload = Payload.builder()
                .iat(issuedAt)
                .exp(expiresAt)
                .claims(claims)
                .build();

        String jwtToken = JwtUtil.createJwt(payload, KEY);
        System.out.println(jwtToken);

        try {
            JwtUtil.verifyToken(jwtToken, KEY);
            Map<String, String> claims2 = JwtUtil.parseClaims(jwtToken);
            System.out.println(claims2);
        } catch (JwtException exception) {
            exception.printStackTrace();
        }

    }
}

