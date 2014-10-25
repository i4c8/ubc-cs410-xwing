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
		
		System.out.println("Hello world.");
		
		String[] argsOut = new String[5];
		argsOut[0] = "javacg-0.1-SNAPSHOT-static.jar";
		
		JCallGraph.main(argsOut);
		
	}
	


}
