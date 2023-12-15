package com.example.example100.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.example100.user.entity.User;
import com.example.example100.user.model.UserLoginToken;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JWTUtils {
    private final String TOKEN_KEY = "Example100";
    private final String CLAIM_ID = "user_id";

    public static UserLoginToken createToken(User user) {
        if (user == null) {
            return null;
        }

        String token =  JWT.create()
                .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMonths(1)))
                .withClaim(CLAIM_ID, user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512(TOKEN_KEY.getBytes()));

        return UserLoginToken.builder()
                .token(token)
                .build();
    }

    public static String getIssuer(String token) {
        return JWT.require(Algorithm.HMAC512(TOKEN_KEY))
                .build()
                .verify(token)
                .getIssuer();
    }
}
