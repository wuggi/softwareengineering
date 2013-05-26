package de.Psychologie.socialintelligence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class PopPollActivity extends Activity {

	private static final int HITHERE_ID = 1;
	private Button snooze_button;
	private Button ok_button;
	private Button cancel_button;
	private TimePicker timepicker;
	private EditText countContact;
	private TextView txtPopPollInfo;
	private Alarm pollAlarm;
	private int maxHourforContact = 5;
	private Calendar cal;
	private SharedPreferences prefs;
	private SQLHandler db; 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityRegistry.register(this);
		setContentView(R.layout.activity_pop_poll);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		
		
		// Kalender Instanze setzen
		cal = Calendar.getInstance();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(PopPollActivity.this);
		// Datenbank Verbindung aufbauen
		db = new SQLHandler(PopPollActivity.this);
		
		// App startet -> hinterlegten Klingelton abspielen
		// Meldung etc. Wenn Handy gesperrt ï¿½ffnet sich zwar die App, aber User bekommt nix von mit :-)
		
		pollAlarm = new Alarm(this);
	
		snooze_button = (Button) findViewById(R.id.snooze_button);
		ok_button=(Button) findViewById(R.id.ok_button);
		cancel_button=(Button) findViewById(R.id.cancel_button);
		
		// Eingabefeld Kontaktpersonen
		countContact=(EditText) findViewById(R.id.countContact);
		countContact.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if(countContact.getText().toString().length() > 0){
					ok_button.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		
		
		// TODO: parsen der Zeit
		// TODO: Fettgedruckt, obwohl HTML korrekt verwendet
		// TODO: Zeiten nur im HH:mm Format ausgeben
		txtPopPollInfo = (TextView) findViewById(R.id.txtPopPollInfo);
		String lastAlarm = db.getLastAlarm();
		if(lastAlarm.compareTo("00:00:00") == 0){
			txtPopPollInfo.setText(Html.fromHtml(getResources().getString(R.string.txtPopPollInfo0)));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Date lastTime = cal.getTime();
			try {
				lastTime = sdf.parse(lastAlarm);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date nowTime = cal.getTime();
			
			if(lastTime.compareTo(nowTime) < 0){
				// heute
				txtPopPollInfo.setText(Html.fromHtml(getResources().getString(R.string.txtPopPollInfo1, lastAlarm.substring(0,5))));	
			} else{
				// gestern
				txtPopPollInfo.setText(Html.fromHtml(getResources().getString(R.string.txtPopPollInfo2, lastAlarm.substring(0,5))));
			}
		}
	
		
		// Zeitauswahl
		timepicker=(TimePicker) findViewById(R.id.timePicker);
		timepicker.setIs24HourView(true);
		timepicker.setCurrentHour(1);
		timepicker.setCurrentMinute(0);
		timepicker.setOnTimeChangedListener(StartTimeChangedListener);
		
		// Wenn Snooze gedrï¿½ck wird
		snooze_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				//Snoozezeit aus den Settings auslesen, sonst 5 Minuten
//				String time= prefs.getString("Sleeptime", "5 Minuten");
//				int snoozetime = Integer.parseInt(time);
//				pollAlarm.setSnooze(snoozetime);
//				db.setSnoozeActiv(true);
//				// App beenden
//				ActivityRegistry.finishAll();
				setSnooze();
			}
		});
		
		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Gesamtdauer der Kontakte
				int hour = timepicker.getCurrentHour();
				int minute = timepicker.getCurrentMinute();
				//Anzahl der Kontakte
				int contacts = Integer.parseInt(countContact.getText().toString());
				Calendar cal = Calendar.getInstance();
				//Zeitpunkt der Antwort
				String answerTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
				
				//Datum
				String date = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
				//Alarmzeit
				String alarmTime=pollAlarm.getCurrentAlarmTime();
				//String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
				//nÃ¤chsten Alarm setzen
				pollAlarm.setNextAlarm();
				db.setSnoozeActiv(false);
				// TODO: LastAlarm gehört zu Alarm
				//db.setLastAlarm(lastAlarmTime);
				db.setPollEntry(date, alarmTime, answerTime, false, contacts, hour, minute);
				// Meldung
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtPopPollOK), Toast.LENGTH_LONG).show();
				ActivityRegistry.finishAll();
				//if abfrage fehlt, ob nicht mehr Stunden vom letzten Alarmpunkt ausgewÃ¤lt wurden
				
			}
		});
		
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PopPollActivity.this);
				builder.setTitle(getResources().getString(R.string.txtPopPollBreakTitle));
		        builder.setMessage(getResources().getString(R.string.txtPopPollBreakText))
		               .setCancelable(false)
		               .setPositiveButton(getResources().getString(R.string.txtYes), new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		           				// Umfrage speichern
			    				String date = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
			    				String alarmTime=pollAlarm.getCurrentAlarmTime();
	
			    				pollAlarm.setNextAlarm();
			    				db.setSnoozeActiv(false);
			    				//Werte in die DB eintragen
			    				db.setPollEntry(date, alarmTime);
			    				Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtPopPollBreak), Toast.LENGTH_LONG).show();
			    				// Alle Activitys beenden
			    				ActivityRegistry.finishAll();
		                   }
		               })
		               .setNegativeButton(getResources().getString(R.string.txtNo), new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   dialog.cancel();
		                   }
		               });
		        AlertDialog alert = builder.create();
		        alert.show();
			}
		});
		
		
		
		// Wenn OK gedrï¿½ckt
		// letzten Alarm (ist dieser momentane Alarm) setzen
		// Calendar cal = Calendar.getInstance();
		// String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
		// db.setLastAlarm(lastAlarmTime);
		//db.setPollEntry(date, alarmTime, answerTime, abort, contacts, hour, minute)
		
		// Wenn Abbrechen gedrï¿½ckt
		//db.setPollEntry(date, alarmTime)
		
	}
	
	public void onBackPressed() {
		setSnooze();
	}
	
	// TODO: setSnooze und set Notification werden in Alarm_Activity auch genutzt (fast 1:1)
	private void setSnooze(){
		//Snoozezeit aus den Settings auslesen, sonst 5 Minuten
		String time= prefs.getString("Sleeptime", "5");
		int snoozetime = Integer.parseInt(time);
		pollAlarm.setSnooze(snoozetime);
		db.setSnoozeActiv(true);
		// Meldung
		Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtPopPollSnooze), Toast.LENGTH_LONG).show();
		// Notification setzen
		setNotification();
		// App beenden
		ActivityRegistry.finishAll();
	}
	
	@SuppressWarnings("deprecation")
	private void setNotification(){
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		// Meldung (im Durchlauf) definieren
		int icon          = R.drawable.ic_launcher;
		CharSequence text = "Schlummerfunktion aktiv!";
		long time         = System.currentTimeMillis();
		
		// Meldung setzen
		Notification notification = new Notification(icon, text, time);
		
		// Meldung schließen
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Meldungstext, wenn gewählt
		Context context = getApplicationContext();
		CharSequence contentTitle = "Umfrage";
		CharSequence contentText  = "Bitte beantworten Sie die Umfrage.";
		
		Intent notificationIntent = new Intent(this, this.getClass());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, notificationIntent, 1);
	
		// Ton hinzufügen
		//notification.defaults |= Notification.DEFAULT_SOUND;
		
		// Vibration benötigt zusätzliches Recht
		//notification.defaults |= Notification.DEFAULT_VIBRATE;
		
		// Licht
		//notification.defaults |= Notification.DEFAULT_LIGHTS;

		
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		//notification.setLatestEventInfo(context, contentTitle, contentText, null);
		
		// NotificationManager bekommt Meldung
		notificationManager.notify(HITHERE_ID, notification);
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
