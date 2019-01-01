package com.example.demo.Pro07;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Toby07 {
	public static void main(String[] args) {
		Publisher<Integer> pub = sub ->{
			sub.onSubscribe(new Subscription() {
				
				@Override
				public void request(long n) {
					sub.onNext(1);
					sub.onNext(2);
					sub.onNext(3);
					sub.onNext(4);
					sub.onNext(5);
				}
				
				@Override
				public void cancel() {
					// TODO Auto-generated method stub
					
				}
			});
		};
		
		pub.subscribe(new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNext(Integer t) {
				
			};
			
			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
			
			
		});
	}
}
