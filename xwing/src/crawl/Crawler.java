package crawl;

import jar.JarHelper;

import java.awt.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.DepthWalk.Commit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

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
	
	public ArrayList<String> walkRepo(Repository repo) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		// TODO Figure out how to access the outside repo we want to analyze
		RevWalk walk = new RevWalk(repo);
		ArrayList<String> authJar = new ArrayList<String>();
		
		// graciously stolen from http://dev.eclipse.org/mhonarc/lists/jgit-dev/msg00858.html
		for (Ref ref : repo.getAllRefs().values()) {
			try {
				walk.markStart( walk.parseCommit( (ref.getObjectId()) ));
			} catch (IncorrectObjectTypeException notACommit) {
				continue;
			}	
		}
		
		for(RevCommit commit : walk){
			// TODO We need to compile the commit to a jar somehow and run it through callgraph
			
			String commitName = commit.getName();
			File jarFile = newJar(commitName);
			
			// TODO: (1) shove all directories and java files into JAR
			// 				Refer to: 	https://stackoverflow.com/questions/2977663/java-code-to-create-a-jar-file
			//							http://www.java2s.com/Code/Java/File-Input-Output/CreateJarfile.htm (using java.util.jar)
			//					(refer to these if JarHelper doesn't work)

			/* http://stackoverflow.com/a/7427658 */
			RevTree fileTree = commit.getTree();
			
			TreeWalk fileTreeWalk = new TreeWalk(repo);
			fileTreeWalk.addTree(fileTree);
			fileTreeWalk.setRecursive(true);
			
//			fileTreeWalk.setFilter(PathFilter.create(path));
			
			while (fileTreeWalk.next()) {
				ObjectId objectId = fileTreeWalk.getObjectId(0);
				ObjectLoader loader = repo.open(commit.getId());
				
				// old stuff
	//			if (!fileTreeWalk.next())
	//				return null;
				
				InputStream in = loader.openStream();
	
				// Convert the above InputStream into a (temp) file so that it can be shoved into JarHelper (which creates the JAR)
				String tempName = objectId.getName();
				File fileToAdd = convertToTempFile(in, tempName);			
				
				JarHelper jh = new JarHelper();
				fileTree.getType();
				jh.jarDir(fileToAdd, jarFile);
				
				// cleanup temp file
				Files.delete(fileToAdd.toPath());
				
				// (2) get author of commit and JAR filename, and add to a list	
				String authorName = commit.getAuthorIdent().getName();
				
				authJar.add("{ " + authorName + "," + commitName +".jar }");
			}
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
	
	/**
	 * Converts an InputStream to a File.
	 * 
	 * (Mostly) stolen from http://www.mkyong.com/java/how-to-convert-inputstream-to-file-in-java/
	 * 
	 * @param is		the InputStream to convert
	 * @param objectId 	object id for the InputStream
	 * @return			File created from InputStream
	 */
	private File convertToTempFile(InputStream is, String objectId) throws IOException {
		
		File temp = File.createTempFile(objectId, null);
		FileOutputStream outputStream = new FileOutputStream(temp);
		
		int read = 0;
		byte[] bytes = new byte[1024];
		
		while ((read = is.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		
		return temp;
	}
}
