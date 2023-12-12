package com.example.example100.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.example100.user.entity.User;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JWTUtils {
    private final String TOKEN_KEY = "Example100";
    private final String CLAIM_ID = "user_id";

    public static String createToken(User user) {
        return JWT.create()
                .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .withClaim(CLAIM_ID, user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512(TOKEN_KEY.getBytes()));
    }

    public static String getIssuer(String token) {
        return JWT.require(Algorithm.HMAC512(TOKEN_KEY))
                .build()
                .verify(token)
                .getIssuer();
    }
}
