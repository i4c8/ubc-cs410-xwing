package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.environment.EnvironmentUtils;

import crawl.CmdLineHelper;

public class CmdLineTest {

	public static void main(String[] args) {
		
		CmdLineHelper clh = new CmdLineHelper();
		File dir = new File("D:\\Android-Universal-Image-Loader-master\\");
		clh.cd(dir);
//		clh.openWindowsTerminal();
//		clh.openCygwinTerminal();
		Map<String,String> procE;
		try {
			procE = new HashMap<String,String>(EnvironmentUtils.getProcEnvironment());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		clh.run("java",1);
		
		clh.run("\"D:\\Program Files\\apache-maven-3.2.3\\bin\\mvn.bat\"" + " compile",1);
//		clh.run("mvn",1);

	}
	

	
}
