package com.example.demo.Pro10;

import java.util.function.Function;

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
import org.springframework.web.context.request.async.DeferredResult;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;

@EnableAsync
@SpringBootApplication
public class Example10 {
	
	@RestController
	public static class MyController{
		//RestTemplate rt = new RestTemplate();
		AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
		
		@RequestMapping("/processings")
		public DeferredResult<String> processings(int idx){
			DeferredResult<String> dr = new DeferredResult<String>();
			
			Completion
				.from(rt.getForEntity("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx))
				.andApply( s->rt.getForEntity("http://localhost:8081/rest/service2?req={req}", String.class, s.getBody()))
				.andApply( s -> MyService.work(s.getBody()))
				.andError(e -> dr.setErrorResult(e.getMessage()))
				.andAccept(s->dr.setResult(s));
			
//			ListenableFuture<ResponseEntity<String>> f1 =  rt.getForEntity("http://localhost:8081/rest/service?req={req}", String.class, "hello" + idx);
//			f1.addCallback(s -> {
//				ListenableFuture<ResponseEntity<String>> f2 =  rt.getForEntity("http://localhost:8081/rest/service2?req={req}", String.class, s.getBody());
//					f2.addCallback(s2->{
//						ListenableFuture<String> f3 = MyService.work(s2.getBody());
//						f3.addCallback(s3->{
//							dr.setResult(s3);
//						}, e->{
//							dr.setErrorResult(e.getMessage());
//						});
//					},e->{
//						dr.setErrorResult(e.getMessage());
//					});
//			}, e -> {
//				dr.setErrorResult(e.getMessage());
//			});
			
			return dr;
		}
	}
	
	public static class AcceptCompletion<S> extends Completion<S, Void>{
		
		private Consumer<S> con;
		
		public AcceptCompletion(Consumer<S> con) {
			this.con = con;
		}
		
		@Override
		protected void run(S value) {
			con.accept(value);
		}
	}
	
	public static class ApplayCompletion<S,T> extends Completion<S,T>{
		private Function<S, ListenableFuture<T>> fn;
		
		public ApplayCompletion(Function<S, ListenableFuture<T>> fn) {
			this.fn = fn;
		}
		
		@Override
		protected void run(S value) {
			ListenableFuture<T> lf = fn.apply(value);
			lf.addCallback(s -> complete(s), e -> error(e));
		}
	}
	
	public static class ErrorCompletion<T> extends Completion<T,T>{
		
		private Consumer<Throwable> econ;
		public ErrorCompletion(Consumer<Throwable> econ) {
			this.econ = econ;
		}
		
		@Override
		protected void run(T value) {
			if(next != null) next.run(value);
		}
		
		@Override
		protected void error(Throwable e) {
			econ.accept(e);
		}
	}
	
	public static class Completion<S, T> {
		
		protected Completion next;
		
		public Completion() {}
		
		public static <S,T> Completion<S,T> from(ListenableFuture<T> lf) {
			Completion<S,T> c  = new Completion<>();
			
			lf.addCallback(s->{
				c.complete(s);
			}, e->{
				c.error(e);
			});
			
			return c;
		}
		
		public <V> Completion<T,V> andApply(Function<T, ListenableFuture<V>> fn) {
			Completion<T,V> c = new ApplayCompletion<>(fn);
			this.next = c;
			return c;
		}
		
		public void andAccept(Consumer<T> con) {
			Completion<T, Void> c = new AcceptCompletion(con);
			this.next = c;
		}
		
		public Completion<T,T> andError(Consumer<Throwable> econ) {
			Completion<T,T> c = new ErrorCompletion<>(econ);
			this.next = c;
			return c;
		}
		
		protected void complete(T s)
		{
			if (next != null) next.run(s);
		}
		
		protected void run(S value) {
		}
		
		protected void error(Throwable e)
		{
			if(next != null) next.error(e);
		}
	}
	
//	public static class Completion{
//		
//		protected Completion next;
//		
//		public Completion() {}
//		
//		public static Completion from(ListenableFuture<ResponseEntity<String>> lf) {
//			Completion c  = new Completion();
//			
//			lf.addCallback(s->{
//				c.complete(s);
//			}, e->{
//				c.error(e);
//			});
//			
//			return c;
//		}
//		
//		public Completion andApply(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
//			Completion c = new ApplayCompletion(fn);
//			this.next = c;
//			return c;
//		}
//		
//		public void andAccept(Consumer<ResponseEntity<String>> con) {
//			Completion c = new AcceptCompletion(con);
//			this.next = c;
//		}
//		
//		public Completion andError(Consumer<Throwable> econ) {
//			Completion c = new ErrorCompletion(econ);
//			this.next = c;
//			return c;
//		}
//		
//		protected void complete(ResponseEntity<String> s)
//		{
//			if (next != null) next.run(s);
//		}
//		
//		protected void run(ResponseEntity<String> value) {
//		}
//		
//		protected void error(Throwable e)
//		{
//			if(next != null) next.error(e);
//		}
//	}
	
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

	public static void main(String[] args) { SpringApplication.run(Example10.class, args); }
	//Thread Pool Hell => 요청이 급격하게 들어오면 쓰레드가 꽉 차고, 추가 요청은 대기상태로 넘어간다. => Latency가 증가한다.
	// 원인 : 서버 내에서 백엔드API를 호출하는 동안에 프론트엔드 쓰레드는 대기상태에 빠진다.
}
