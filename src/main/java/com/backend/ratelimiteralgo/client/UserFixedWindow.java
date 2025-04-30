package com.backend.ratelimiteralgo.client;

import com.backend.ratelimiteralgo.rateLimiter.FixedWindowCounter;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class UserFixedWindow  implements  Client{
    Map<Integer,FixedWindowCounter> bucket;
    public UserFixedWindow(int clientId) {
        bucket = new HashMap<>();
        bucket.put(clientId,new FixedWindowCounter(5,1));//5 req every sec frame
    }
    @Override
    public void accessApplication(int clientId) {
        boolean granted = false;
        if(bucket.get(clientId).grantAccess()){
            granted = true;
        }else{
            granted = false;
        }
        System.out.println(Thread.currentThread().getName()+": "+ LocalTime.now()+" - "+(granted ?"Access Granted perform your business logic":"Access Denied Due to Rate Limiting!!" ));
    }
}
