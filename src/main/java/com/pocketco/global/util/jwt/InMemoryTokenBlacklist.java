package com.pocketco.global.util.jwt;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 서버를 재시작하면 BlackList 초기화 된다
@Component
public class InMemoryTokenBlacklist {
    private final Map<String, Long> store = new ConcurrentHashMap<>();

    public void add(String token, Instant expiresAt) {
        store.put(hash(token), expiresAt.getEpochSecond());
    }

    public boolean isContain(String token) {
        Long exp = store.get(hash(token));
        if (exp == null) return false;

        long now = Instant.now().getEpochSecond();

        if (exp <= now) {
            store.remove(hash(token));
            return false;
        }

        return true;
    }

    public void removeExpiredTokens() {
        long now = Instant.now().getEpochSecond();
        store.entrySet().removeIf(entry -> entry.getValue() <= now);
    }

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}