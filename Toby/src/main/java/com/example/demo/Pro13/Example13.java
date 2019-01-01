package com.example.demo.Pro13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class Example13 {
	
	@RestController
	public static class MyController{
		
		@GetMapping("/")
		public Mono<String> hello(){
			return Mono.just("Hello WebFlux").log();
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Example13.class, args);
	}

}
