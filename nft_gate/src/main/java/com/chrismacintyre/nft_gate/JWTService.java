package com.chrismacintyre.nft_gate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    public JWTService(@Value("${security.jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private final Key key;
    private LocalDate today = LocalDate.now();
    private final ZonedDateTime startTime = today.atTime(20, 0).atZone(ZoneId.of("Europe/London"));
    private final ZonedDateTime endTime = today.atTime(22, 0).atZone(ZoneId.of("Europe/London"));

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public ZonedDateTime getStartTime() { return startTime; }
    public ZonedDateTime getEndTime() { return endTime; }

    public String createJWT(String account) {
        return Jwts.builder()
                .setIssuer("0xd84Bc2506E099330ed0f17CceaFeB3FFe87Ab454")
        //        .setIssuer(tokenService.getContractAddress())
                .claim("owner", account)
                .setIssuedAt(Date.from(Instant.now()))
                .notBefore(Date.from(startTime.toInstant()))
                .setExpiration(Date.from(endTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private ZonedDateTime extractExpiration(String token) {
        return ZonedDateTime.from(extractClaim(token, Claims::getExpiration).toInstant());
    }

    public boolean isExpired(String token) {
        return extractExpiration(token).isAfter(ZonedDateTime.from(today));
    }

    public boolean isValid(String token) {
        return !extractAllClaims(token).isEmpty() && !isExpired(token);
    }

    private Key getSigningKey() {
        return key;
    }
}

