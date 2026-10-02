package com.campushire.app.config;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginRateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 60;

    private final ConcurrentHashMap<String, AttemptRecord> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String email) {
        AttemptRecord record = attempts.get(email.toLowerCase());
        if (record == null) return false;

        boolean windowExpired = Instant.now().getEpochSecond() - record.windowStart > WINDOW_SECONDS;
        if (windowExpired) {
            attempts.remove(email.toLowerCase());
            return false;
        }
        return record.count.get() >= MAX_ATTEMPTS;
    }

    public void recordFailedAttempt(String email) {
        attempts.compute(email.toLowerCase(), (key, record) -> {
            if (record == null || Instant.now().getEpochSecond() - record.windowStart > WINDOW_SECONDS) {
                return new AttemptRecord();
            }
            record.count.incrementAndGet();
            return record;
        });
    }

    public void clearAttempts(String email) {
        attempts.remove(email.toLowerCase());
    }

    private static class AttemptRecord {
        final long windowStart = Instant.now().getEpochSecond();
        final AtomicInteger count = new AtomicInteger(1);
    }
}