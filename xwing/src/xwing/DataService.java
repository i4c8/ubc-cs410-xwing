package xwing;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import calltree.CallgraphParser;
import crawl.Crawler;
import gr.gousiosg.javacg.stat.JCallGraph;


public class DataService {
	
	static ArrayList<String> authjar = new ArrayList<String>();
	
	/** MAIN METHOD
	 * @param args - Specified git project name and author name
	 */
	public static void main(String[] args){
		
		System.out.println("Welcome to XWing!\r\n");
		String auth = args[0];
		String proj = args[1];
		System.out.println("Running on Author: " + auth + " and project: " + proj);
		
		Crawler crawler = new Crawler();
		// Open the directory of the local git repo
		String repoPath = "/Users/" + System.getProperty("user.name") + "/git/" + proj + "/.git";
		File gitDir = new File(repoPath);
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			
			Repository repo = builder.setGitDir(gitDir)
					  .readEnvironment() // scan environment GIT_* variables
					  .findGitDir() // scan up the file system tree
					  .build();
			
			// Call the crawler to iterate through each commit
			System.out.println("Crawling repository: " + repoPath);
			authjar = crawler.walkRepo(repo, auth, proj); //{"name,result#.txt",....}
			String[] authors = new String[authjar.size()];
			String[] jars = new String[authjar.size()];
			for (int i = 0; i<authjar.size(); i++){
				String[] temp = authjar.get(i).split(",");
				authors[i] = temp[0];
				jars[i] = temp[1];
			}
			
			CallgraphParser parser = new CallgraphParser();
			String[] jsons = parser.parseList(jars);
			File htmlFile = new File(insertArguments(authors, jsons));
			Desktop.getDesktop().browse(htmlFile.toURI());
			repo.close();
			System.out.println("Finished crawling.\r\n");
			
			for(String s : authjar){
				System.out.println(s);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
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
			System.out.println("Passing " + result.getName() + " to parser.");
			String[] results = parser.parseList(list);
			File htmlFile = new File(insertArgument(results));
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		} */

	}
	
	public static String insertArguments(String[] authors, String[] jsons){
		int usedColor = 0;
		HashMap<String, String> authorColors = new HashMap<String, String>();
		//Note there is only 30 distinct author colors before we begin repeating
		String[] colorOptions = {"#36FADD",
		                         "#F6132E",
		                         "#6672F7",
		                         "#FDE15B",
		                         "#A6757E",
		                         "#1EEB64",
		                         "#DA178C",
		                         "#368020",
		                         "#ED52F6",
		                         "#3883C4",
		                         "#FD8F34",
		                         "#D6FDB3",
		                         "#BDE6E6",
		                         "#0D8261",
		                         "#F887B7",
		                         "#D03D61",
		                         "#F8C8BB",
		                         "#8AC02E",
		                         "#E7CDF0",
		                         "#677868",
		                         "#FFBB6B",
		                         "#F26B59",
		                         "#4CCE98",
		                         "#9CA010",
		                         "#ACFC5E",
		                         "#D05623",
		                         "#3CC860",
		                         "#87F2EB",
		                         "#41A892",
		                         "#78E6A8"};
		
		//Builds a HashSet of Author/Color Pairs;\
		for (int i = 0; i<authors.length; i++){
			if (!authorColors.containsKey(authors[i])){
				authorColors.put(authors[i], colorOptions[usedColor]);
				if (usedColor == 29){
					usedColor = 0;
				}
				else usedColor++;
			}
		}
		//Builds the colorList we want added
		String toAdd = "var colorList = [";
		for (int i = 0; i<authors.length; i++){
			toAdd = toAdd.concat("\""+authorColors.get(authors[i])+"\", ");
		}
		toAdd = toAdd.substring(0,toAdd.length()-2);
		toAdd = toAdd.concat("];");
		
		//Builds the jsons we want added
		String toAdd2 = "var jsons = [";
		for (int i = 0; i<jsons.length; i++){
			toAdd = toAdd.concat("\""+jsons[i]+"\", ");
		}
		toAdd = toAdd.substring(0,toAdd.length()-2);
		toAdd = toAdd.concat("];");
		
		//Adds it
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader("web/index3.html"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("runthis.html"));
			while ((line = br.readLine()) != null){
				if (line.equals("<script>")){
					bw.write(line);
					bw.newLine();
					bw.write(toAdd);
					bw.newLine();
					bw.write(toAdd2);
					bw.newLine();
				}
				else bw.write(line);
				bw.newLine();
			}
			br.close();
			bw.close();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "runthis.html";
	}	
	
	/**
	 * @param args
	 * @return File with name fname containing the results of the java-callgraph call
	 * @throws IOException
	 */
	public static void runCallGraph(String[] args, String fname) throws IOException{
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
	    
	    
		//return result;
		
	}
	

}
