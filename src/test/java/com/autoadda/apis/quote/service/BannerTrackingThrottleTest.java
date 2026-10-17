package com.autoadda.apis.quote.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class BannerTrackingThrottleTest {

    /** A clock the test moves by hand. */
    private static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-10-17T10:00:00Z");

        void advance(Duration d) {
            now = now.plus(d);
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }
    }

    @Test
    void repeatsInsideTheWindowAreNotCounted() {
        MutableClock clock = new MutableClock();
        BannerTrackingThrottle t = new BannerTrackingThrottle(60, clock);
        assertTrue(t.shouldCount("click", "b1", "203.0.113.7"));
        assertFalse(t.shouldCount("click", "b1", "203.0.113.7"));
        clock.advance(Duration.ofSeconds(59));
        assertFalse(t.shouldCount("click", "b1", "203.0.113.7"));
        clock.advance(Duration.ofSeconds(1));
        assertTrue(t.shouldCount("click", "b1", "203.0.113.7"));
    }

    @Test
    void differentBannerKindOrIpCountSeparately() {
        BannerTrackingThrottle t = new BannerTrackingThrottle(60, new MutableClock());
        assertTrue(t.shouldCount("click", "b1", "203.0.113.7"));
        assertTrue(t.shouldCount("impression", "b1", "203.0.113.7"));
        assertTrue(t.shouldCount("click", "b2", "203.0.113.7"));
        assertTrue(t.shouldCount("click", "b1", "203.0.113.8"));
    }

    @Test
    void staysBoundedAndRecoversAfterTheWindow() {
        MutableClock clock = new MutableClock();
        BannerTrackingThrottle t = new BannerTrackingThrottle(60, clock);
        for (int i = 0; i < BannerTrackingThrottle.MAX_ENTRIES; i++) {
            t.shouldCount("impression", "b" + i, "10.0.0.1");
        }
        assertFalse(t.shouldCount("impression", "new", "10.0.0.1"), "full map: not counted");
        clock.advance(Duration.ofSeconds(61));
        assertTrue(t.shouldCount("impression", "new", "10.0.0.1"), "expired entries are evicted");
    }
}
