package com.hlab.app.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author Hesham Omran
 *
 */
public class ConfigFile {
	
	private Properties properties;
	private static ConfigFile instance;
	
	private ConfigFile(){	
		connectToPropertiesFile();
	}
	
	public static ConfigFile getInstance(){
		if(instance == null){
			instance = new ConfigFile();
		}
		return instance;
	}
	
	private void connectToPropertiesFile(){
		properties = new Properties();
		try {
			properties.load(new FileInputStream("emonao.properties"));
		} catch (IOException e) {
			System.out
					.println("ERROR (class preferences): can't open "
							+ "properties file 'ArduinoActuator.properties'. Please validate "
							+ "that the file exists and the user rights for reading "
							+ "are given. Error message: " + e.toString());
			System.exit(0);
		}
	}
	
	public String getRobotURL(){
		return properties.getProperty("robot.url");
	}
	
	public String getEmotionAPIKey(){
		return properties.getProperty("emotion.apikey");
	}
	
	public String getButlerVisionFolderPath(){
		return properties.getProperty("butler.visionimagepath");
	}
	
	public String getButlerCommandKeyword(){
		return properties.getProperty("butler.keyword");
	}
	
	public String getButlerWakeUpTone(){
		return properties.getProperty("butler.wakeuptone");
	}

}
