package com.backend.ratelimiteralgo.factory;

import com.backend.ratelimiteralgo.client.*;
import com.backend.ratelimiteralgo.rateLimiter.RateLimiter;
import com.backend.ratelimiteralgo.rateLimiter.SlidingWindow;
import com.backend.ratelimiteralgo.rateLimiter.SlidingWindowCounter;
import com.backend.ratelimiteralgo.rateLimiter.TokenBucket;

public class RateLimitorFactory {
    public static Client getRateLimiter(int clientId, String rateLimiterName) {
        return switch (rateLimiterName){
            case "userslidingwindow" -> new UserSlidingWindow(clientId);
            case "fixedwindowcounter" -> new UserFixedWindow(clientId);
            case "slidingwindowcounter" -> new UserSlidingWindowCounter(clientId);
            case "tokenbucket" -> new UserTokenBucket(clientId);
            default -> new UserSlidingWindow(clientId);
        };
    }
}
