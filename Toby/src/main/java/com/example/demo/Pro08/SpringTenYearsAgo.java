package com.example.demo.Pro08;

public class SpringTenYearsAgo {
	
	public static void main(String[] args) {
		
	}

}


//import java.util.concurrent.Future;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.AsyncResult;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.concurrent.ListenableFuture;
//
//import lombok.extern.slf4j.Slf4j;
//
//
//@SpringBootApplication
//@Slf4j
//@EnableAsync
//public class TobyApplication {
//	
//	@Component
//	public static class MyService{
//		@Async //쓰레드가 정리되지 않고 쓰레드를 계속 만들기때문에 안좋다.
//		//public Future<String> hello() throws InterruptedException{
//		public ListenableFuture<String> hello() throws InterruptedException{
//			log.info("hello()");
//			Thread.sleep(2000);
//			return new AsyncResult("Hello");
//		}
//	}
//	
//	@Bean
//	ThreadPoolTaskExecutor tp(){
//		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
//		te.setCorePoolSize(10);//요청이 들어오면 최소로 만드는 갯수
//		te.setMaxPoolSize(100);
//		te.setQueueCapacity(200);// 큐 대기를 걸 갯수
//		//코어 => 큐 => 맥스 순서대로 진행된다.
//		te.setThreadNamePrefix("myThread");
//		te.initialize();
//		return te;
//	}
//
//	public static void main(String[] args) {
//		try(ConfigurableApplicationContext c = SpringApplication.run(TobyApplication.class, args)){
//			
//		};
//	}
//	
//	@Autowired MyService myService;
//	
//	@Bean
//	ApplicationRunner run() {
//		return args ->{
//			log.info("run()");
////			Future<String> f = myService.hello();
////			log.info("exit " + f.isDone());
////			log.info(f.get());
//			
//			ListenableFuture<String> f = myService.hello();
//			f.addCallback(s->log.info(s), e->log.info(e.getCause().toString()));
//			log.debug("exit");
//		};
//	}
//	//이런 코드가 얼마나 유용할 것인가?
//	//비동기를 거는 이유는, 장시간걸리는 작업일 경우..
//	//결과를 데이터 베이스나 다른 저장장치에 넣어놓고, 작업이 끝났는지를 계속 access해서 확인
//	//Session에 저장하는 방법도 있다. 
//	//=> 10년전에 쓰던 방법들