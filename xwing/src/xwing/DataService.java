package xwing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import gr.gousiosg.javacg.stat.ClassVisitor;
import gr.gousiosg.javacg.stat.JCallGraph;
import gr.gousiosg.javacg.stat.MethodVisitor;

public class DataService {
	
	// Main method
	public static void main(String[] args){
		
		System.out.println("Starting java-callgraph on JAR: " + args[0]);
		
		//String[] argsOut = new String[1];
		//argsOut[0] = "/lib/javacg-0.1-SNAPSHOT-static.jar";
		
		JCallGraph.main(args);
		
	}
	


}
