package de.Psychologie.socialintelligence;

/**
* @class FormatHandler
* @brief Diese Klasse konvertiert mehrere Typen in ein Wunsch Format als String.
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file FormatHandler.java
*/ 
public class FormatHandler {

	/**
	 * @brief Ersetzt Integer zu zweistelligen String: 9 -> 09
	 * @param time Zahl
	 * @return String mit führender Null bei einstelligen Zahlen
	 */
	public static String withNull(int number){
		return String.format("%02d", number);
	}
	
	/**
	 * @brief Erstellt aus einem Array einen String
	 * @param inputArray String-Array
	 * @param glueString String zum Verbinden der Elemente
	 * @return String mit allen Array-Elementen inkl. Verbindungselement
	 */
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


