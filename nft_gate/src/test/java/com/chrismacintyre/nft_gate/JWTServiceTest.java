package com.chrismacintyre.nft_gate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.*;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
class JWTServiceTest {

    public JWTService jwtService = new JWTService("ZmFrZXRlc3RzZWNyZXRrZXlfZmFrZXRlc3RrZXk");

    @Test
    void getTimeZone() {
        ZonedDateTime GMT = ZonedDateTime.now(ZoneId.of("Europe/London"));
        assertThat(ZonedDateTime.now()).isAfterOrEqualTo(GMT);
    }

    @Test
    void testZones() {
        Instant instant = Instant.now();
        ZonedDateTime GMT = instant.atZone(ZoneId.of("Europe/London"));
        ZonedDateTime CET = instant.atZone(ZoneId.of("Europe/Berlin"));
        ZonedDateTime EST = instant.atZone(ZoneId.of("America/New_York"));
        assertThat(GMT.toInstant()).isEqualTo(CET.toInstant());
        assertThat(GMT.toInstant()).isEqualTo(EST.toInstant());
        assertThat(EST.getOffset().getTotalSeconds())
                .isLessThan(GMT.getOffset().getTotalSeconds());
        assertThat(GMT.getOffset().getTotalSeconds())
                .isLessThan(CET.getOffset().getTotalSeconds());
    }

    @Test
    void testTimeLimitGMT() {
        LocalDate today = LocalDate.now();
        ZonedDateTime span1 = today.atTime(20,0).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime span2 = today.atTime(22,0).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime GMT1 = today.atTime(19,59).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime GMT2 = today.atTime(22, 1).atZone(ZoneId.of("Europe/London"));
        assertThat(GMT1).isBefore(span1);
        assertThat(GMT2).isAfter(span2);
    }

    @Test
    void testTimeLimitGLOBAL() {
        LocalDate today = LocalDate.now();
        ZonedDateTime t1 = today.atTime(20,0).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime t2 = today.atTime(22,0).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime EST = today.atTime(15,1).atZone(ZoneId.of("America/New_York"));
        ZonedDateTime CET = today.atTime(21,0).atZone(ZoneId.of("America/New_York"));
        assertThat(EST).isAfter(t1);
        assertThat(CET).isAfter(t2);
    }

    @Test
    void testCreateJWT() {
        byte[] keyBytes = "ZmFrZXRlc3RzZWNyZXRrZXlfZmFrZXRlc3RrZXk".getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String testJWT = Jwts.builder()
                .setIssuer("0xd84Bc2506E099330ed0f17CceaFeB3FFe87Ab454")
                //        .setIssuer(tokenService.getContractAddress())
                .claim("owner", "d0a8305f634f5317f703eba1cf62807539006083e0")
                .setIssuedAt(Date.from(Instant.now()))
                .notBefore(Date.from(jwtService.getStartTime().toInstant()))
                .setExpiration(Date.from(jwtService.getEndTime().toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        System.out.println(testJWT);
    }

    @Test
    void testValidity() {
        byte[] keyBytes = "ZmFrZXRlc3RzZWNyZXRrZXlfZmFrZXRlc3RrZXk".getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        Base64.Decoder decoder = Base64.getUrlDecoder();
    //  Remove nbf condition from test tokens to test JWT structure
        String token = Jwts.builder()
                .setIssuer("0xd84Bc2506E099330ed0f17CceaFeB3FFe87Ab454")
                //        .setIssuer(tokenService.getContractAddress())
                .claim("owner", "d0a8305f634f5317f703eba1cf62807539006083e0")
                .setExpiration(Date.from(jwtService.getEndTime().toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String token2 = Jwts.builder()
                .setIssuer("0xd84Bc2506E099330ed0f17CceaFeB3FFe87Ab454")
                //        .setIssuer(tokenService.getContractAddress())
                .claim("owner", "a8c42a2837e51c735f446842f735a8df8443c3d9b8")
                .setExpiration(Date.from(jwtService.getEndTime().toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String[] arr = token.split("\\.");
        String[] arr2 = token2.split("\\.");
        System.out.println(arr[0]);
        System.out.println(arr2[0]);

        String fakeHeaderB64 = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"none\"}".getBytes());
        token2 = fakeHeaderB64 + "." + arr2[1] + "." + arr2[2];
        String header = new String(decoder.decode(arr[0]));
        String header2 = new String(decoder.decode(fakeHeaderB64));

        System.out.println(header);
        System.out.println(header2);
        if (header.contains("none")) { System.out.println("INVALID");}
        if (header2.contains("none")) { System.out.println("INVALID");}
        try {
            Claims res = Jwts
                    .parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Claims res2 = Jwts
                    .parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token2)
                    .getBody();
            System.out.println(res.toString());
            System.out.println(res2.toString());
        } catch (UnsupportedJwtException uje) {
            uje.printStackTrace();
        }
    }


}