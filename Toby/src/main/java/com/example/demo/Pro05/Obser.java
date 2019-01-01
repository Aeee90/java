package com.example.demo.Pro05;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Stream;

public class Obser {
	
	static class IntObservable extends Observable implements Runnable{
		
		@Override
		public void run() {
			List.of(1,2,3,4,5,6).forEach( i ->{
				setChanged();
				notifyObservers(i);
			});
		}
	}

	public static void main(String[] args) {
		Observer ob = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("observer :" + arg);
			}
		};
		
		IntObservable io = new IntObservable();
		io.addObserver(ob);
		
		io.run();
	}
}
