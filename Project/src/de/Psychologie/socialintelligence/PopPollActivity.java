package de.Psychologie.socialintelligence;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TimePicker;

public class PopPollActivity extends Activity {
	
	Button snooze_button;
	Button ok_button;
	Button cancel_button;
	private TimePicker time;
	private EditText count;
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_poll);
		
		
		// Datenbank Verbindung aufbauen
		final SQLHandler db = new SQLHandler(PopPollActivity.this);
		
		// App startet -> hinterlegten Klingelton abspielen
		// Meldung etc. Wenn Handy gesperrt �ffnet sich zwar die App, aber User bekommt nix von mit :-)
		
		 final Alarm pollAlarm = new Alarm(this);
	
		snooze_button = (Button) findViewById(R.id.snooze_button);
		ok_button=(Button) findViewById(R.id.ok_button);
		cancel_button=(Button) findViewById(R.id.cancel_button);
		
		
		// Wenn Snooze gedr�ck wird
		snooze_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String time= prefs.getString("Sleeptime", "5");
				int snoozetime = Integer.parseInt(time);
				pollAlarm.setSnooze(snoozetime);
				db.setSnoozeActiv(true);
				
			}
		});
		// Wenn OK oder Abbrechen gedr�ckt wird, wird automatisch neuer Alarm gesetzt
		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int hour = time.getCurrentHour();
				int minute = time.getCurrentMinute();
				int contacts = Integer.parseInt(count.getText().toString());
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				//answerTime = cal.
				pollAlarm.setNextAlarm();
				db.setSnoozeActiv(false);
				String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
				db.setLastAlarm(lastAlarmTime);
			//	db.setPollEntry(date, alarmTime, answerTime, false, contacts, hour, minute);
				
			}
		});
		
		cancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int hour = time.getCurrentHour();
				int minute = time.getCurrentMinute();
				int contacts = Integer.parseInt(count.getText().toString());
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
			//test	db.setPollEntry(date, alarmTime)
				pollAlarm.setNextAlarm();
				db.setSnoozeActiv(false);
			//	db.setPollEntry(date, lastAlarmTime, answerTime, true, contacts, hour, minute);
				
			}
		});
		
		
		
		// Wenn OK gedr�ckt
		// letzten Alarm (ist dieser momentane Alarm) setzen
		// Calendar cal = Calendar.getInstance();
		// String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
		// db.setLastAlarm(lastAlarmTime);
		//db.setPollEntry(date, alarmTime, answerTime, abort, contacts, hour, minute)
		
		// Wenn Abbrechen gedr�ckt
		//db.setPollEntry(date, alarmTime)
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_poll, menu);
		return true;
	}

}
