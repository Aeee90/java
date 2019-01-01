package com.example.demo.Pro05;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Iter {

	public static void main(String... args) {
		// Duality
		// Observer pattern
		// Reactive Streams - 표준
		
		//Collection => Iterable을 상속한 객체들 => for-each loop를 돌릴수 있다.
		
		//List<Integer> iter = Arrays.asList(1,2,3,4,5);
		//List<Integer> iter = List.of(1,2,3,4,5);
		//Iterable<Integer> iter = Arrays.asList(1,2,3,4,5);
		Iterable<Integer> iter = () -> 
				new Iterator<Integer>() {
					final static int MAX = 10;
					int i = 0;
					@Override
					public Integer next() {
						return i++;
					}
					
					@Override
					public boolean hasNext() {
						// TODO Auto-generated method stub
						return i < MAX;
					}
				};
				
				
		for(Iterator<Integer> it = iter.iterator(); it.hasNext();)
			System.out.println(it.next());
		
		//상대성(duality)
		//Iterable <=> Observerable
		//poll	   <=> push

	}

}
