/**
 * 
 */
package com.hlab.app.model;

import java.util.List;

/**
 * @author homran
 *
 */
public class AppSettings {
	protected static boolean isLiveSpeech;
	
	public static void setIsAllowedLiveSpeech(boolean isallowed){
		isLiveSpeech = isallowed;
	}
	
	public static boolean getIsAllowedLiveSpeech(){
		return isLiveSpeech;
	}
	
	
}
