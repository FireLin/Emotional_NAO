package com.hlab.common.enums;

/**
 * Emotion Array Enums
 * 
 * @author Hesham Omran
 *
 */
public enum Emotion{ 
	Positive(0),Neutral(1),Negative(2),Surprised(3), Angry(4), NONE(5);
	public int index;
	Emotion(int i){
		this.index = i;
	}
	
	@Override
	public String toString() {
		switch(this){
		case Negative:
			return "Negative";
		case Positive:
			return "Positive";
		case Surprised:
			return "Surprised";
		case Neutral:
			return "Neutral";
		case Angry:
			return "Angry";
		case NONE:
			return null;
		default:
			return "";
		}
	}
}