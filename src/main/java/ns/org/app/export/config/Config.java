/**
  Config.java
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
package ns.org.app.export.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ns.org.app.export.helper.CommandHelper;

@Configuration
public class Config {
	@Autowired
	private CommandHelper commandHelper;
	
	@PostConstruct
	public void startCommmand() {
		commandHelper.createCommand();
	}
}
