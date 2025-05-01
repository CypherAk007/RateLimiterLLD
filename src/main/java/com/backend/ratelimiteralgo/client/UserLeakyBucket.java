package com.backend.ratelimiteralgo.client;

import com.backend.ratelimiteralgo.rateLimiter.LeakyBucket;
import com.backend.ratelimiteralgo.rateLimiter.TokenBucket;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

public class UserLeakyBucket implements Client{
    private final ConcurrentHashMap<Integer, LeakyBucket> bucket;

    public UserLeakyBucket(int clientId) {
        this.bucket = new ConcurrentHashMap<>();
        bucket.put(clientId,new LeakyBucket(5,5));
    }


    @Override
    public void accessApplication(int clientId) {
        boolean granted = false;

        if(bucket.get(clientId).grantAccess()){
            granted = true;
        }else {
            granted = false;
        }
        System.out.println(Thread.currentThread().getName()+": "+ LocalTime.now()+" - "+(granted ?"Access Granted perform your business logic":"Access Denied Due to Rate Limiting!!" ));
    }
}
