package crawl;

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
		
		for(RevCommit commit : walk){
			// TODO We need to compile the commit to a jar somehow and run it through callgraph
			
			// TODO: (1) compile commit into JAR
			
			// TODO: (2) get author of commit and JAR filename, and add to a list
			
			System.out.println("Commit name: " + commit.getName());
		}
		
		// Cleanup
		walk.dispose();
		
		// TODO: (3) return above said list
	}
}
