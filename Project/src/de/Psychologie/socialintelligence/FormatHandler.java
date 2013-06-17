package de.Psychologie.socialintelligence;

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
