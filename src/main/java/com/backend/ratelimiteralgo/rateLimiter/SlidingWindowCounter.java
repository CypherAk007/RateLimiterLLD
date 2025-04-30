package com.backend.ratelimiteralgo.rateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowCounter implements RateLimiter {
    private final ConcurrentHashMap<Long, AtomicInteger> counters;
    private final int bucketSize;
    private final int windowSizeInSeconds;

    public SlidingWindowCounter(int bucketSize, int windowSizeInSeconds) {
        this.counters = new ConcurrentHashMap<>();
        this.bucketSize = bucketSize;
        this.windowSizeInSeconds = windowSizeInSeconds;
    }

    @Override
    public boolean grantAccess() {
        long currentTimeInSeconds = System.currentTimeMillis()/1000;
        long currentWindowStart = getCurrentWindowStart(currentTimeInSeconds);
        long previousWindowStart = currentWindowStart-windowSizeInSeconds;

        counters.putIfAbsent(currentWindowStart,new AtomicInteger(0));
        counters.putIfAbsent(previousWindowStart,new AtomicInteger(0));

        long timeInCurrentWindow = currentTimeInSeconds- currentWindowStart;
        // 0.75-> 75% of previous window is still having weight as only 25% in new window is completed
        double weight = 1.0 - ((double) timeInCurrentWindow/windowSizeInSeconds);
        double estimatedCount = counters.get(previousWindowStart).get() * weight+
                counters.get(currentWindowStart).get();
        if(estimatedCount>=bucketSize){
            return false;//reject
        }
        counters.get(currentWindowStart).incrementAndGet();

        //cleaup old windows
        counters.keySet().removeIf(key->key<previousWindowStart);

        return true;//allow
    }

    private long getCurrentWindowStart(long currentTime){
        long time  = currentTime/1000;
        return (time/windowSizeInSeconds)*windowSizeInSeconds;
    }

}
//CORE LOGIC
//Sliding Window Counter Estimation Logic

//double weight = 1.0 - ((double) timeInCurrentWindow / windowSizeInSeconds);
//What this does:
//It calculates how far into the current window we are.
//
//timeInCurrentWindow is the number of seconds elapsed since the current window started.
//
//windowSizeInSeconds is the total duration of the window (e.g., 60 seconds).
//
//So, the weight is:
//
//        weight = fraction of the previous window that still affects the current rate
//Example:
//
//Suppose windowSizeInSeconds = 60
//
//You are 15 seconds into the current window → timeInCurrentWindow = 15
//
//Then:
//weight = 1 - (15 / 60) = 0.75
//
//That means we consider 75% of the previous window's requests as still affecting the rate limit.