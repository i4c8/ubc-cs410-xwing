package xwing;

import static org.junit.Assert.*;

import org.junit.Test;

public class CallTreeTest {

	@Test
	public void nodesTest() {		
		CallTree test = new CallTree();
		test.addMethod("test");
		assertEquals("size is one", 1, test.getMethods().size());
		assertEquals("contains test", "test", test.getMethods().get(0).getName());
		test.addMethod("test");
		assertEquals("no duplicate added", 1, test.getMethods().size());
		test.addMethod("test2");
		assertEquals("size is two", 2, test.getMethods().size());
		assertEquals("contains test2", "test2", test.getMethods().get(1).getName());
		test.addMethod("test2");
		assertEquals("size is still two", 2, test.getMethods().size());
	}
	
	@Test
	public void classTest() {
		CallTree test = new CallTree();
		test.addClass("test");
		assertEquals("size is one", 1, test.getClasses().size());
		assertEquals("contains test", "test", test.getClasses().get(0).getName());
		test.addClass("test");
		assertEquals("no duplicate added", 1, test.getClasses().size());
		test.addClass("test2");
		assertEquals("size is two", 2, test.getClasses().size());
		assertEquals("contains test2", "test2", test.getClasses().get(1).getName());
		test.addClass("test2");
		assertEquals("size is still two", 2, test.getClasses().size());
	}
	
	
	@Test
	public void addMethodsToClassTest(){
		CallTree test = new CallTree();
		test.addMethod("method1");
		test.addClass("class1");
		test.addMethodToClass("class1", "method1");
		assertEquals("class1 contains 0", true, test.getClasses().get(0).getMethods().contains(0));
		test.addMethodToClass("class1", "method1");
		test.addMethodToClass("class1", "notmethod");
		test.addMethodToClass("notclass", "method1");
		assertEquals("no dups or false additions", 1, test.getClasses().get(0).getMethods().size());
	}
	
	@Test
	public void linkTest() {
		CallTree test = new CallTree();
		test.addMethod("method1");
		test.addMethod("method2");
		test.addConnection("method1", "method2");
		assertEquals("one element", 1, test.getLinks().size());
		assertEquals("correct element", true, test.getLinks().contains(new CallTreeLinks(0, 1)));
		test.addConnection("method1", "method2");
		test.addConnection("method1", "notmethod");
		test.addConnection("notmethod", "method2");
		assertEquals("no dups or false additions", 1, test.getLinks().size());


		
	}

}
