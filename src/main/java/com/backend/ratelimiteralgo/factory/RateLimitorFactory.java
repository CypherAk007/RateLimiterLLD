package com.backend.ratelimiteralgo.factory;

import com.backend.ratelimiteralgo.client.Client;
import com.backend.ratelimiteralgo.client.UserFixedWindow;
import com.backend.ratelimiteralgo.client.UserSlidingWindow;
import com.backend.ratelimiteralgo.client.UserSlidingWindowCounter;
import com.backend.ratelimiteralgo.rateLimiter.RateLimiter;
import com.backend.ratelimiteralgo.rateLimiter.SlidingWindow;
import com.backend.ratelimiteralgo.rateLimiter.SlidingWindowCounter;

public class RateLimitorFactory {
    public static Client getRateLimiter(int clientId, String rateLimiterName) {
        return switch (rateLimiterName){
            case "userslidingwindow" -> new UserSlidingWindow(clientId);
            case "fixedwindowcounter" -> new UserFixedWindow(clientId);
            case "slidingwindowcounter" -> new UserSlidingWindowCounter(clientId);
            default -> new UserSlidingWindow(clientId);
        };
    }
}
