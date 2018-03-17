package com.hlab.common.controller;

import java.io.IOException;
import java.sql.Timestamp;

import com.hlab.common.enums.SpeechCommand;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

/**
 * 
 * 
 * @author Hesham Omran
 *
 */
public class SpeechController {
	
	LiveSpeechRecognizer recognizer;
	Configuration configuration;
	SpeechResult result;
	SpeechCommand lastCommand;
	String speechCommand;
	boolean isRecognitionActivated;
	Timestamp lastActivationTime;
	final int timeToExitSpeech = 60000;
	
	public SpeechController(){
		initSpeechRecognition();
	}
	
	private void initSpeechRecognition(){
		configuration = new Configuration();
		// Set path to acoustic model.
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		// Set path to dictionary.
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		// Set language model.
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");

		configuration.setGrammarPath("data/");
		configuration.setUseGrammar(true);
		configuration.setGrammarName("action");
		lastCommand = null;
		isRecognitionActivated = false;
		lastActivationTime = new Timestamp(0);
	}

	private void checkLastActivationTime(){
		Timestamp current = new Timestamp(System.currentTimeMillis());
		if((Math.abs(current.getTime()-lastActivationTime.getTime())) > timeToExitSpeech){
			isRecognitionActivated = false;
			System.out.println("system is not listening anymore,"
					+ " no speech was detected for "+(timeToExitSpeech/1000)+"seconds");
		}
		else{
			isRecognitionActivated = true;
		}
	}

	public boolean isRecognitionActivated(){
		checkLastActivationTime();
		return isRecognitionActivated;
	}
	
	public SpeechCommand getLastCommand(){
		return lastCommand;
	}
	
	public void startRecognition() throws IOException{
		recognizer = new LiveSpeechRecognizer(configuration);
		// Start recognition process pruning previously cached data.
		recognizer.startRecognition(true);
		System.out.println("Recognition Started");
	}
	
	public void stopRecognition(){
		recognizer.stopRecognition();
		System.out.println("Recognition Stoppd");
	}
	
	public void recognize(){
		checkLastActivationTime();
		result = recognizer.getResult();
	}
	
	public String getRecognizedSpeechString(){
		if(result != null){
			String target = result.getHypothesis();
			return target;
		}
		return null;
	}
	
	public boolean isCurrentCommandSimilarToLastCommand(){
		if(lastCommand != null){
			if(isMatchToCommand(lastCommand)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isMatchToCommand(SpeechCommand command){
		if(isRecognitionActivated){
			if(result != null){
				String target = result.getHypothesis();
				if((command.getFirstCommand().equalsIgnoreCase(target) |
						command.getSecondCommand().equalsIgnoreCase(target))
						&& isKeywordActivation(target) == false){
					System.out.println("recognized");
					lastCommand = command;
					lastActivationTime = new Timestamp(System.currentTimeMillis());
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	private boolean isKeywordActivation(String target){
		if(SpeechCommand.HELLOLINE.getFirstCommand().equalsIgnoreCase(target) |
				SpeechCommand.HELLOLINE.getSecondCommand().equalsIgnoreCase(target)){
			return true;
		}
		return false;
	}
	
	public boolean isMatchToActivationCommand(){
		if(!isRecognitionActivated){
			if(result != null){
				String target = result.getHypothesis();
				if(isKeywordActivation(target)){
					//lastCommand = SpeechCommand.HELLOLINE;
					return true;
				}
			}
		}
		return false;
	}
	
	public void activateRecognition(){
		isRecognitionActivated = true;
		lastActivationTime = new Timestamp(System.currentTimeMillis());
		System.out.println("activation command detected");
	}
}
