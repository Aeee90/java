package com.example.demo.Pro11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Example11 {
	

	public static void main(String[] args) throws ExecutionException, InterruptedException{
		ExecutorService es = Executors.newFixedThreadPool(2);
		CompletableFuture
					.runAsync(() -> log.info("runAsync"))
					.thenRun(() -> log.info("thenRunAsync"));
		
		CompletableFuture
					.supplyAsync(() -> {
						log.info("supplyAsync");
						if(1==1) throw new RuntimeException();
						return 1;
					}, es)
					.thenApplyAsync(s -> {
						log.info("thenRun1 {}", s);
						return s + 1;
					}, es)
					.exceptionally(e -> -10)
					.thenApplyAsync(s -> {
						log.info("thenRun2 {}", s);
						return s + 1;
					}, es)
					.thenAcceptAsync(s2 -> log.info("thenAccept {}", s2), es);
		log.info("exit");
		
		
		ForkJoinPool.commonPool().shutdown();
		ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
	}
	
	<T> CompletableFuture<T> toCF(ListenableFuture<T> lf){
		CompletableFuture<T> cf = new CompletableFuture<T>();
		lf.addCallback(s -> {cf.complete(s);}, e -> {cf.completeExceptionally(e);});
		
		return cf;
	}
}
