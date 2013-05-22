package de.Psychologie.socialintelligence;

import java.util.Calendar;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class PopPollActivity extends Activity {

	Button snooze_button;
	Button ok_button;
	Button cancel_button;
	private TimePicker timepicker;
	private EditText count;
	private Alarm pollAlarm;
	private int maxHourforContact = 5;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_poll);
		
		;
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PopPollActivity.this);
		// Datenbank Verbindung aufbauen
		final SQLHandler db = new SQLHandler(PopPollActivity.this);
		
		// App startet -> hinterlegten Klingelton abspielen
		// Meldung etc. Wenn Handy gesperrt �ffnet sich zwar die App, aber User bekommt nix von mit :-)
		
		pollAlarm = new Alarm(this);
	
		snooze_button = (Button) findViewById(R.id.snooze_button);
		ok_button=(Button) findViewById(R.id.ok_button);
		cancel_button=(Button) findViewById(R.id.cancel_button);
		timepicker=(TimePicker) findViewById(R.id.timePicker);
		timepicker.setIs24HourView(true);
		timepicker.setCurrentHour(1);
		timepicker.setCurrentMinute(0);
		timepicker.setOnTimeChangedListener(StartTimeChangedListener);
		
		// Wenn Snooze gedr�ck wird
		snooze_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Snoozezeit aus den Settings auslesen, sonst 5 Minuten
				String time= prefs.getString("Sleeptime", "5 Minuten");
				int snoozetime = Integer.parseInt(time);
				pollAlarm.setSnooze(snoozetime);
				db.setSnoozeActiv(true);
				
			}
		});
		
		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Gesamtdauer der Kontakte
				int hour = timepicker.getCurrentHour();
				int minute = timepicker.getCurrentMinute();
				//Anzahl der Kontakte
				int contacts = Integer.parseInt(count.getText().toString());
				Calendar cal = Calendar.getInstance();
				//Zeitpunkt der Antwort
				String answerTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
				
				//Datum
				String date = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
				//Alarmzeit
				String alarmTime=pollAlarm.currentAlarmTime;
				String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
				//nächsten Alarm setzen
				pollAlarm.setNextAlarm();
				db.setSnoozeActiv(false);
				db.setLastAlarm(lastAlarmTime);
				db.setPollEntry(date, alarmTime, answerTime, false, contacts, hour, minute);
				
				//if abfrage fehlt, ob nicht mehr Stunden vom letzten Alarmpunkt ausgewält wurden
				
			}
		});
		
		cancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				String date = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
				String alarmTime=pollAlarm.currentAlarmTime;

				pollAlarm.setNextAlarm();
				db.setSnoozeActiv(false);
				//Werte in die DB eintragen
				db.setPollEntry(date, alarmTime);
				
				
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

	private TimePicker.OnTimeChangedListener StartTimeChangedListener = new TimePicker.OnTimeChangedListener() {

		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			updateDisplay(view, hourOfDay, minute);
		}
	};

	private TimePicker.OnTimeChangedListener NullTimeChangedListener = new TimePicker.OnTimeChangedListener() {

		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

		}
	};

	private void updateDisplay(TimePicker timePicker, int hourOfDay, int minute) {
		int nextMinute = 0;
		int nextHour = 0;
		timePicker.setOnTimeChangedListener(NullTimeChangedListener);
		// Minuten - Abstand
		if (minute >= 45 && minute <= 59)
			nextMinute = 45;
		else if (minute >= 30)
			nextMinute = 30;
		else if (minute >= 15)
			nextMinute = 15;
		else if (minute > 0)
			nextMinute = 0;
		else {
			nextMinute = 45;
		}

		if (minute - nextMinute == 1) {
			if (minute >= 45 && minute <= 59)
				nextMinute = 00;
			else if (minute >= 30)
				nextMinute = 45;
			else if (minute >= 15)
				nextMinute = 30;
			else if (minute > 0)
				nextMinute = 15;
			else {
				nextMinute = 15;
			}
		}
		timePicker.setCurrentMinute(nextMinute);
		
		
		if(hourOfDay == 23){
			nextHour = maxHourforContact;
		} else if (hourOfDay > maxHourforContact){
			nextHour = 0;
		} else {
			nextHour = hourOfDay;
		}
	
		
		timePicker.setCurrentHour(nextHour);
		timePicker.setOnTimeChangedListener(StartTimeChangedListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_poll, menu);
		return true;
	}

}
