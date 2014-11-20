package crawl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipFile;

import javax.net.ssl.HttpsURLConnection;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import xwing.DataService;
import crawl.ZipFileUtil;
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
	
	// HARD CODED URLs and PATHS within!!!
	// Will re-factor to accept a parameter instead if time, rather than having two different versions
	// with a URL/path for each repository...
	public void downloadCommit(String owner, String project, String commitName){
		System.out.println("Starting Download...");
		try {
			URL commitDownload =  new URL("https://github.com/" + owner + "/" + project + "/archive/" + commitName + ".zip");
			
			HttpsURLConnection con = (HttpsURLConnection) commitDownload.openConnection();
			
			// Check for errors
			int responseCode = con.getResponseCode();
			InputStream inputStream;
			if (responseCode == HttpURLConnection.HTTP_OK) {
			    inputStream = con.getInputStream();
			} else {
			    inputStream = con.getErrorStream();
			}

			OutputStream output = new FileOutputStream(commitName + ".zip");

			// Process the response
			byte[] buffer = new byte[8 * 1024]; // Or whatever
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) > 0) {
			    output.write(buffer, 0, bytesRead);
			}

			output.close();
			inputStream.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Download Finished.");
	}
	
	/**
	 * Creates a JAR of every commit in a repo.
	 * 
	 * @param repo	Repository to create a JAR From
	 * 
	 * @return	List of author-JAR filename pairs. Format: "{authorName,commitHash"}"
	 * 
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	public ArrayList<String> walkRepo(Repository repo, String owner, String project) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		// TODO Figure out how to access the outside repo we want to analyze
		RevWalk walk = new RevWalk(repo);
		ArrayList<String> authJar = new ArrayList<String>();
		
		// add startpoints so that walk can be traversed.
		// attempting to loop over walk otherwise will result in nothing happening.
		// (http://dev.eclipse.org/mhonarc/lists/jgit-dev/msg00858.html)
		for (Ref ref : repo.getAllRefs().values()) {
			try {
				walk.markStart( walk.parseCommit( (ref.getObjectId()) ));
			} catch (IncorrectObjectTypeException notACommit) {
				continue;
			}	
		}
		
		int i = 0;
		
		// go thru every commit
		for(RevCommit commit : walk){
			// TODO We need to compile the commit to a jar somehow and run it through callgraph
			
			String commitName = commit.getName();
			String authName = commit.getAuthorIdent().getName();
			// Add authorname to list of author/jar pairs.
			authJar.add(authName);
			System.out.println("Commit Hash: " + commitName);
			
			//Obtain the archived files
			downloadCommit(owner, project, commitName);
			
			// We've downloaded the archived version of the code
			// Unzip the folder to a temp directory
			File commitZip = new File(commitName + ".zip");
			ZipFile zf = new ZipFile(commitZip);
			File target = new File("TEMP");
			ZipFileUtil.unzipFileIntoDirectory(zf, target);
			File targetIn = new File("TEMP" + "\\" + project + "-" + commitName);
			
			// cd to the directory of the unzipped source code (with inclujded POM for Maven)
			// then install it (dir will be "target" inside respective source folder; java-callgraph jars
			// contained within.
			CmdLineHelper clh = new CmdLineHelper();
			clh.cd(targetIn);
			clh.openWindowsTerminal();
			/*
			 * Specify location of Maven executable here. Don't forget to escape the backslashes!
			 * Also, if quotes are not needed (i.e. no spaces within filepath), DO NOT add another set of quotes on the filepath!
			 * 
			 * It goes without saying that Maven needs to be installed. Environment variables have no meaning here!
			*/
			String mvnFilepath = "\"C:\\Program Files\\apache-maven-3.2.3\\bin\\mvn.bat\"";
			
			// BEFORE YOU COMPILE: pom.xml must have "<packaging>jar</packaging>" for output to jar.
			
			clh.run(mvnFilepath + " install", 1);
			
			String[] jarPath = new String[1];
			jarPath[0] = "TEMP" + "\\" + project + "-" + commitName + "\\target" + "\\javacg-0.1-SNAPSHOT-dycg-agent.jar";
			File jarTarget = new File(jarPath[0]);
			
			if(jarTarget.exists()){
				DataService.runCallGraph(jarPath, "result" + i + ".txt");
				i++;
			}
			
			//https://github.com/gousiosg/java-callgraph/archive/bf7276eca18543bbac91f6d3edd197604683297d.zip
			
		} // end of going through each commit
		
		// Cleanup
		walk.dispose();
		
		return authJar;
	}
	
//	/**
//	 * Creates an empty JAR file.
//	 * 
//	 * @param filename	filename of JAR to be created, without the extension
//	 * @return			the newly-created JAR (returns null if IOException encountered)
//	 */
//	private File newJar(String filename) {
//		try {
//			File commitJar = new File(filename + ".jar");
//			commitJar.createNewFile();	
//			return commitJar;			
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	private void addNextFile(TreeWalk fileTreeWalk, Repository repo, RevObject commit) {
//		while (fileTreeWalk.next()) {
//			ObjectId objectId = fileTreeWalk.getObjectId(0);
//			ObjectLoader loader = repo.open(commit.getId());
//			
//			// (old stuff)
////			if (!fileTreeWalk.next())
////				return null;
//			
//			InputStream in = loader.openStream();
//			CrawlerHelper.addStreamToJar(jarFile, objectId, in);
//		}
//	}
	

}
