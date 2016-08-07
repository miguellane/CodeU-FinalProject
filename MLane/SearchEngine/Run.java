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
			System.out.println((char)27 + "[34;43mBlue text with yellow background");
			String search = searches[i];
			System.out.println("Query: " + search);
			Retriever search1 = new Retriever(search, index);
			search1.print();
		}

	}

}

