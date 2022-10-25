package com.oliver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

import static com.oliver.configuration.constant.JWT_EXPIRATION_TIME;
import static com.oliver.configuration.constant.JWT_KEY;

/**
 * JWT util class
 */
public class JWTUtil {
    /**
     * Creates JWT with given subject.
     *
     * @param subject The data needed to be stored in JWT.
     * @return Returns a JWT string.
     */
    public static String createJWT (String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, UUID.randomUUID().toString());
        return builder.compact();
    }

    /**
     * Creates JWT with given subject and expiration time.
     *
     * @param subject The data needed to be stored in JWT.
     * @param ttlMillis The expiration time of JWT.
     * @return Returns a JWT string.
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, UUID.randomUUID().toString());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder (String subject, Long expirationTime, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if(expirationTime == null){
            expirationTime = Long.valueOf(JWT_EXPIRATION_TIME);
        }

        long expMillis = nowMillis + expirationTime;
        Date expDate = new Date(expMillis);

        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("com.oliver")
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, JWT_KEY)
                .setExpiration(expDate);
    }

    /**
     * Parses the given JWT.
     *
     * @param jwt A JWT needed to be parsed.
     * @return Returns the body of the given JWT.
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(JWT_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
