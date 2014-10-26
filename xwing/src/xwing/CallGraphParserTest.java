package xwing;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;

/*
 * All Tests automatic save for the last which has instructions to manually performed
 */
public class CallGraphParserTest {
	
	/*
	 * Tests that each line does not contain java
	 */
	@Test
	public void removeJavaTest(){
		CallgraphParser test = new CallgraphParser();
		test.removeJava("result.txt", 10);
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader("temp10"));
			while ((line = br.readLine()) != null){
				assertEquals("Line does not contain \"java\"", false, line.contains("java"));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        File toDelete = new File("temp10");
        toDelete.delete();

	}
	
	/*
	 * Check temp11 for any duplicate lines
	 */
	@Test
	public void removeDuplicatesTest(){
		CallgraphParser test = new CallgraphParser();
		HashSet<String> data = new HashSet<String>();
		test.removeDuplicates("result.txt", 11);
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader("temp11"));
			while ((line = br.readLine()) != null){
				assertEquals("Line does not contain duplicates", false, data.contains(line));
				data.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        File toDelete = new File("temp11");
        toDelete.delete();
	}
	
	/*
	 * Check temp12.classes all lines start with C
	 * Check temp12.methods all lines start with M
	 */
	@Test
	public void splitMethodsAndClassesTest(){
		CallgraphParser test = new CallgraphParser();
		test.splitClassesMethods("result.txt", 12);
		this.testMethods();
		this.testClasses();
	}
	
	@Test
	public void testClasses() {
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader("temp12.classes"));
			while ((line = br.readLine()) != null){
				assertEquals("Line begins with C", 'C', line.charAt(0));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        File toDelete = new File("temp12.classes");
        toDelete.delete();
	}
	
	@Test
	public void testMethods() {
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader("temp12.methods"));
			while ((line = br.readLine()) != null){
				assertEquals("Line begins with M", 'M', line.charAt(0));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        File toDelete = new File("temp12.methods");
        toDelete.delete();
	}

	/*
	 * Compare result0.json with result1.json
	 * They should both be in the appropriate format as discussed on piazza
	 * Used tlrobinson.net/projects/javascript-fun/jsondiff/
	 * To compare them, all new additions are at the bottom of result2
	 * And no method names/numbers are changed from one to the next
	 */
	@Test
	public void parseListTest(){
		String[] testList = {"result1.txt", "result2.txt"};
		CallgraphParser test = new CallgraphParser();
		test.parseList(testList);
	}

}
