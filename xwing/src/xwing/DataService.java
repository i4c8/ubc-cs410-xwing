package xwing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import gr.gousiosg.javacg.stat.JCallGraph;

public class DataService {
	
	// Main Method
	/**
	 * @param args - Specified .jar file at runtime in console. 
	 */
	public static void main(String[] args){
		System.out.println("Welcome to XWing!\r\n");
		//	If we decide to specify within the program rather than
		//	in the runtime arguments, build from here.
		//System.out.println("Specify .jar name: ");
		
		// Call java-callgraph
		try {
			
			File result = runCallGraph(args, "result.txt");
			//System.out.print("\r\n\r\n" + "printing Results: ");
			//System.out.println(result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// We've got a file with the results of a call to java-callgraph
		// Now we do something with it...
		
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
