package com.backend.ratelimiteralgo.rateLimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucket implements RateLimiter {
    private final int bucketCapacity;
    private final int refillRate;//no. of tokens per second
    private final long refillIntervalMs = 1000; //interval at which it refills

    private AtomicInteger availableTokens;
    private long lastRefillTime;

    public TokenBucket(int bucketCapacity,int refillRate) {
        this.bucketCapacity = bucketCapacity;
        this.refillRate = refillRate;
        this.availableTokens = new AtomicInteger(bucketCapacity); // initially full
        this.lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean grantAccess() {
        refillTokensIfNeeded();

        if(availableTokens.get()>0){
            availableTokens.decrementAndGet();
            return true;
        }
        return false;

    }
    public void refillTokensIfNeeded(){
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - lastRefillTime;
        if(difference>=refillIntervalMs){
            int noOfTokens = (int)(difference/refillIntervalMs)*refillRate;
            int newTokensToAdd = Math.min(availableTokens.get()+noOfTokens,bucketCapacity);

            availableTokens.addAndGet(newTokensToAdd);
            lastRefillTime = currentTime;
        }
    }
}
