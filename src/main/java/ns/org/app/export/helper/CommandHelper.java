/**
  CommandHelper.java
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
package ns.org.app.export.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandHelper {

	private static final Logger log = LoggerFactory.getLogger(CommandHelper.class);

	@Value("${export.mongo.host}")
	private String host;

	@Value("${export.mongo.db.name}")
	private String dbName;

	@Value("${export.mongo.db.collection}")
	private String collectionName;

	@Value("${export.dir}")
	private String outputFileDir;

	public void createCommand() {

		StringBuilder command = new StringBuilder(); // command variable to execute

		// command arguments variable
		List<String> cmdParams = new ArrayList<>();

		// Check operating system
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		// check environment to run command
		if (isWindows) {
			// -- Window --
			log.debug("Environment detected : dos");
			cmdParams.add("cmd.exe");
			command.append("C:/Program Files/MongoDB/Server/3.4/bin/mongoexport.exe");

		} else {
			// -- All Linux, MacIntosh --
			log.debug("Environment detected : Shell");
			cmdParams.add("bash");
			// Adding command
			cmdParams.add("-c"); // command signal
			command.append("/usr/local/mongodb/bin/mongoexport ");
		}

		command.append(" -h ").append(host);
		command.append(" -d ").append(dbName);
		command.append(" -c ").append(collectionName);
		command.append(" -o ").append(outputFileDir).append(System.currentTimeMillis()).append(".json");

		log.debug("command [{}]", command);
		cmdParams.add(command.toString());
		executeCommand(cmdParams);
	}

	private void executeCommand(List<String> cmdParams) {

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectErrorStream(true);
		StringBuilder output = new StringBuilder();
		// Add commands to process
		processBuilder.command(cmdParams);

		try {
			// Run command
			Process process = processBuilder.start();

			// Get output
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			log.info("[{}]", output);
			if (exitVal == 0) {
				log.info("Command executed successfully and exported at location [{}]", outputFileDir);
			} else {
				log.error("Command failed to executed, code [{}]", exitVal);
			}
			log.info("Stopping service");
			System.exit(0); // stop the service

		} catch (Exception e) {
			log.error("Unknown error occured, failed to export data [{}]", e);
		}

	}
}
