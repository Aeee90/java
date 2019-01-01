package com.example.demo.Pro07;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class Ex1 {


  //Reactive Streams
  //ǥ���� �� �˾ƾ���   
  public static void main(String[] args) {
     Publisher<Integer> pub = sub -> {
        sub.onSubscribe(new Subscription() {
           @Override
           public void request(long n) {
              sub.onNext(1);
              sub.onNext(2);
              sub.onNext(3);
              sub.onNext(4);
              sub.onNext(5);
              sub.onComplete();
             
           }
           
           @Override
           public void cancel() {
              
           }
        });  
     };
     
     //���۷����͸� �ϳ� �����Ѵ�
     
     // pub
     
      Publisher<Integer> subOnPub = sub -> {
        ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
          @Override
          public String getThreadNamePrefix() {
             //return super.getThreadNamePrefix();
             return "subOn-";
          }
        });         
        es.execute(() -> pub.subscribe(sub));
     };
     
      Publisher<Integer> pubOnPub = sub -> {
         //pub.subscribe(sub);
         subOnPub.subscribe(new Subscriber<Integer>() {
            //ExecutorService es = Excutors.newSingleThreadExecutor();
            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
               @Override
               public String getThreadNamePrefix() {
                  return "pubOn-";
               }
            });
            
            @Override
            public void onSubscribe(Subscription s) {
               sub.onSubscribe(s);
            }
            
            //������ �����带 ���ؼ�
            @Override
            public void onNext(Integer integer) {
               //sub.onNext(integer);
               es.execute(() -> sub.onNext(integer));
            }
            
            @Override
            public void onError(Throwable t) {
               //sub.onError(t);
               es.execute(() -> sub.onError(t));
            }

            @Override
            public void onComplete() {
               //sub.onComplete();
               es.execute(() -> sub.onComplete());
            }         
            
         });
      };
     
     //sub
     
     pubOnPub.subscribe(new Subscriber<Integer>() {
        @Override
        public void onSubscribe(Subscription s) {
           s.request(Long.MAX_VALUE);
        }
        
        @Override
        public void onNext(Integer integer) {
        }
        
        @Override
        public void onError(Throwable t) {
        }
        
        @Override
        public void onComplete() {
        }
     });

     System.out.println("exit");
  }
}

// ������Ǯ ���� ť�� ���� ó������ ����� �������Ե�
// ���� ���α׷������� �ۺ��ſ� ���꽺ũ���̹��� ���������� �����ϰԲ� �ڵ带 �ۼ����� ����
// 
