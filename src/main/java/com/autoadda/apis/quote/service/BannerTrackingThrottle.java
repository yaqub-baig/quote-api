package com.autoadda.apis.quote.service;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Counts a banner impression or click at most once per banner, per client IP, per window.
 * The tracking endpoints are public, so without this a script could inflate the numbers
 * (and use up a banner's impression limit) by calling them in a loop.
 *
 * In memory and bounded: when the map is full, expired entries are dropped first; if it is
 * still full, the event is not counted.
 */
@Component
public class BannerTrackingThrottle {

    static final int MAX_ENTRIES = 50_000;

    private final long windowMillis;
    private final Clock clock;
    private final Map<String, Long> lastCounted = new ConcurrentHashMap<>();

    @Autowired
    public BannerTrackingThrottle(@Value("${banner.tracking.dedupe-seconds:60}") long windowSeconds) {
        this(windowSeconds, Clock.systemUTC());
    }

    BannerTrackingThrottle(long windowSeconds, Clock clock) {
        this.windowMillis = windowSeconds * 1000L;
        this.clock = clock;
    }

    /** True when this event should be counted; false when it repeats one inside the window. */
    public boolean shouldCount(String kind, String bannerId, String clientIp) {
        long now = clock.millis();
        if (lastCounted.size() >= MAX_ENTRIES) {
            lastCounted.values().removeIf(t -> now - t >= windowMillis);
            if (lastCounted.size() >= MAX_ENTRIES) {
                return false;
            }
        }
        String key = kind + '|' + bannerId + '|' + clientIp;
        boolean[] counted = {false};
        lastCounted.compute(key, (k, last) -> {
            if (last == null || now - last >= windowMillis) {
                counted[0] = true;
                return now;
            }
            return last;
        });
        return counted[0];
    }
}
