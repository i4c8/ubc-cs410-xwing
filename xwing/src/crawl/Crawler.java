package crawl;

import jar.JarHelper;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.DepthWalk.Commit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;

public class Crawler {
	
	public Crawler(Repository repo){
		//
		walkRepo(repo);
	}
	
	//TODO
	public static Git getGit(Repository repo){
		//Repository repo;
		Git git = new Git(repo);
		
		return git;
	}
	
	public ArrayList<String> walkRepo(Repository repo){
		// TODO Figure out how to access the outside repo we want to analyze
		RevWalk walk = new RevWalk(repo);
		ArrayList<String> authJar = new ArrayList<String>();
		
		for(RevCommit commit : walk){
			// TODO We need to compile the commit to a jar somehow and run it through callgraph

			String commitName = commit.getName();
			File jarFile = newJar(commitName);
			
			// TODO: (1) shove all directories and java files into JAR
			// 				Refer to: 	https://stackoverflow.com/questions/2977663/java-code-to-create-a-jar-file
			//							http://www.java2s.com/Code/Java/File-Input-Output/CreateJarfile.htm (using java.util.jar)
			//					(refer to these if JarHelper doesn't work)
			RevTree fileTree = commit.getTree();
			JarHelper jh = new JarHelper();
			fileTree.getType();
//			jh.jarDir(dirOrFile2Jar, jarFile);
			
			// TODO: (2) get author of commit and JAR filename, and add to a list

			String authorName = commit.getAuthorIdent().getName();
			
			// Note: returning jarFile *might* be better idea?
			authJar.add("{ " + authorName + ", " + commitName +" }");
			
		}
		
		// Cleanup
		walk.dispose();
		
		// (3) return above said list
		return authJar;
	}
	
	/**
	 * Creates an empty JAR file.
	 * 
	 * @param filename	filename of JAR to be created, without the extension
	 * @return			the newly-created JAR (returns null if IOException encountered)
	 */
	private File newJar(String filename) {
		try {
			File commitJar = new File(filename + ".jar");
			commitJar.createNewFile();	
			return commitJar;			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
