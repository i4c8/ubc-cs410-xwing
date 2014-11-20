package crawl;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

/**
 * Helper class for commons-exec, for running commands via a terminal.
 *  
 * @author Norris
 *
 */
public class CmdLineHelper {

	private DefaultExecutor executor;
	
	/**
	 * Initializes the Helper.
	 */
	public CmdLineHelper() {	
		executor = new DefaultExecutor();
		executor.setExitValue(1);
	}

	/**
	 * Opens a Windows terminal. Not for Macs or Linux machines!
	 */
	public void openWindowsTerminal() {
		String line = "cmd";
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		
		try {
			int exitValue = executor.execute(cmdLine);
		} catch (IOException e) {
			System.err.println("You filthy liar! You're not on Windows!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a command.
	 * 
	 * @param input		The command as if it were directly typed into a terminal
	 */
	public void run(String input) {
		CommandLine cmdLine = CommandLine.parse(input);
		
		try {
			int exitValue = executor.execute(cmdLine);
		} catch (IOException e) {
			System.err.println("  Could not run the following command: " + input);
//			e.printStackTrace();
		}
	}
	
}
