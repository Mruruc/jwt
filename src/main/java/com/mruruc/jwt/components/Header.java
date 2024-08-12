package com.mruruc.jwt.component;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <h1>Step 1:</h1>
 * <h3> Specify the alg name and token type.</h3>
 *
 * <h1>Step 2: </h1>
 * <h3>Convert to JSON string.</h3>
 *
 * <h1>Step 3:</h1>
 * <h3> Encode base65 url and return as string. </h3>
 */

public final class Header {
    private static final String alg = "HS256";
    private static final String type = "JWT";

    Header(){}

    public static String createHeader() {
        String jsonHeader = getJsonString();
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(jsonHeader.getBytes(StandardCharsets.UTF_8));
    }

    private static String getJsonString() {
        return String.format(
                "{\"alg\":\"%s\",\"type\":\"%s\"}"
                , alg, type);
    }
}
