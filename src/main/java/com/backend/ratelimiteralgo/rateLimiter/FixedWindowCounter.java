package com.backend.ratelimiteralgo.rateLimiter;

import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowCounter implements RateLimiter {
    private final long bucketSize;
    private final long windowSizeInSeconds;

    private final ConcurrentHashMap<Long, Integer> counterMap;//window start time, windowSizeInSeconds
    private long currentWindow;

    public FixedWindowCounter(long bucketSize, long windowSizeInSeconds) {
        this.bucketSize = bucketSize;
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.counterMap = new ConcurrentHashMap<>();
        this.currentWindow = getWindowStartTime(System.currentTimeMillis());
    }

    @Override
    public boolean grantAccess() {
        long currentTime = System.currentTimeMillis();
        long windowStartTime = getWindowStartTime(currentTime);

//        is it a new window ?
        if(windowStartTime!=currentWindow){
            currentWindow = windowStartTime;
            counterMap.clear();
        }

        counterMap.putIfAbsent(currentWindow,0);
        int requestCount = counterMap.get(currentWindow);

        if(requestCount<bucketSize){
            counterMap.put(currentWindow,requestCount+1);
            return true;
        }
        return false;
    }

    private long getWindowStartTime(long currentTimestampMillis) {
        long seconds = currentTimestampMillis/1000;
        return (seconds/windowSizeInSeconds)*windowSizeInSeconds;
    }
}
