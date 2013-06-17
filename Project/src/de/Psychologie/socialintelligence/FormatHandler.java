package de.Psychologie.socialintelligence;

/**
* @class FormatHandler
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file FormatHandler.java
*
* @brief //TODO Diese Klasse macht.....
*
* 
*
* 
*/ 
public class FormatHandler {

	// Verwendet fuer Uhrzeitenanzeige, statt 9:3 -> 09:03
	static public String withNull(int time){
		String res;
		if(time < 10){
			res = "0"+String.valueOf(time);
		} else {
			res = String.valueOf(time);
		}
		return res;
	}
}
