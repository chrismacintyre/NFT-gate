package com.chrismacintyre.nft_gate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTServiceTest {

    @Test
    void getTimeZone() {
        ZonedDateTime GMT = ZonedDateTime.now(ZoneId.of("Europe/London"));
        assertThat(ZonedDateTime.now()).isAfterOrEqualTo(GMT);
    }

    @Test
    void testZones() {
        Instant now = Instant.now();
        ZonedDateTime GMT = now.atZone(ZoneId.of("Europe/London"));
        ZonedDateTime CET = now.atZone(ZoneId.of("Europe/Berlin"));
        ZonedDateTime EST = now.atZone(ZoneId.of("America/New_York"));
        assertThat(GMT.toInstant()).isEqualTo(CET.toInstant());
        assertThat(GMT.toInstant()).isEqualTo(EST.toInstant());
        assertThat(GMT.toLocalTime()).isBefore(CET.toLocalTime());
        assertThat(GMT.toLocalTime()).isAfter(EST.toLocalTime());
    }

    @Test
    void testTimeLimitGMT() {
        LocalDate today = LocalDate.now();
        ZonedDateTime span1 = today.atTime(20,0).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime span2 = today.atTime(22,0).atZone(ZoneId.of("Europe/London"));
        ZonedDateTime GMT =ZonedDateTime.now(ZoneId.of("Europe/London"));
        assertThat(GMT).isBefore(span1);
        assertThat(GMT).isAfter(span2);
    }

    @Test
    void createJWT() {

    }

}