package com.example.demo.test;

import java.util.List;

public class No50 {
	
	public static List<Integer> getList(int limit) 
	{
		List<Integer> list = List.of(2);
		for(int i = 3; i< limit; i++)
			list.add(i);
		return list;
	}
	
	public static int sum = 0;
	public static int max = 0;
	
	public static void main(String[] args) {
		
		getList(1000000).stream().filter(i -> {
			for(int j=3; j < Math.sqrt(i)+1; j+=2)
				if(i%j == 0)
					return false;
				
			return true;
		}).forEach(System.out::println);
	}
}
