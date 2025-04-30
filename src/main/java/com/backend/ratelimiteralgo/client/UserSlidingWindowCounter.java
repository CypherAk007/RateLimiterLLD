package com.backend.ratelimiteralgo.client;

import com.backend.ratelimiteralgo.rateLimiter.SlidingWindowCounter;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class UserSlidingWindowCounter implements Client{
    Map<Integer, SlidingWindowCounter> bucket;
    public UserSlidingWindowCounter(int clientId){
        bucket = new HashMap<>();
        bucket.put(clientId,new SlidingWindowCounter(5,1));
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
