package test;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

import crawl.CmdLineHelper;

public class CmdLineTest {

	public static void main(String[] args) {
		
		CmdLineHelper clh = new CmdLineHelper();
		
		clh.openWindowsTerminal();
//		clh.run("cmd");
		clh.run("java");
		
	}
	

	
}
