package com.example.demo.Pro12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@Slf4j
public class Example12 {
	
	WebClient clinet = WebClient.create();
	
	@GetMapping("/rest")
	public Mono<String> rest(int idx){
		return Mono.just("Hello " + idx);
	}
	
	public static void main(String[] args) {
		System.setProperty("reactor.ipc.netty.workerCount", "2");
		System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
		SpringApplication.run(Example12.class, args);
	}

}
