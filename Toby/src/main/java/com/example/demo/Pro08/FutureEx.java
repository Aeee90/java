package com.example.demo.Pro08;

import java.nio.channels.AsynchronousByteChannel;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.netty.util.concurrent.Future;

public class FutureEx {
	
	interface SuccessCallback{
		void onSuccess(String result);
	}
	
	interface ExceptionCalback{
		void onError(Throwable t);
	}
	
	public static class CallbackFutureTask extends FutureTask<String>
	{
		SuccessCallback sc;
		ExceptionCalback ec;
		public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCalback ec) {
			super(callable);
			this.sc = Objects.requireNonNull(sc);
			this.ec = Objects.requireNonNull(ec);
			System.out.println(this.sc.toString());
			System.out.println(this.toString());
			System.out.println(this.ec.toString());
		}
		
		@Override
		protected void done() {
			try {
				sc.onSuccess(get());
			} catch (InterruptedException e) {
				//너 수행을 수행하지말고 종료해라(강제가 아님.)
				Thread.currentThread().interrupt();
			} catch(ExecutionException e)
			{
				//포장된 e를 던지는게 아니라...진짜이유인 e.getCause를 던지자.
				//ec.onError(e);
				ec.onError(e.getCause());
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException{
		ExecutorService es = Executors.newCachedThreadPool();
		
		/*Future<String> f = es.submit(() -> {
			Thread.sleep(2000);
			System.out.println("Async");
			return "Hello";
		});*/

//		FutureTask<String> f = new FutureTask( new Callable<String>() {
//			@Override
//			public String call() throws Exception {
//				System.out.println("ccc");
//				return null;
//			}
//		});
		

		
		
		CallbackFutureTask f = new CallbackFutureTask(() -> {
			//비지니스 로직
			Thread.sleep(2000);
			System.out.println("Async");
			if(1==1) throw new RuntimeException("Asnc ERROR!");
			return "Hello";
		}, System.out::println
		,	e ->{ 
			System.out.println("Error : " + e.getMessage());
		});
		
		//기술 로직
		es.execute(f);
		es.shutdown();
		
		//자바에서 진짜로 callback을 써서 처리를 하는가??
		//AsynchronousByteChannel Interface 참조
		
		
		//기술적인 로그와 비지니스로직이 혼동된 코드는 안 좋다.. 깔끔하게 추상화하여 분리하는게 좋다.
	}
}
