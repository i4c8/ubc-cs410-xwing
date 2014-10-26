package xwing;

import org.junit.Test;

/*
 * The last modifications I did of CallgraphParser these all passed as of Oct 25/ 7:00 pm
 */
public class CallGraphParserTest {
	
	/*
	 * After running this test check temp10 for the String java
	 * Note: Java is okay, we are only trying to remove instances 
	 * of provided classes like java.util
	 */
	@Test
	public void removeJavaTest(){
		CallgraphParser test = new CallgraphParser();
		test.removeJava("result.txt", 10);
	}
	
	/*
	 * Check temp11 for any duplicate lines
	 */
	@Test
	public void removeDuplicatesTest(){
		CallgraphParser test = new CallgraphParser();
		test.removeDuplicates("result.txt", 11);
	}
	
	/*
	 * Check temp12.classes all lines start with C
	 * Check temp12.methods all lines start with M
	 */
	@Test
	public void splitMethodsAndClassesTest(){
		CallgraphParser test = new CallgraphParser();
		test.splitClassesMethods("result.txt", 12);
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
