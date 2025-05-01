package com.backend.ratelimiteralgo.rateLimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class LeakyBucket implements RateLimiter {
    private final int leakRate;
    private final int bucketSize;
    private final long leakIntervalInMs = 1000;

    private AtomicInteger currentWaterLevel;
    private long lastLeakTime;

    public LeakyBucket(int leakRate, int bucketSize) {
        this.leakRate = leakRate;
        this.bucketSize = bucketSize;
        this.currentWaterLevel = new AtomicInteger(0);
        this.lastLeakTime = System.currentTimeMillis();
    }

    @Override
    public boolean grantAccess() {
        leakBucketIfPossible();
        if(currentWaterLevel.get()<bucketSize){
            currentWaterLevel.incrementAndGet();
            return true;
        }
        return false;
    }

    public void leakBucketIfPossible(){
        long currentTime = System.currentTimeMillis();
        long difference = currentTime-lastLeakTime;
        if(difference>=leakIntervalInMs){
            int waterToLeak = (int)(difference/leakIntervalInMs)*leakRate;
            int newWaterLevel = Math.max(0,currentWaterLevel.get()-waterToLeak);
            currentWaterLevel.set(newWaterLevel);
            lastLeakTime = currentTime;
        }
    }
}
