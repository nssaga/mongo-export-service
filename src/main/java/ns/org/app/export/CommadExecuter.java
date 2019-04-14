/**
  CommadExecuter.java
 ***********************************************************************************************************************
 Description: 	

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 14-Apr-2019		Nawal Sah				Initial Version

 Copyright (c) 2018,
 ***********************************************************************************************************************
 */
package ns.org.app.export;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
//https://stackoverflow.com/questions/23609140/archiving-a-mongodb-collection

public class CommadExecuter {
	public static void main(String[] args) {

		String host = "localhost:27017";
		String dbName = "test";
		String collectionName = "movie";
		String outputFile = "/Users/nawalsah/dumps/"+System.currentTimeMillis()+".json ";
		StringBuilder command = new StringBuilder(); // command variable to execute 
		
		// command arguments variable
		List<String> cmdParams = new ArrayList<>();

		// Check operating system
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		// check environment to run command
		if (isWindows) {
			// -- Window --
			cmdParams.add("cmd.exe");
			command.append("C:/Program Files/MongoDB/Server/3.4/bin/mongoexport.exe");

		} else {
			// -- All Linux, MacIntosh --
			cmdParams.add("bash");
			// Adding command
			cmdParams.add("-c"); //command signal
			command.append("/usr/local/mongodb/bin/mongoexport ");
			
		}
		command.append("-h "+host+" -d "+dbName+" -c "+collectionName+" -o "+outputFile);
		System.out.println(command.toString());
		cmdParams.add(command.toString());
		executeCommand(cmdParams);
	}

	private static String executeCommand(List<String> cmdParams) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		//processBuilder.redirectErrorStream(true);
		StringBuilder output = new StringBuilder();
		// Add commands to process
		processBuilder.command(cmdParams);

		try {
			// Run command
			Process process = processBuilder.start();

			// Get output
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader error=new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output);
				System.exit(0);
			} else {
				// abnormal...
				System.out.println(error.readLine());
				System.out.printf("Command failed to executed, code [%s], [%s]", exitVal, error.readLine());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return output.toString();
	}
}
