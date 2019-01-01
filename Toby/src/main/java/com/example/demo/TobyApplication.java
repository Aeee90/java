package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.CompleteFuture;

@EnableAsync
@SpringBootApplication
public class TobyApplication {
	
	@RestController
	public static class MyController{
		//RestTemplate rt = new RestTemplate();
		AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
		
		@RequestMapping("/rest")
		public ListenableFuture<ResponseEntity<String>> rest(int idx) {
			//String res = rt.getForObject("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx);
			ListenableFuture<ResponseEntity<String>> res =  rt.getForEntity("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx);
			return res;
		}
		
		@RequestMapping("/processing")
		public DeferredResult<String> processing(int idx){
			DeferredResult<String> dr = new DeferredResult<String>();
			
			ListenableFuture<ResponseEntity<String>> f1 =  rt.getForEntity("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx);
			f1.addCallback(s -> {
				dr.setResult(s.getBody() + "/work");
			}, e -> {
				dr.setErrorResult(e.getMessage());
			});
			
			return dr;
		}
		
		@RequestMapping("/processings")
		public DeferredResult<String> processings(int idx){
			DeferredResult<String> dr = new DeferredResult<String>();
			
			ListenableFuture<ResponseEntity<String>> f1 =  rt.getForEntity("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx);
			f1.addCallback(s -> {
				ListenableFuture<ResponseEntity<String>> f2 =  rt.getForEntity("http://localhost:8081/rest/service2?req={req}", String.class, s.getBody());
					f2.addCallback(s2->{
						dr.setResult(s2.getBody() + "/work2");
					},e->{
						dr.setErrorResult(e.getMessage());
					});
			}, e -> {
				dr.setErrorResult(e.getMessage());
			});
			
			return dr;
		}
		
		@RequestMapping("/processings2")
		public DeferredResult<String> processings2(int idx){
			DeferredResult<String> dr = new DeferredResult<String>();
			
			ListenableFuture<ResponseEntity<String>> f1 =  rt.getForEntity("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx);
			f1.addCallback(s -> {
				ListenableFuture<ResponseEntity<String>> f2 =  rt.getForEntity("http://localhost:8081/rest/service2?req={req}", String.class, s.getBody());
					f2.addCallback(s2->{
						ListenableFuture<String> f3 = MyService.work(s2.getBody());
						f3.addCallback(s3->{
							dr.setResult(s3);
						}, e->{
							dr.setErrorResult(e.getMessage());
						});
					},e->{
						dr.setErrorResult(e.getMessage());
					});
			}, e -> {
				dr.setErrorResult(e.getMessage());
			});
			
			return dr;
		}
	}
	
	@Service
	public static class MyService{
		@Async
		public static ListenableFuture<String> work(String req){
			return new AsyncResult<>(req + "/asyncwork");
		}
	}
	
	@Bean
	public ThreadPoolTaskExecutor myThreadPool() {
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		te.setCorePoolSize(1);
		te.setMaxPoolSize(1);
		te.initialize();
		return te;
	}

	public static void main(String[] args) { SpringApplication.run(TobyApplication.class, args); }
	//Thread Pool Hell => 요청이 급격하게 들어오면 쓰레드가 꽉 차고, 추가 요청은 대기상태로 넘어간다. => Latency가 증가한다.
	// 원인 : 서버 내에서 백엔드API를 호출하는 동안에 프론트엔드 쓰레드는 대기상태에 빠진다.
}
