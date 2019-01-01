package com.example.demo.Pro05;

import java.util.Arrays;
import java.util.Iterator;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Laest {
	//1. Complete???
	//2. Error
	
	public static void main(String[] args) {
		//Publisher => Observerable
		//Subscriber => Observer
		Publisher<Integer> publisher = new Publisher<Integer>() {
			
			Iterable<Integer> ir = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
			@Override
			public void subscribe(Subscriber<? super Integer> s) {
				Iterator<Integer> it = ir.iterator();
				// TODO Auto-generated method stub
				s.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						try {
							while(n-- > 0)
								if(it.hasNext())
									s.onNext(it.next());
								else
									s.onComplete();
						} catch (Exception e) {
							s.onError(e);
						}
					}
					
					@Override
					public void cancel() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		};
		
		Subscriber<Integer> subscriber = new Subscriber<Integer>() {
			
			@Override
			public void onSubscribe(Subscription s) {
				System.out.println("OnSubscribe");
				s.request(5);
			}
			
			@Override
			public void onNext(Integer t) {
				System.out.println("OnNext : " + t);
			}
			
			@Override
			public void onError(Throwable t) {
				System.out.println("onError");
			}
			
			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		};
		
		publisher.subscribe(subscriber);
	}
}
