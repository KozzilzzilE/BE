package com.pocketco.global.util.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
    private TimeUtils() {} // 생성자 막기

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static LocalDateTime toKst(Instant instant) {
        if (instant == null) return null;

        return instant
                .atZone(KST)
                .toLocalDateTime();
    }
}