package SearchEngine;


//import Retriever.java;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

import redis.clients.jedis.Jedis;

//Idea: Case insensitivity
public class Run {

	public static void main(String[] args) throws IOException {
		int option = 0;
		Scanner reader = new Scanner(System.in);
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis); 

		System.out.println("Hello, please enter a search phrase:");
		System.out.println("Two terms not seperated by operator will be treated as AND:");
		System.out.println("Operators include 'AND', 'OR', '-':");
//		System.out.println("Prefixes include 'Site:', 'Cringe:', 'IFL:'");
		String cmdLine = reader.nextLine();
		String[] searches = cmdLine.split("\\s+");
		switch(searches[0]){
			case "Site:":	option = 1;	break;
			case "IFL:":	option = 2;	break;
			case "Cringe:":	option = 3;	break;
		}
//		System.out.println((char)27 + "[34;43mBlue text with yellow background");	
		System.out.println("Query: " + cmdLine);
		Retriever result = new Retriever();
		result = operators(searches, index);
		result.print();

	}
	public static Retriever operators(String[] searches, JedisIndex index){
		Retriever retriever = new Retriever();
		if(searches.length == 1){
			retriever.search(searches[0], index);
			return retriever;		
		}



		for(int i = 0; i < searches.length; i++){
			if(searches[i].equals("OR")){
				retriever = operators(Arrays.copyOfRange(searches, 0, i),index);
				return retriever.or(operators(Arrays.copyOfRange(searches, i+1, searches.length),index));
			}else if(searches[i].equals("AND")){
				retriever = operators(Arrays.copyOfRange(searches, 0, i),index);
				return retriever.and(operators(Arrays.copyOfRange(searches, i+1, searches.length),index));	
			}
		}
		retriever.search(searches[0], index);
		for(int i = 1; i < searches.length; i++){
			if(searches[i].startsWith("-")){
				System.out.println("NOOOO");
				retriever = retriever.minus(new Retriever(searches[i].substring(1),index));			
			}else{
				retriever = retriever.and(new Retriever(searches[i],index));
		}	}
		return retriever;



	}
}

