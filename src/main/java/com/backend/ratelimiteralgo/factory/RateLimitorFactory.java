package com.backend.ratelimiteralgo.factory;

import com.backend.ratelimiteralgo.client.Client;
import com.backend.ratelimiteralgo.client.UserSlidingWindow;
import com.backend.ratelimiteralgo.rateLimiter.RateLimiter;
import com.backend.ratelimiteralgo.rateLimiter.SlidingWindow;

public class RateLimitorFactory {
    public static Client getRateLimiter(int clientId, String rateLimiterName) {
        return switch (rateLimiterName){
            case "userslidingwindow"-> new UserSlidingWindow(clientId);
            default -> new UserSlidingWindow(clientId);
        };
    }
}
