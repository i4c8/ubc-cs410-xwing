package crawl;

import jar.JarHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevTree;

public class CrawlerHelper {

	/**
	 * Adds an InputStream to a JAR file
	 * @param id
	 * @param id 
	 * @param inStream
	 */
	public static void addStreamToJar(File jarFile, RevTree fileTree, ObjectId id, InputStream inStream) {
		String tempName = id.getName();
		
		File fileToAdd = null;
		
		try {
			fileToAdd = convertToTempFile(inStream, tempName);
			
			JarHelper jh = new JarHelper();
			fileTree.getType();
			jh.jarDir(fileToAdd, jarFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// cleanup temp file
		try {
			Files.delete(fileToAdd.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException n) {
			System.err.println("inStream was never converted to a File.");
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
	private static File convertToTempFile(InputStream is, String objectId) throws IOException {
		
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
