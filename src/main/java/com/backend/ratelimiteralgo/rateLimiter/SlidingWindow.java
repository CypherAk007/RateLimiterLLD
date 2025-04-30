package com.backend.ratelimiteralgo.rateLimiter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindow implements RateLimiter{
    Queue<Long> slidingWindow;
    int bucketCapacity; // 10 request are allowed in the window
    int actualWindowTimeFrame; //ttl -> 10 requests are allowed in how many sec/min? -> 1 sec 10 req allowed

    public SlidingWindow(int bucketCapacity, int time){
        this.bucketCapacity = bucketCapacity;
        this.actualWindowTimeFrame = time;
        this.slidingWindow = new ConcurrentLinkedQueue<>();
    }

    // should we grant access to the incoming request or not?
    @Override
    public boolean grantAccess() {
        long currentTime = System.currentTimeMillis();
        updateQueue(currentTime);
        if(slidingWindow.size()<bucketCapacity){
            slidingWindow.offer(currentTime);
            return true;
        }
        return false;
    }

    private void updateQueue(long currentTime){
        // kicks of old elements
        if(slidingWindow.isEmpty()) return ;
        // time in ms so convert to sec as actualWindowTimeFrame is in sec - > 1sec 1000 ms
        long currentWindowTimeFrame  = (currentTime-slidingWindow.peek())/1000;
        while(currentWindowTimeFrame>this.actualWindowTimeFrame){
            slidingWindow.poll();
            if(slidingWindow.isEmpty()) break;
            currentWindowTimeFrame = (currentTime-slidingWindow.peek())/1000;//check for next peek ele if it fits in time frame
        }
    }
}
