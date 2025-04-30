package com.backend.ratelimiteralgo;

import com.backend.ratelimiteralgo.client.Client;
import com.backend.ratelimiteralgo.client.UserSlidingWindow;
import com.backend.ratelimiteralgo.factory.RateLimitorFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RateLimiterAlgoApplication implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(RateLimiterAlgoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Please enter your client ID");
        int clientId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Please enter rateLimiting Algo : ");
        String rateLimitingAlgo = scanner.nextLine();

        Client clientRateLimiter = RateLimitorFactory.getRateLimiter(clientId,rateLimitingAlgo);
//        UserSlidingWindow userSlidingWindow = new UserSlidingWindow(clientId);

        int threadcount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadcount);
        for( int i=0;i<threadcount;i++){
            executorService.execute(()->{
                clientRateLimiter.accessApplication(clientId);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}
