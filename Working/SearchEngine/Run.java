package SearchEngine;


//import Retriever.java;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

import redis.clients.jedis.Jedis;

public class Run {

	public static void main(String[] args) throws IOException {
		int option = 0;
		Scanner reader = new Scanner(System.in);
//		Jedis jedis = new Jedis("catfish.redistogo.com", 10923);
//		jedis.auth("6c2d21e12259335a176dbe84c9371048");
		Jedis jedis = new Jedis("redis-15405.c8.us-east-1-3.ec2.cloud.redislabs.com", 15405);		
		jedis.auth("password");
		JedisIndex index = new JedisIndex(jedis); 

		System.out.println("Hello, please enter a search phrase, all lowercase:");
		System.out.println("Two terms not seperated by operator will be treated as AND:");
		System.out.println("Operators include 'AND', 'OR', '-':");
//		System.out.println("Prefixes include 'Crawl:', 'Site:', 'Cringe:', 'IFL:'");
		String cmdLine = reader.nextLine();
		String[] searches = cmdLine.split("\\s+");
		switch(searches[0]){
			case "Crawl:":
				option = 1;
				Crawler crawl = new Crawler("https://en.wikipedia.org/wiki/Java_(programming_language)", index);
				crawl.run();
				break;
//			case "Site:":	option = 2;
//				System.out.println((char)27 + "[34;43mBlue text with yellow background");
//				break;
//			case "IFL:":	option = 3;	break;
//			case "Cringe:":	option = 4;	break;
		}

		System.out.println("Query: " + cmdLine);
		Retriever result = new Retriever();
		if(option != 0)
			result = operators(Arrays.copyOfRange(searches, 1, searches.length), index);
		else
			result = operators(searches, index);		
		result.print();
//		System.out.println(index.urlNum());
//		result.stringPrint();
	}

//Takes in working string, recursively parses and calls retriever options to output single retriever object.
	public static Retriever operators(String[] array, JedisIndex index){
		Retriever retriever = new Retriever();
		if(array.length == 1){
			retriever = new Retriever(array[0], index);
			return retriever;		
		}



		for(int i = 0; i < array.length; i++){
			if(array[i].equals("OR")){
				retriever = operators(Arrays.copyOfRange(array, 0, i),index);
				return retriever.or(operators(Arrays.copyOfRange(array, i+1, array.length),index));
			}else if(array[i].equals("AND")){
				retriever = operators(Arrays.copyOfRange(array, 0, i),index);
				return retriever.and(operators(Arrays.copyOfRange(array, i+1, array.length),index));	
			}
		}
		retriever = new Retriever(array[0], index);
		for(int i = 1; i < array.length; i++){
			if(array[i].startsWith("-")){
				retriever = retriever.minus(new Retriever(array[i].substring(1),index));			
			}else{
				retriever = retriever.and(new Retriever(array[i],index));
		}	}
		return retriever;
	}


}

