package de.Psychologie.socialintelligence;

import java.util.Calendar;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class PopPollActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_poll);
		
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(PopPollActivity.this);
		
		// App startet -> hinterlegten Klingelton abspielen
		// Meldung etc. Wenn Handy gesperrt öffnet sich zwar die App, aber User bekommt nix von mit :-)
		
		Alarm pollAlarm = new Alarm(this);
		
		// Wenn Snooze gedrück wird:
		// int snoozeTime = hole ZEIT von JENS (in Minuten)
		// pollAlarm.setSnooze(snoozeTime);
		// Snooze aktivieren
		// db.setSnoozeActiv(true);
		
		// Wenn OK oder Abbrechen gedrückt wird, wird automatisch neuer Alarm gesetzt
		// pollAlarm.setNextAlarm();
		// Snooze deaktivieren
		// db.setSnoozeActiv(false);
		
		
		// Wenn OK gedrückt
		// letzten Alarm (ist dieser momentane Alarm) setzen
		// Calendar cal = Calendar.getInstance();
		// String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
		// db.setLastAlarm(lastAlarmTime);
		//db.setPollEntry(date, alarmTime, answerTime, abort, contacts, hour, minute)
		
		// Wenn Abbrechen gedrückt
		//db.setPollEntry(date, alarmTime)
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_poll, menu);
		return true;
	}

}
