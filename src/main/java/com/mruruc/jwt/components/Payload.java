package com.mruruc.jwt.component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Map;

public class Payload {
    private final long iat;
    private final long exp;
    private final Map<String, String> claims;

    public Payload(LocalDateTime iat, Map<String, String> claims, LocalDateTime exp) {
        this.iat = convertToEpochMillis(iat);
        this.exp = convertToEpochMillis(exp);

        if (this.exp <= this.iat) {
            throw new IllegalArgumentException("Expiration time must be after issued-at time.");
        }
        this.claims = claims;
    }

    private long convertToEpochMillis(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public String createClaimsAsBase64URLString() {
        String jsonPayload = getJSONPayload();
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(jsonPayload.getBytes(StandardCharsets.UTF_8));
    }

    private String getJSONPayload() {
        var payloadBuilder = new StringBuilder(String.format("{\"iat\":%d,", iat));

        int count = 0;
        for (Map.Entry<String, String> entry : claims.entrySet()) {
            payloadBuilder.append(String.format("\"%s\":\"%s\"", entry.getKey(), entry.getValue()));
            count++;
            if (count < claims.size()) {
                payloadBuilder.append(", ");
            }
        }

        payloadBuilder.append(String.format(",\"exp\":%d}", exp));
        return payloadBuilder.toString();
    }

    public static PayloadBuilder builder() {
        return new PayloadBuilder();
    }

    public static class PayloadBuilder {
        private LocalDateTime iat;
        private LocalDateTime exp;
        private Map<String, String> claims;

        public PayloadBuilder() {
        }

        public PayloadBuilder iat(LocalDateTime iat) {
            this.iat = iat;
            return this;
        }

        public PayloadBuilder exp(LocalDateTime exp) {
            this.exp = exp;
            return this;
        }

        public PayloadBuilder claims(Map<String, String> claims) {
            this.claims = claims;
            return this;
        }

        public Payload build() {
            return new Payload(iat, claims, exp);
        }

    }

}
