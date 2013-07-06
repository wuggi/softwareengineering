package de.Psychologie.socialintelligence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
* @class Alarm
* @brief Alarmzeit und Snoozezeit werden gesetzt
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file Alarm.java 
*/ 
public class Alarm{

	/**
	 * @brief Instanze vom Kalender
	 */
	private Calendar cal;
	// Wochentag
	/**
	 * @brief Wochentag 0 = Montag
	 */
	private int currentWeekDay;
	/**
	 * @brief n�chster Wochentag 0 = Montag
	 */
	private int nextWeekDay;
	/**
	 * @brief Tag des Monats
	 */
	private Date currentDate;
	/**
	 * @brief N�chster Tag
	 */
	private Date nextDate;
	/** 
	 * @brief Stunde 24er Format
	 */
	private int currentHour;
	/**
	 * @brief Alarmzeiten in Datenbank
	 */
	private String currentAlarmTime;
	/**
	 * @brief n�chster Alarm aus der Datenbank
	 */
	private String nextAlarmTime;
	/**
	 * @brief Activity welche den Alarm nutzen moechte
	 */

	private Activity source;
	
	// speichern letzter Alarm
	// speichern naechster Alarm
	
	// pruefen ist Alarm gesetzt, sonst naechsten Alarm aktivieren
	/**
	 * @brief speichern letzter Alarm, speichern nächster Alarm <br> pruefen ist Alarm gesetzt, sonst naechsten Alarm aktivieren
	 * @param source
	 * 
	 */
	Alarm(Activity source){
		// Zeit und Datum holen
		cal = Calendar.getInstance();
		this.source = source;
		// Wochentag ist in Android: Sonntag = 1, in unserer Datenbank ist Montag = 0,
		// daher muss (Wochentag+5) mod 7 erfolgen.
		currentWeekDay = (cal.get(Calendar.DAY_OF_WEEK)+5)%7;
		nextWeekDay = (currentWeekDay+1)%7;
		// aktuelles Datum speichern
		currentDate = cal.getTime();
		// naechstes Datum ermitteln
		cal.add(Calendar.DAY_OF_MONTH, +1);
		nextDate = cal.getTime();
		
		currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cal.get(Calendar.MINUTE);


		SQLHandler db = new SQLHandler(source);
		if(!db.getSnoozeActiv()){
			// Zeit setzen, mit Aufbau 00:00:00
			currentAlarmTime = FormatHandler.withNull(currentHour)+":"+FormatHandler.withNull(currentMinute)+":00";	
			db.setCurrentAlarm(currentAlarmTime);
		} else {
			currentAlarmTime = db.getCurrentAlarm();
		}
		//Log.v("currentAlarmTime",currentAlarmTime);
	}

	
	// setzen naechsten oder ersten Alarm
	/**
	 * @brief Setzen des naechsten oder ersten Alarm <br>
	 * ruft dazu die Funktion {@link setNextAlarm(boolean firstAlarm)} auf
	 * @return {@code setNextAlarm(false)}
	 */
	public boolean setNextAlarm(){
		return setNextAlarm(false);
	}
	
	/**
	 * 
	 * @param firstAlarm
	 * @return res
	 * @brief Setzen des naechsten oder ersten Alarm
	 */
	public boolean setNextAlarm(boolean firstAlarm){
		int lastHour = firstAlarm?23:19;
		//Log.v("test","lastHour " + String.valueOf(lastHour));
		boolean res = false;
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(source);
		//Log.v("test","usercode: " + String.valueOf(db.existUserCode()));
		// naechster Alarm
		Date alarmDay;
		//Log.v("test","next Alarm: " + String.valueOf(currentHour));
		// Aktuelle Zeit im letzen Slot des Tages, dann ist es nach 19 Uhr
		if(currentHour >= lastHour){
			// erste Zeit vom naechsten Wochentag holen
			nextAlarmTime = db.getFirstTimeFromDay(nextWeekDay);
			alarmDay = nextDate;
		} else {
			// fuer Heute existiert noch eine Alarmzeit
			//Log.v("test","heute: " + currentAlarmTime);
			
			nextAlarmTime = db.getNextTimeFromDayTime(currentWeekDay, currentAlarmTime);
			
			alarmDay = currentDate;
		}
		
		//Log.v("neue Alarmzeit",nextAlarmTime);
		
		// neue Alarmzeit geholt
		if(nextAlarmTime.compareTo("00:00:00") != 0){
			// Alarm-Tag (heute oder morgen) mit Uhrzeit versehen
			// TODO: Veraltet!
			alarmDay.setHours(Integer.valueOf(nextAlarmTime.substring(0,2)));
			alarmDay.setMinutes(0);
			alarmDay.setSeconds(5);
			//Log.v("test",alarmDay.toString());

			// letzen Alarm setzen
			if(!firstAlarm){
				db.setLastAlarm(currentAlarmTime);
			} 
			// naechsten Alarm setzen
			db.setNextAlarm(FormatHandler.withNull(alarmDay.getHours())+":"+FormatHandler.withNull(alarmDay.getMinutes())+":00");
        	cal.setTime(alarmDay);
        	startAlarm();
        	
            // Alarm wurde erfolgreich gesetzt
			res = true;
		}
		db.close();
		return res;
	}
	
