package com.hlab.app.console;

import java.io.IOException;

import com.hlab.app.model.EmotionArray;
import com.hlab.common.controller.AbstractButlerController;
import com.hlab.common.controller.EmotionAPIController;
import com.hlab.common.controller.SpeechController;
import com.hlab.common.controller.butler.DummyButlerController;
import com.hlab.common.controller.butler.NAOButlerController;
import com.hlab.common.enums.SpeechCommand;



/**
 * @author homran
 *
 */
public class Main {
	
	private static SpeechController speechcontrol;
	private static AbstractButlerController butler;
	private static EmotionAPIController emotioncontrol;
	
	public Main() throws Throwable{
		try {
			initButler(1);
			initSpeechRecognition();
			Main.butler.animate(new Object[]{"STAND"});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initButler(int num) throws Throwable, Exception{
		switch(num){
		case 0:
			Main.butler = new DummyButlerController();
			break;
		case 1:
			Main.butler = new NAOButlerController();
			break;
		default:
			Main.butler = new DummyButlerController();
		}
	}
	
	private void initSpeechRecognition(){
		try {
				speechcontrol = new SpeechController();
				speechcontrol.startRecognition();
				SpeechReaction R1 = new SpeechReaction( "Speech Recognizer");
			    R1.start();
			} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	class SpeechReaction implements Runnable {
		   private Thread t;
		   private String threadName;
		   
		   SpeechReaction(String name) {
		      threadName = name;
		      System.out.println("Creating " +  threadName );
		   }
		   
		   public void run(){
			   while(true){
				   if(!Main.butler.isReacting()){
					   	Main.speechcontrol.recognize();
					   	boolean isOn = Main.speechcontrol.isMatchToActivationCommand();
					   	if(isOn == true){
					   		Main.butler.wakeUp();
					   		speechcontrol.activateRecognition();
					   		System.out.println("system now listening");
					   	}
					   	if(speechcontrol.isRecognitionActivated()){
				    		applyRules();
				    	}
				   }
			   }
		    }
		   
		   private void applyRules(){
			   System.out.println(speechcontrol.getRecognizedSpeechString());
			   
			   if(speechcontrol.isMatchToCommand(SpeechCommand.HOWIFEEL)){
				   try {
						EmotionArray tmp = emotioncontrol.getEmotionsFromImage(
								Main.butler.getImageSeenByButler());
						/* checkForEmotions(tmp); */
						butler.reactByEmotion(tmp.getDominantEmotion()
								   ,"well",null);
					} catch (Exception e) {
						e.printStackTrace();
					}
			   }
			   else if(speechcontrol.isMatchToCommand(SpeechCommand.HOWUFEEL)){
				   Main.butler.say("I am doing well, thank you");
			   }
		   	}
		   
		    public void start () {
		      System.out.println("Starting " +  threadName );
		      if (t == null) {
		         t = new Thread (this, threadName);
		         t.start ();
		      }
		   }
		}
	
	
	public static void main(String[] args) throws Throwable{
		new Main();
	}
}
