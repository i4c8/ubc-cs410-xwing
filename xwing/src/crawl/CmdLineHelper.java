package crawl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
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
	 * Empty constructor.
	 */
	public CmdLineHelper() {
		executor = new DefaultExecutor();
	}

	/**
	 * Opens a Windows terminal. Not for Macs or Linux machines!
	 */
	public void openWindowsTerminal() {
		String line = "cmd.exe";
		CommandLine cmdLine = CommandLine.parse(line);
		executor.setExitValue(0);
		
		try {
			int exitValue = executor.execute(cmdLine);
		} catch (IOException e) {
			System.err.println("You filthy liar! You're not on Windows!");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Runs a command. Uses exit code of 0 if not specified.
	 * 
	 * @param input		The command as if it were directly typed into a terminal
	 */
	public void run(String input) {
		CommandLine cmdLine = CommandLine.parse(input);
		executor.setExitValue(0);
		System.out.println(input);

		try {
			/*int exitValue = */executor.execute(cmdLine);
		} catch (IOException e) {
			System.err.println("  Could not run the following command: " + input);
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a command. Set an exit value if the commmand doesn't exit properly.
	 * 
	 * @param input		The command as if it were directly typed into a terminal
	 * @param exitValue	Exit value to set
	 */
	public void run(String input, int exitValue) {
		CommandLine cmdLine = CommandLine.parse(input);
		executor.setExitValue(exitValue);
		System.out.println(input);

		try {
			/*int exitValue = */executor.execute(cmdLine);
		} catch (IOException e) {
			System.err.println("  Could not run the following command: " + input);
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the working directory. 
	 * @param directory	Filepath
	 */
	public void cd(File directory) {
		executor.setWorkingDirectory(directory);
//		File workDir = executor.getWorkingDirectory();
	}
	
}
