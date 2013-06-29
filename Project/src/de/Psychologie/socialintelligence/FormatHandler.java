package de.Psychologie.socialintelligence;

/**
* @class FormatHandler
*  @brief Diese Klasse konvertiert die Uhrzeit in ein ordentliches (HH:MM) Format.
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file FormatHandler.java
*
*
*
* 
*
* 
*/ 
public class FormatHandler {

	// Verwendet fuer Uhrzeitenanzeige, statt 9:3 -> 09:03
	public static String withNull(int time){
		return String.format("%02d", time);
	}
	
	
	public static String implodeArray(String[] inputArray,String glueString){
		String res = "";
		if(inputArray.length > 0){
			StringBuilder sb = new StringBuilder();
			// erste Element einfügen
			sb.append(inputArray[0]);
			// Verkettung + nächsten Wert
			for (int i = 1; i < inputArray.length; i++) {
				sb.append(glueString);
				sb.append(inputArray[i]);
			}
			res = sb.toString();
		}
		return res;
	}
}


