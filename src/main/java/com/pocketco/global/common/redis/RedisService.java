package com.pocketco.global.common.redis;

import java.util.List;

public interface RedisService {
    void saveTokens(String submissionId, List<String> tokens);
    List<String> getTokens(String submissionId);
    void deleteTokens(String submissionId);
}