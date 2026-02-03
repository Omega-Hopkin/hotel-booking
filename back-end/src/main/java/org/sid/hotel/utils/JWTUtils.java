package org.sid.hotel.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.sql.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JWTUtils {
    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; //pour 7 jours
    
    private final SecretKey Key;

    public JWTUtils() {
        String secreteString = "eyJpc3MiOiJodHRwczovL2V4YW1wbGUuYXV0aDAuY29tLyIsImF1ZCI6Imh0dHBzOi8vYXBpLmV4YW1wbGUuY29tL2NhbGFuZGFyL3YxLyIsInN1YiI6InVzcl8xMjMiLCJpYXQiOjE0NT";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date(System.currentTimeMillis())); //System.currentTimeMillis() ajouté
    }
}