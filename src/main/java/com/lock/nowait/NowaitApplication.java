package com.lock.nowait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
@EnableRetry
public class NowaitApplication {

	public static void main(String[] args) {

//		ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//		for (int i = 0; i < 1000; i++) {
//
//			int finalI = i;
//			executorService.submit(() -> {
//				System.out.println(Thread.currentThread().getName() + " " + finalI);
//			});
//
//		}
//
//		executorService.shutdown();





	SpringApplication.run(NowaitApplication.class, args);
	}

}
