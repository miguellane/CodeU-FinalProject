package SearchEngine;

import Retriever.java;

//import joptsimple.*;

import java.io.IOException;

public class Run {
	public static void main(String[] args) throws IOException {
		System.out.println("test");
		Retriever retriever = new Retriever();
		retriever.runner();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Run test = new Run();
		System.out.println(retriever);
	}
}

