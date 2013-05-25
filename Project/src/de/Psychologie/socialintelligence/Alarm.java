package de.Psychologie.socialintelligence;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

// TODO: dies ist keine Activity!! Es benötigt lediglich 3 Methoden von dieser!
public class Alarm{

	private Calendar cal;
	// Wochentag
	private int currentWeekDay;
	private int nextWeekDay;
	// Tag des Monats
	private Date currentDate;
	private Date nextDate;
	// Stunde 24er Format
	private int currentHour;
	// Alarmzeiten in Datenbank
	String currentAlarmTime;
	String nextAlarmTime;
	// Activity. welche den Alarm nutzen möchte
	// TODO: sehr unschön...
	Activity source;
	

	
	// speichern letzter Alarm
	// speichern nächster Alarm
	
	// prüfen ist Alarm gesetzt, sonst nächsten Alarm aktivieren
	
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
		// nächstes Datum ermitteln
		cal.add(Calendar.DAY_OF_MONTH, +1);
		nextDate = cal.getTime();
		
		currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cal.get(Calendar.MINUTE);


		// Zeit setzen, mit Aufbau 00:00:00
		currentAlarmTime = withNull(currentHour)+":"+withNull(currentMinute)+":00";
		
	}
	
	// setzen nächsten oder ersten Alarm
	public boolean setNextAlarm(){
		return setNextAlarm(false);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setNextAlarm(boolean firstAlarm){
		int lastHour = firstAlarm?23:19;
		Log.v("test",String.valueOf(lastHour));
		boolean res = false;
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(source);
		Log.v("test",String.valueOf(db.existUserCode()));
		// nächster Alarm
		Date alarmDay;
		Log.v("test",String.valueOf(currentHour));
		// Aktuelle Zeit im letzen Slot des Tages, dann ist es nach 19 Uhr
		if(currentHour > lastHour){
			// erste Zeit vom nächsten Wochentag holen
			nextAlarmTime = db.getFirstTimeFromDay(nextWeekDay);
			alarmDay = nextDate;
		} else {
			// für Heute existiert noch eine Alarmzeit
			Log.v("test",currentAlarmTime);
			
			
			//Fehler wenn heute kein gespeicherter eintrag nach der aktuellen Uhrzeit vorhanden ist.
			//TODO: reparieren
			nextAlarmTime = db.getNextTimeFromDayTime(currentWeekDay, currentAlarmTime);
			
			alarmDay = currentDate;
		}
		
		// neue Alarmzeit geholt
		if(nextAlarmTime.compareTo("00:00:00") != 0){
			// Alarm-Tag (heute oder morgen) mit Uhrzeit versehen
			// TODO: Veraltet!
			alarmDay.setHours(Integer.valueOf(nextAlarmTime.substring(0,2)));
			alarmDay.setMinutes(0);
			alarmDay.setSeconds(5);
			Log.v("test",alarmDay.toString());

			// nächsten Alarm setzen
        	cal.setTime(alarmDay);
        	startAlarm();
        	
            // Alarm wurde erfolgreich gesetzt
			res = true;
		}
		db.close();
		return res;
	}
	
	// setze Snooze in Minuten
	public void setSnooze(int snoozeTime){
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(source);
		// nächster Alarm
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, snoozeTime);
		startAlarm();
		db.close();
	}
	
	public int getCurrentWeekDay(){
		return currentWeekDay;
	}
	
	private String withNull(int time){
		String res;
		if(time < 10){
			res = "0"+String.valueOf(time);
		} else {
			res = String.valueOf(time);
		}
		return res;
	}
	
	private void startAlarm(){
    	// bei Alarmstart die Umfrage aufrufen
    	// Damit der Start durch den Alarm klar ist
        Intent intent = new Intent(source, Alarm_Activity.class);
        // 10000 ist einmalige Nummer für den Alarm
        PendingIntent pendingIntent = PendingIntent.getActivity(source, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)source.getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
	}
	
	
	
	// TODO: kann weg
	public boolean appStartByAlarm(){
		boolean res = false;
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(source);
		
		// existiert aktuelle Zeit in der Datenbank, dann wurde es durch den 
		// Alarm gestartet
		Log.v("test",String.valueOf(currentWeekDay));
		Log.v("test",currentAlarmTime);
		if(db.existDayTime(currentWeekDay, currentAlarmTime)){
			res = true;
		}
		
		db.close();
		return res;
	}
	
	
	/*
	private void alarm (boolean activate) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);

        if(activate == true) {
            int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long interval = 3000;
            long triggerTime = SystemClock.elapsedRealtime();       
            am.setRepeating(type, triggerTime, interval, pi);       
        } else {
            am.cancel(pi);
        }
    }

    private boolean alarmIsSet() {
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }
    */
	

}


