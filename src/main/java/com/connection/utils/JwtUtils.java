package com.connection.utils;

import com.connection.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "SECRET_KEY_JHSBHB239&(#$1#@$_R*R@HC$*NFYM@#WHBFBWENNCWNN";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(removeBearerIfExist(token)).getSubject();
    }

    public Claims getClaimFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("JWT parsing failed", e);
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(removeBearerIfExist(token)).get("username", String.class);
    }

    private String removeBearerIfExist(String token) {
        if (token.contains(Constants.BEARER)) {
            token = token.replace(Constants.BEARER, "");
        }
        return token;
    }
}