	// setze Snooze in Minuten
	/**
	 * 
	 * @param snoozeTime
	 * @brief Setzt Snooze in Minuten
	 */
	public void setSnooze(int snoozeTime){
		// Datenbank Verbindung aufbauen
		//SQLHandler db = new SQLHandler(source);
		// naechster Alarm
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, snoozeTime);
		startAlarm();
		//db.close();
	}
	
	/**
	 * @brief holt n�chste Alarmzeit aus der Datenbank
	 * @return Alarmzeit hh:mm:ss
	 */
	public String getNextAlarm(){
		String nextAlarm;
		SQLHandler db = new SQLHandler(source);
		if(currentHour >= 23){
			// erste Zeit vom naechsten Wochentag holen
			nextAlarm = db.getFirstTimeFromDay(nextWeekDay);
		} else {
			nextAlarm = db.getNextTimeFromDayTime(currentWeekDay, currentAlarmTime);
		}
		db.close();
		return nextAlarm;
	}
	
	/**
	 * @brief Differenz in Minuten zwischen aktueller Zeit und n�chstem Alarm
	 * @return Differenz in Minuten
	 */
	public int getDifferenceToNextAlarm(){
		// Parser f�r Uhrzeit, setzt Datum auf 01.01.1970
		DateFormat df = new SimpleDateFormat("hh:mm:ss");
		
		// Zeiten erstellen mit aktuellen Tag, Uhrzeit
		Date currentTime = new Date();
		Date nextTime = new Date();
		
		// aktuelles Datum auf 01.01.1970 setzen
		// magic 70 -> 1970 Sollte mit Calendar sauber gemacht werden!
		// es existiert nur die Uhrzeit, daher ein Standarddatum nehmen.
		currentTime.setYear(70);
		currentTime.setMonth(0);
		currentTime.setDate(1);

		// n�chsten Alarm auslesen
		try {
			nextTime = df.parse(getNextAlarm());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//Log.v("cur",currentTime.toGMTString());
		//Log.v("nex",nextTime.toGMTString());
		
		// n�chster AlarmUhrzeit kleiner als aktuelle Uhrzeit, ist es der morgige Tag
		if(nextTime.getHours() < currentTime.getHours()){
			nextTime.setDate(2);
		}
		//Log.v("nex",nextTime.toGMTString());
		
		// Uhrzeit in Millisekunden
		long cT = currentTime.getTime();
		//Log.v("aktuelleZeit",String.valueOf(cT));
		// Uhrzeit in Millisekunden
		long nT = nextTime.getTime();
		//Log.v("n�chste Zeit",String.valueOf(nT));
		// return difference in minutes
		return (int) (nT - cT)/60000;
	}
	/**
	 * 
	 * @return currentWeekDay
	 * @brief Liefert den aktuellen Tag der Woche
	 */
	public int getCurrentWeekDay(){
		return currentWeekDay;
	}
	
	/**
	 * @brief Stoppt Snooze
	 */
	//Intent must be exactly the same!!!
	public void stopSnooze(){
		// bei Alarmstart die Umfrage aufrufen
    	// Damit der Start durch den Alarm klar ist
        Intent intent = new Intent(source, Alarm_Activity.class);
        // 10000 ist einmalige Nummer fuer den Alarm
        PendingIntent pendingIntent = PendingIntent.getActivity(source, 10000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)source.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
	}
	 
	/**
	 * @brief startet AlarmManager
	 */
	private void startAlarm(){
		stopSnooze();
    	// bei Alarmstart die Umfrage aufrufen
    	// Damit der Start durch den Alarm klar ist
        Intent intent = new Intent(source, Alarm_Activity.class);
        // 10000 ist einmalige Nummer fuer den Alarm
        PendingIntent pendingIntent = PendingIntent.getActivity(source, 10000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        AlarmManager am = (AlarmManager)source.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
	}
	
	/**
	 * @brief Liefert die aktuelle Alarmzeit
	 * @return currentAlarmTime
	 */
	public String getCurrentAlarmTime() {
		return currentAlarmTime;
	}
		
}


