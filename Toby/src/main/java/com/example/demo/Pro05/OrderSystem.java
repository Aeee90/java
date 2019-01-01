package com.example.demo.Pro05;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class OrderSystem {
	
	public static enum MENU {
		AMERICANO, CAFEMOCA, MILK_TEA, CAKE, NO_MENU;
		
		private static final String AM 		= "Ameriacno";
		private static final int AM_TIME 	= 5000;
		private static final String CM 		= "Cafe Moca";
		private static final int CM_TIME 	= 5000;
		private static final String MT 		= "Milk Tea";
		private static final int MT_TIME 	= 5000;
		private static final String CK 		= "Cake";
		private static final int CK_TIME 	= 5000;
		private static final String NM		= "No Menu";
		
		public static String getMenu(MENU menu) {
			switch (menu) {
			case AMERICANO:
				return AM;
			case CAFEMOCA:
				return CM;
			case MILK_TEA:
				return MT;
			case CAKE:
				return CK;
			default:
				return NM;
			}
		}
		
		public static MENU getMenu(int menu) {
			switch (menu) {
			case 1:
				return AMERICANO;
			case 2:
				return CAFEMOCA;
			case 3:
				return MILK_TEA;
			case 4:
				return CAKE;
			default:
				return NO_MENU;
			}
		}
		
		public static String getMenues()
		{
			List<String> list = List.of(AM, CM, MT, CK);
			return list.stream()
					.map(v -> (list.indexOf(v) + 1) + "." + v)
					.reduce("", (v1, v2) -> {
				v1 += v2 + "\t";
				return v1;
			});
		}
		
		public static int getTime(MENU menu) {
			switch (menu) {
			case AMERICANO:
				return AM_TIME;
			case CAFEMOCA:
				return CM_TIME;
			case MILK_TEA:
				return MT_TIME;
			case CAKE:
				return CK_TIME;
			default:
				return 0;
			}
		}
	}
	
	public static class Store implements Publisher<OrderSystem.MENU>
	{
		private Queue<Clerk> clerkPool;
		private Queue<Customer> workList;
		private Supervisor supervisor;
		
		public Store(int clerkNum) {
			workList = new LinkedList();
			clerkPool = new LinkedList();
			for(int i =0; i<clerkNum; i++)
				clerkPool.add(new Clerk(this,i));
			supervisor = new Supervisor();
		}
		
		@Override
		public void subscribe(Subscriber<? super MENU> s) {
			workList.add((Customer)s);
			
			if(supervisor.isDead)
				new Thread(supervisor).start();
		}
		
		public void onWait(Clerk clerk) {
			clerkPool.add(clerk);
		}
		
		private class Supervisor implements Runnable
		{
			public boolean isDead = true;
			
			@Override
			public void run() {
				isDead = false;
				while(!workList.isEmpty()) {
					if(!clerkPool.isEmpty())
					{
						Clerk clerk = clerkPool.poll();
						clerk.setSub(workList.poll());
						new Thread(clerk).start();
					}
					
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				isDead = true;
			}
		}
		
		private class Clerk implements Runnable
		{
			private Customer sub;
			private Store store;
			private int num;
			
			public Clerk(Store store, int num) {
				this.store = store;
				this.num = num;
			}
			
			public Clerk setSub(Customer sub)
			{
				this.sub = sub;
				return this;
			}
			
			@Override
			public void run() {
				try {
					MENU menu = sub.getMenu();
					Thread.sleep(MENU.getTime(menu));
					sub.onSubscribe(new Subscription() {
						@Override
						public void request(long n) {
							sub.onNext(menu);
							sub.onComplete();
						}
						
						@Override
						public void cancel() {}
					});
				} catch (Exception e) {}
				finally {
					store.onWait(this);
				}
			}
		}
	}
	
	public static class Customer implements Subscriber<MENU>
	{
		private final MENU menu;
		
		public Customer(MENU menu) {
			this.menu = menu;
		}
		
		public MENU getMenu(){
			return menu;
		}
		
		@Override
		public void onSubscribe(Subscription s) {
			s.request(1);
		}
		
		@Override
		public void onNext(MENU menu) {
			System.out.println( "[" + Thread.currentThread().getName() + "]" + MENU.getMenu(menu) + " 받았습니다.");
		}
		
		@Override
		public void onComplete() {
			System.out.println("수고하세요.");
		}
		
		@Override
		public void onError(Throwable t) {
			System.out.println("이게 카페야! 환불해줘!");
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Java9 Cafe에 오신 것을 환영합니다.");
		
		Store store = new Store(3);
		while(true)
		{
			System.out.println("메뉴를 골라주세요.");
			System.out.println(MENU.getMenues());
			store.subscribe(new Customer(MENU.getMenu(sc.nextInt())));
		}
	}
}
