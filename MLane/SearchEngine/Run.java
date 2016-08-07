package SearchEngine;


//import Retriever.java;

//import joptsimple.*;

import java.io.*;
import java.util.Scanner;

import redis.clients.jedis.Jedis;

public class Run {

	public static void main(String[] args) throws IOException {
//		int option = 0;
		Scanner reader = new Scanner(System.in);
		System.out.println("Hello, please enter a search phrase:");
//		System.out.println("Options include 'Site:', 'Cringe:', 'IFL:', 'AND', 'OR', '-'");
		String cmdLine = reader.next();
		String[] searches = cmdLine.split(" ");
//		search = searches[0];
//		if(search.compareToIgnoreCase("Cringe:");		
		
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis); 
		
		for(String search: searches){
			System.out.println("Query: " + search);
			Retriever search1 = new Retriever(search, index);
			search1.print();
		}
//		String term2 = "programming";
//		System.out.println("Query: " + term2);
//		Retriever search2 = search(term2, index);
//		search2.print();
		
//		System.out.println("Query: " + term1 + " AND " + term2);
//		Retriever intersection = search1.and(search2);
//		intersection.print();


		//		Run test = new Run();
		//		System.out.println(retriever);

		//System.out.println("test");
		//Retriever retriever = new Retriever();
		//retriever.runner();

	}

}

