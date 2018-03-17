package com.hlab.app.model;

import java.util.ArrayList;

import com.hlab.common.enums.Emotion;

/**
 * This class holds the emotional confidence interval of each of detected emotion in an image, in
 * addition to the number of people that where spotted in the image.
 *  
 * @author Hesham
 *
 */
public class EmotionArray {
	ArrayList<Double> emotions;
	int numberOfPeople;
	
	/**
	 * Constructor
	 */
	public EmotionArray(){
		numberOfPeople = 0;
		emotions = new ArrayList<Double>();
		for(int i=0; i<Emotion.values().length; i++){
			emotions.add(new Double(0));
		}
		System.out.println(emotions.size());
	}
	
	/**
	 * This method gets the confidence level of a specific emotion
	 * @param e Emotion
	 * @return double
	 */
	public double getEmotionValue(Emotion e){
		return this.emotions.get(e.index);
	}
	
	/**
	 * This method set the value of the confidence level of a specific emotion
	 * @param e Emotion
	 * @return double
	 */
	public void setEmotionValue(Emotion e, double value){
		this.emotions.set(e.index, value);
	}
	
	/**
	 * This method add to the value of a specific emotion
	 * @param e Emotion
	 * @param value double
	 * @return double
	 */
	public double addToEmotionValue(Emotion e, double value){
		double finalvalue = this.emotions.get(e.index) + value;
		this.emotions.set(e.index, finalvalue);
		return this.emotions.get(e.index);
	}
	
	/**
	 * This method sets the number of people seen in an image
	 * @param number int
	 */
	public void setNumberOfPeopleSeen(int number){
		this.numberOfPeople = number;
	}
	
	/**
	 * This method gets the number of people seen in an image
	 * @param number int
	 */
	public int getNumberOfPeopleWithEmotions(){
		return this.numberOfPeople;
	}
	
	/**
	 * This method gets the emotion with the maximum confidence level 
	 */
	public Emotion getDominantEmotion(){
		int x = maxIndex(this.emotions);
		for(Emotion e: Emotion.values()){
			if(x == e.index){
				return e;
			}
		}
		return Emotion.Neutral;
	}
	
	/**
	 * Method to return maximum value in a list
	 * @param list List<Double>
	 * @return
	 */
	private int maxIndex(ArrayList<Double> list) {
		  int i=0, maxIndex=-1;
		  Double max=null;
		  for (Double x : list) {
		    if ((x!=null) && ((max==null) || (x>max))) {
		      max = x;
		      maxIndex = i;
		    }
		    i++;
		  }
		  return maxIndex;
	}	
}
