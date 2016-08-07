package SearchEngine;


//import Retriever.java;

//import joptsimple.*;

import java.io.*;
import java.util.Scanner;

import redis.clients.jedis.Jedis;

public class Run {

	public static void main(String[] args) throws IOException {
		int option = 0;
		Scanner reader = new Scanner(System.in);
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis); 

		System.out.println("Hello, please enter a search phrase:");
		System.out.println("Two terms not seperated by operator will be treated as AND:");
		System.out.println("Options include 'Site:', 'Cringe:', 'IFL:', 'AND', 'OR', '-'");
		String cmdLine = reader.nextLine();
		String[] searches = cmdLine.split("\\s+");
		switch(searches[0]){
			case "Site:":	option = 1;	break;
			case "IFL:":	option = 2;	break;
			case "Cringe:":	option = 3;	break;
		}
		for(int i = 0; i < searches.length; i++ ){
			String search = searches[i];
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

