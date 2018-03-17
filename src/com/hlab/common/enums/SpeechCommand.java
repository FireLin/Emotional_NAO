package com.hlab.common.enums;

import com.hlab.app.model.ConfigFile;

/**
 * This class is used to store all speech commands in an Enum variable
 * @author Hesham Omran
 *
 */
public enum SpeechCommand {
	HELLOLINE("AGENT_NAME", "hi AGENT_NAME"),
	HOWIFEEL("how do I feel","How am I feeling"),
	HOWUFEEL("how do you feel","How are you feeling");
	
	private String command1;
	private String command2;
    private SpeechCommand(String value1, String value2) {
    	if(value1.contains("AGENT_NAME") 
    			|| value2.contains("AGENT_NAME") ){
    		String name = ConfigFile.getInstance().getButlerCommandKeyword();
    		this.command1 = value1.replaceAll("\\bAGENT_NAME\\b", name);
    		this.command2 = value2.replaceAll("\\bAGENT_NAME\\b", name);
    		System.out.println(this.command1);
    		System.out.println(this.command2);
    	}
    	else{
    		this.command1 = value1;
    		this.command2 = value2;
    	}
    }
    
    public String getFirstCommand(){
    	return command1;
    }
    
    public String getSecondCommand(){
    	return command2;
    }
    
    @Override
    public String toString() {
    	return command1;
   }
    
};