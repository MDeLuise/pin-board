package com.github.mdeluise.pinboard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private final String issuer;
    private final String secretKey;
    private final Integer expireAfterDays;


    @Autowired
    public JwtTokenUtil(@Value("${jwt.issuer}") String issuer,
                        @Value("${jwt.secretKey}") String secretKey,
                        @Value("${jwt.tokenExpirationAfterDays}") Integer expireAfterDays) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.expireAfterDays = expireAfterDays;
    }


    public JwtTokenInfo generateAccessToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                          .withIssuer(issuer)
                          .withSubject(userDetails.getUsername())
                          .withIssuedAt(new Date())
                          .withExpiresAt(java.sql.Date.valueOf(
                              LocalDate.now().plusDays(expireAfterDays)))
                          .sign(algorithm);
        Date expireOn = java.sql.Date.valueOf(LocalDate.now().plusDays(expireAfterDays));
        return new JwtTokenInfo(token, expireOn);
    }


    public boolean isValid(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        try {
            JWT.require(algorithm)
               .withIssuer(issuer)
               .build()
               .verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }


    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }
}
