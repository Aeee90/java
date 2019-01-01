package com.example.demo.Pro14;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Example14 {

	@GetMapping("/event/{id}")
	Mono<Event> hello(@PathVariable long id){
		return Mono.just(new Event(id, "event" + id));
	}
	
	@GetMapping("/events")
	Flux<Event> event(){
		return Flux.just(new Event(1L, "event1"), new Event(2L, "event2"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Example14.class, args);
	}
	
	@Data
	@AllArgsConstructor
	public static class Event{
		long id;
		String value;
	}
}
