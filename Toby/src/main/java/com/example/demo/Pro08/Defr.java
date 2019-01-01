package com.example.demo.Pro08;

public class Defr {

}

//
//import java.sql.ResultSet;
//import java.util.Queue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.async.DeferredResult;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
//
//import lombok.extern.slf4j.Slf4j;
//
//
//@SpringBootApplication
//@Slf4j
//@EnableAsync
//public class TobyApplication {
//
//	static Queue<DeferredResult<String>> results = new ConcurrentLinkedQueue();
//		
//	@RestController
//	public static class MyController{
//		
//		@GetMapping("/dr")
//		public DeferredResult<String> async() throws InterruptedException{
//			log.info("dr");
//			DeferredResult<String> dr = new DeferredResult<>(6000000L);
//			results.add(dr);
//			
//			return dr;
//		}
//		
//		@GetMapping("/dr/count")
//		public String drcount() {
//			return String.valueOf(results.size());
//		}
//
//		@GetMapping("/dr/event")
//		public String drevent(String msg) {
//			for(DeferredResult<String> dr : results)
//			{
//				dr.setResult("Hello " + msg);
//				results.remove();
//			}
//			
//			return "OK";
//		}
//		
//		@GetMapping("/emitter")
//		public ResponseBodyEmitter emitter() throws InterruptedException {
//			log.info("em");
//			
//			ResponseBodyEmitter emitter = new ResponseBodyEmitter();
//			
//			return emitter;
//		}
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(TobyApplication.class, args);	
//	}
//}