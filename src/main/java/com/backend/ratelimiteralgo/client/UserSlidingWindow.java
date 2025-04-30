package com.backend.ratelimiteralgo.client;

import com.backend.ratelimiteralgo.rateLimiter.SlidingWindow;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

//multiple users have sliding window rate limiter access
public class UserSlidingWindow implements Client {
    Map<Integer, SlidingWindow> bucket;
    public UserSlidingWindow(int clientId) {
        bucket  = new HashMap<>();
        bucket.put(clientId,new SlidingWindow(5,1));
    }

    public void accessApplication(int clientId) {
        boolean granted = false;
        if(bucket.get(clientId).grantAccess()){
//            System.out.println("Access Granted perform your business logic");
            granted = true;
        }else{
//            System.out.println("Access Denied Due to Rate Limiting!!");
            granted = false;
        }
        System.out.println(Thread.currentThread().getName()+": "+ LocalTime.now()+" - "+(granted ?"Access Granted perform your business logic":"Access Denied Due to Rate Limiting!!" ));

    }
}
