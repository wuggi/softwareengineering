package de.Psychologie.socialintelligence;

import java.util.Calendar;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class PopPollActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(PopPollActivity.this);
		
		Alarm pollAlarm = new Alarm();
		
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_poll, menu);
		return true;
	}

}
