package xwing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import calltree.CallgraphParser;
import crawl.Crawler;
import gr.gousiosg.javacg.stat.JCallGraph;


public class DataService {
	
	/** MAIN METHOD
	 * @param args - Specified .jar file at runtime in console. 
	 */
	public static void main(String[] args){
		System.out.println("Welcome to XWing!\r\n");
		//	If we decide to specify within the program rather than
		//	in the runtime arguments, build from here.
		//System.out.println("Specify .jar name: ");
		
		//This is a naive/mock version of what our program will do when ran.
		// Call java-callgraph
		try {
			
			File result = runCallGraph(args, "result.txt");
			// We've got a file with the results of a call to java-callgraph
			// Pass it to the parsing stage to make JSON object files
			String[] list = new String[1];
			list[0] = result.getName();
			CallgraphParser parser = new CallgraphParser();
			// Consider changing parser to accept files rather than file names?
			parser.parseList(list);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @param args
	 * @return File with name fname containing the results of the java-callgraph call
	 * @throws IOException
	 */
	private static File runCallGraph(String[] args, String fname) throws IOException{
		System.out.println("Creating file: " + fname);
		File result = new File(fname);
		
		System.out.println("Running java-callgraph.");
		// java-callgraph prints each method to the console. So I'm just going to capture it.
		// We can also modify the source and rebuild (or get rid of) the parser, but we've already got the parser
		// working on files. Consider refactoring if time.
		OutputStream out = new FileOutputStream(result);
	    //ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(out);
	    // Save the old System.out!
	    PrintStream old = System.out;
	    // Tell Java to use your special stream
	    System.setOut(ps);
	    
	    // Call JCallGraph on jar name in args
	    JCallGraph.main(args);
	    // Put things back
	    System.out.flush();
	    System.setOut(old);
	    System.out.println("Finished java-callgraph!");

	    if(result.exists()){
	    	System.out.println("Results are in: " + fname);
	    } else
	    	System.out.println("Error creating results file.");
	    
	    
		return result;
		
	}
	

}
