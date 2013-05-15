package de.Psychologie.socialintelligence;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

// TODO: dies ist keine Activity!! Es ben�tigt lediglich 3 Methoden von dieser!
public class Alarm extends Activity {

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
	String curruentAlarmTime;
	String nextAlarmTime;
	

	
	// speichern letzter Alarm
	// speichern n�chster Alarm
	
	// pr�fen ist Alarm gesetzt, sonst n�chsten Alarm aktivieren
	
	Alarm(){
		// Zeit und Datum holen
		cal = Calendar.getInstance();
		
		// Wochentag ist in Android: Sonntag = 1, in unserer Datenbank ist Montag = 0,
		// daher muss (Wochentag+5) mod 7 erfolgen.
		currentWeekDay = (cal.get(Calendar.DAY_OF_WEEK)+5)%7;
		nextWeekDay = (currentWeekDay+1)%7;
		// aktuelles Datum speichern
		currentDate = cal.getTime();
		// n�chstes Datum ermitteln
		cal.add(Calendar.DAY_OF_MONTH, +1);
		nextDate = cal.getTime();
		
		currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cal.get(Calendar.MINUTE);
		// TODO: IF nicht notwendig, else reicht
		if(currentMinute < 2){
			curruentAlarmTime = String.valueOf(currentHour)+":00:00";
		} else {
			curruentAlarmTime = String.valueOf(currentHour)+":"+String.valueOf(currentMinute)+":00";
		}
	}
	
	// setzen n�chsten oder ersten Alarm
	public boolean setNextAlarm(){
		return setNextAlarm(false);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setNextAlarm(boolean firstAlarm){
		int lastHour = firstAlarm?23:19;
		
		boolean res = false;
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(Alarm.this);
		// n�chster Alarm
		Date alarmDay;
		
		// Aktuelle Zeit im letzen Slot des Tages, dann ist es nach 19 Uhr
		if(currentHour > lastHour){
			// erste Zeit vom n�chsten Wochentag holen
			nextAlarmTime = db.getFirstTimeFromDay(nextWeekDay);
			alarmDay = nextDate;
		} else {
			// f�r Heute existiert noch eine Alarmzeit
			nextAlarmTime = db.getNextTimeFromDayTime(currentWeekDay, curruentAlarmTime);
			alarmDay = currentDate;
		}
		
		// neue Alarmzeit geholt
		if(nextAlarmTime.compareTo("00:00:00") != 0){
			// Alarm-Tag (heute oder morgen) mit Uhrzeit versehen
			// TODO: Veraltet!
			alarmDay.setHours(Integer.valueOf(nextAlarmTime.substring(0,2)));
			alarmDay.setMinutes(0);
			alarmDay.setSeconds(5);

			// n�chsten Alarm setzen
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
		SQLHandler db = new SQLHandler(Alarm.this);
		// n�chster Alarm
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, snoozeTime);
		startAlarm();
		db.close();
	}
	
	public int getCurrentWeekDay(){
		return currentWeekDay;
	}
	
	private void startAlarm(){
    	// bei Alarmstart die Umfrage aufrufen
    	// TODO: oder eine Zwischen-Activtiy starten, die lediglich Snooze setzt.
    	// Damit der Start durch den Alarm klar ist
        Intent intent = new Intent(this, PopPollActivity.class);
        // 10000 ist einmalige Nummer f�r den Alarm
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
	}
	
	
	
	// TODO: kann weg
	public boolean appStartByAlarm(){
		boolean res = false;
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(Alarm.this);
		
		// existiert aktuelle Zeit in der Datenbank, dann wurde es durch den 
		// Alarm gestartet
		Log.v("test",String.valueOf(currentWeekDay));
		Log.v("test",curruentAlarmTime);
		if(db.existDayTime(currentWeekDay, curruentAlarmTime)){
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


