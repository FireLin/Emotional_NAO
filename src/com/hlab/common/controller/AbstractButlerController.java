/**
 * 
 */
package com.hlab.common.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.hlab.app.model.ConfigFile;
import com.hlab.common.enums.Emotion;

/**
 * Abstract Butler class with basic requirments for any robotic butler with emotional responses
 * 
 * @author Hesham Omran
 *
 */
public abstract class AbstractButlerController {
	
	private boolean isEmotionOn;
	private boolean isReactionStarted;
	private boolean isReactionEnded;
	protected String visionfolderpath;
	
	/*
	 * Abstract methods required for a butler implementation
	 */
	public abstract File getImageSeenByButler();
	public abstract void say(String speechtext);
	public abstract void animate(Object[] args);
	public abstract void wakeUp();
	public abstract void sleepBack();
	
	/**
	 * Constructor
	 */
	public AbstractButlerController(){
		this.isEmotionOn = true;
		this.isReactionStarted = false;
		this.isReactionEnded = false;
		
		visionfolderpath = ConfigFile.getInstance().getButlerVisionFolderPath();
	}
	
	/**
	 * This method sets the emotional responses for the butler which is defined by
	 * the abstract animate and say methods per each butler
	 * 
	 * @param e Emotion
	 * @param Command String
	 * @param args Object[]
	 * @throws Exception
	 */
	public void reactByEmotion(Emotion e, String Command, Object[] args) throws Exception{
		String response = Command+", You seem %s today";
		if(args != null){
			ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(args));
			newObj.add(e);
			args = newObj.toArray();
		}
		else{
			args = new Object[]{e};
		}
		setReactionStarted();
			switch(e){
				case Surprised:
					this.say(String.format(response, "Surprised"));
					this.animate(args);
					break;
				case Positive:
					this.say( String.format(response, "Positive"));
					this.animate(args);
					break;
				case Negative:
					this.say(String.format(response, "Sad"));
					this.animate(args);
					break;
				case Neutral:
					this.say(String.format(response, "Calm"));
					this.animate(args);
					break;
				case Angry:
					this.say(String.format(response, "Angry"));
					this.animate(args);
					break;
				case NONE:
					this.say(Command);
					this.animate(args);
					break;
				default:
					this.say(Command);
					this.animate(args);
					break;
		}
		setReactionEnded();
	}
	
	/**
	 * Set Emotion on and off
	 * @param status String
	 */
	public void setEmotion(boolean status) {
		this.isEmotionOn = status;
	}
	
	/**
	 * Get Emotion state 
	 * @return boolean
	 */
	public boolean getEmotionStatus() {
		return this.isEmotionOn;
	}
	
	/**
	 * This is a flag which is set to true when a butler reaction in started
	 */
	public void setReactionStarted() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			this.isReactionStarted = true;
			this.isReactionEnded = false;
		}
	}
	
	/**
	 * This is a flag which is set to true when a butler reaction in ended
	 */
	public void setReactionEnded() {
		this.isReactionStarted = false;
		this.isReactionEnded = true;
	}
	
	/**
	 * This is a flag which is set to true when a butler reaction in progress
	 */
	public boolean isReacting(){
		if(this.isReactionStarted && !this.isReactionEnded){
			return true;
		}
		return false;
	}
	
}
