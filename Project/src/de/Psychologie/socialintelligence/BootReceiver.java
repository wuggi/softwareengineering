package de.Psychologie.socialintelligence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// Klasse startet mit dem Systemstart
public class BootReceiver extends BroadcastReceiver {
	private Alarm alarm;
	private SQLHandler db; 
	private Calendar cal;
	private Activity tmp;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// Alarm holen
		alarm = new Alarm();
		// fixtive Activity setzen
		tmp = new Activity();
		// Datenbank Verbindung aufbauen
		db = new SQLHandler(tmp);
		// Kalenderinstanze
		cal = Calendar.getInstance();
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		// Aktuelle Zeit
		Date now = cal.getTime();
		// letzter Alarm
		Date lastAlarm = null;
		// nächster Alarm
		Date nextAlarm = null;
		try {
			lastAlarm = df.parse(db.getLastAlarm());
			nextAlarm = df.parse(db.getNextAlarm());
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		// wurde eine Umfrage verpasst in der Zeit als das Handy aus war?
		if(nextAlarm != null && lastAlarm != null){
			if((now.after(nextAlarm) && now.after(lastAlarm)) && nextAlarm.after(lastAlarm)){
				// Umfrage wurde verpasst
				tmp.startActivity(new Intent(tmp,PopPollActivity.class));
			} else {
				// Alarm wieder einstellen
				alarm.setNextAlarm();
			}
		} 
	}
}