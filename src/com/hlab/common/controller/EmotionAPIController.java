package com.hlab.common.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.hlab.app.model.ConfigFile;
import com.hlab.app.model.EmotionArray;
import com.hlab.common.enums.Emotion;
import com.hlab.emotionanalysisapi.EmotionRestClient;
import com.hlab.emotionanalysisapi.models.FaceAnalysis;
import retrofit2.Response;
/**
 * This class encapsulates the EmotionAPI REST Client which connects to Microsoft Emotion API via
 * the API key
 * 
 * @author Hesham
 *
 */
public class EmotionAPIController {
	
	/**
	 * Constructor to initiate the connection
	 */
	public EmotionAPIController(){
		EmotionRestClient.init(ConfigFile.getInstance().getEmotionAPIKey());
	}
	
	/**
	 * Extracting Emotions for the image file in the data folder captured by the butler 
	 * vision system. The Emotions returned are either postive, negative, surprised or angry
	 * 
	 * @param x File
	 * @return EmotionArray Object
	 */
	public EmotionArray getEmotionsFromImage(File x){
		int scale = 100;
		EmotionArray myEmotions = new EmotionArray();
		Response<FaceAnalysis[]> res = null;
		try {
			res = EmotionRestClient.getInstance()
					.detect("file://"+x.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(res != null){
	    	FaceAnalysis[] faces = res.body();
	    	String status = res.message().toString();
	    	System.out.println(status);
	    	if(faces != null){
	    		myEmotions.setNumberOfPeopleSeen(faces.length);
		    	for(int i=0; i<faces.length; i++){
		    		double positive = faces[i].getScores().getHappiness();
		    		
		    		double negative = faces[i].getScores().getSadness() 
		    						+ faces[i].getScores().getDisgust() 
		    						+ faces[i].getScores().getContempt() 
		    						+ faces[i].getScores().getFear();
		    		
		    		double angry = faces[i].getScores().getAnger();
		    		double neutral = faces[i].getScores().getNeutral();
		    		double suprise = faces[i].getScores().getSurprise();
		    		
		    		myEmotions.addToEmotionValue(Emotion.Positive, positive*scale);
		    		myEmotions.addToEmotionValue(Emotion.Negative, negative*scale);
		    		myEmotions.addToEmotionValue(Emotion.Neutral,  neutral*scale);
		    		myEmotions.addToEmotionValue(Emotion.Surprised,suprise*scale);
		    		myEmotions.addToEmotionValue(Emotion.Angry	  ,angry*scale);
		    	}
		    	
		    	for(Emotion e: Emotion.values()){
		    		System.out.println(e.toString()+" "+myEmotions.getEmotionValue(e));
		    	}
		    	System.out.println("number:"+myEmotions.getNumberOfPeopleWithEmotions());
		    	return myEmotions;
	    	}
		}
		return null;
	}
}
