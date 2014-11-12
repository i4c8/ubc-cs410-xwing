package crawl;

import java.awt.List;
import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class Crawler {
	
	public Crawler(){
		//
	}
	
	//TODO
	public static Git getGit(Repository repo){
		//Repository repo;
		Git git = new Git(repo);
		
		return git;
	}
	
	public static void walkRepo(Repository repo){
		// TODO Figure out how to access the outside repo we want to analyze
		RevWalk walk = new RevWalk(repo);
		List authJar = new List();
		
		for(RevCommit commit : walk){
			// TODO We need to compile the commit to a jar somehow and run it through callgraph
			
			String commitName = commit.getName();
			newJar(commitName);
			
			// TODO: (1) shove all directories and java files into JAR
			// 				Refer to: 	https://stackoverflow.com/questions/2977663/java-code-to-create-a-jar-file
			//							http://www.java2s.com/Code/Java/File-Input-Output/CreateJarfile.htm (using java.util.jar)
			//					(refer to these if JarHelper doesn't work)
			
			
			
			// TODO: (2) get author of commit and JAR filename, and add to a list

			String authorName = commit.getAuthorIdent().getName();
			authJar.add("[ " + authorName + ", " +" ]");
			
			System.out.println("Commit name: " + commitName);
		}
		
		// Cleanup
		walk.dispose();
		
		// TODO: (3) return above said list
	}
	
	/**
	 * Creates an empty JAR file
	 * 
	 * @param filename	filename of JAR to be created
	 */
	private static void newJar(String filename) {
		try {
			File commitJar = new File(filename + ".jar");
			commitJar.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
