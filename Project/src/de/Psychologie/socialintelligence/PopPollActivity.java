package de.Psychologie.socialintelligence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
* @class PopPollActivity
* @brief Umfrage-Activity
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file PopPollActivity.java
*/ 
@SuppressLint("SimpleDateFormat")
public class PopPollActivity extends Activity {
	
	private static final int SNOOZE_ID = 111;
	private final int minPerHour = 60; 
	private Button snooze_button;
	private Button ok_button;
	private Button cancel_button;
    private NumberPicker hourPicker;
	private NumberPicker minutePicker;
	/**
	 * @brief Eingabefeld für die Anzahl der Komtakte
	 */
	private EditText countContact;
    private Alarm pollAlarm;
	private Calendar cal;
	private SharedPreferences prefs;
	private SQLHandler db; 
	private NotificationManager notificationManager = null;

	private boolean action_done = false;
	private int difHour;
	private int difMinute;
	
	/**
	 * @brief //TODO
	 */
	@SuppressWarnings("deprecation")
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
		// Meldung etc. Wenn Handy gesperrt �ffnet sich zwar die App, aber User bekommt nix von mit :-)
	
		pollAlarm = new Alarm(this);

		snooze_button = (Button) findViewById(R.id.snooze_button);
		snooze_button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		ok_button=(Button) findViewById(R.id.ok_button);
		ok_button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
		cancel_button = (Button) findViewById(R.id.cancel_button);
        cancel_button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
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
			
		// Blendet Tastatur aus, sobald au�erhalb des Feldes zur Eingabe geklickt wird.
		LinearLayout completeView = (LinearLayout) findViewById(R.id.LinearLayout1);
		completeView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(countContact.getWindowToken(), 0);
			}
		});



        TextView txtPopPollInfo = (TextView) findViewById(R.id.txtPopPollInfo);
		
		String lastAlarm = db.getLastAlarm();
		if(lastAlarm.compareTo("00:00:00") == 0){
			txtPopPollInfo.setText(Html.fromHtml(getResources().getString(R.string.txtPopPollInfo0)));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Date lastTime = cal.getTime();
			try {
				lastTime = sdf.parse(lastAlarm);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Date nowTime = cal.getTime();
			
			if(lastTime.compareTo(nowTime) < 0){
				// heute
				txtPopPollInfo.setText(Html.fromHtml(getResources().getString(R.string.txtPopPollInfo1, lastAlarm.substring(0, 5))));
			} else{
				// gestern
				txtPopPollInfo.setText(Html.fromHtml(getResources().getString(R.string.txtPopPollInfo2, lastAlarm.substring(0, 5))));
			}
		}
	
		// Zeitdifferenz
		int lastHour = Integer.valueOf(lastAlarm.substring(0,2));
		int lastMinute = Integer.valueOf(lastAlarm.substring(3,5));
		Log.v("oldHour",String.valueOf(lastHour));
		Log.v("oldMin",String.valueOf(lastMinute));
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cal.get(Calendar.MINUTE);
		Log.v("newHour",String.valueOf(currentHour));
		Log.v("newMin",String.valueOf(currentMinute));
	
		Calendar oldAlarm = Calendar.getInstance();
		oldAlarm.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), lastHour, lastMinute);
		
		long diffInMs = Math.abs(cal.getTimeInMillis() - oldAlarm.getTimeInMillis());
		
		long diffInMS = cal.getTimeInMillis() - oldAlarm.getTimeInMillis();
		// If oldAlarm was the day before e.g. old= 23:59 new= 1:00 diff=1:01 and NOT diff=22:59
		// must add 24h (86400000ms)
		if (diffInMS<0) 
			diffInMS+= 86400000;
		
		long diffInMin = (long) (diffInMs/60000);
		Log.v("DiffInMin","="+String.valueOf(diffInMin));
		
		difHour = (int) (diffInMin/minPerHour);
		difMinute = (int) (diffInMin%minPerHour);
		
		// Zeitauswahl
		hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
		//TODO: 12:04:24.130 E/AndroidRuntime(11067): java.lang.RuntimeException: Unable to start activity ComponentInfo{[..]PopPollActivity}: java.lang.IllegalArgumentException: maxValue must be >= 0
		hourPicker.setMaxValue(difHour);
		hourPicker.setMinValue(0);
		hourPicker.setFocusable(true);
		hourPicker.setFocusableInTouchMode(true);
		
		minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
		if(difHour == 0){
			minutePicker.setMaxValue(difMinute);
		} else {
			minutePicker.setMaxValue(59);
		}
		minutePicker.setMinValue(0);
		minutePicker.setFocusable(true);
		minutePicker.setFocusableInTouchMode(true);
		
		hourPicker.setOnValueChangedListener(new OnValueChangeListener(){

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if(hourPicker.getValue() == difHour){
					minutePicker.setMaxValue(difMinute);
				} else {
					minutePicker.setMaxValue(59);
				}
				
			}
			
		});
		
		// Wenn Snooze gedr�ck wird
		snooze_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setSnooze();
				ActivityRegistry.finishAll();
				action_done=true;
			}
		});
		
		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Gesamtdauer der Kontakte
				int hour = hourPicker.getValue();
				int minute = minutePicker.getValue();
				//Anzahl der Kontakte
				int contacts = Integer.parseInt(countContact.getText().toString());
				Calendar cal = Calendar.getInstance();
				//Zeitpunkt der Antwort
				String answerTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
				//Datum
				String date = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+1+"."+cal.get(Calendar.YEAR);
				//Alarmzeit
				String alarmTime=pollAlarm.getCurrentAlarmTime();
				//String lastAlarmTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":00";
				//nächsten Alarm setzen
				pollAlarm.setNextAlarm();
				db.setSnoozeActiv(false);
				action_done=true;
				db.setPollEntry(date, alarmTime, answerTime, false, contacts, hour, minute);
				// Meldung
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtPopPollOK), Toast.LENGTH_LONG).show();
				ActivityRegistry.finishAll();			
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
                                String date = cal.get(Calendar.DAY_OF_MONTH) + "." + cal.get(Calendar.MONTH)+1 + "." + cal.get(Calendar.YEAR);
                                String alarmTime = pollAlarm.getCurrentAlarmTime();

                                pollAlarm.setNextAlarm();
                                db.setSnoozeActiv(false);
                				action_done=true;
                                //Werte in die DB eintragen
                                db.setPollEntry(date, alarmTime);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.txtPopPollBreak), Toast.LENGTH_LONG).show();
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
	
	/**
	 * @brief Notification wird gelöscht und Snooze wird auf inaktiv gesetzt
	 */
	public void onStart(){
		//Delete notification
		cancelNotification();
        db.setSnoozeActiv(false);
        action_done = false;
		super.onStart();
	}
	/**
	 * @brief Wenn die Umfrage nicht beantwortet wurde wird Snooze gesetzt und pausiert
	 */
	@Override
	protected void onPause(){
		if (!action_done)
			setSnooze();		
		super.onPause();
		
	}
	
	/**
	 * @brief Snoozezeit wird gesetzt
	 */
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
	}
	/**
	 * @brief Notification wird gesetzt
	 */
	@SuppressWarnings("deprecation")
	private void setNotification(){
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		// Meldung (im Durchlauf) definieren
		int icon          = R.drawable.ic_stat_notify;
		CharSequence text = "Schlummerfunktion aktiv!";
		long time         = System.currentTimeMillis();
		
		// Meldung setzen
		Notification notification = new Notification(icon, text, time);
		
		// Meldung schliessen
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Meldungstext, wenn gewaehlt
		Context context = getApplicationContext();
		CharSequence contentTitle = "Umfrage";
		CharSequence contentText  = "Bitte beantworten Sie die Umfrage.";
		
		Intent notificationIntent = new Intent(this, this.getClass());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, notificationIntent, 1);
	
		// Ton hinzuf�gen
		//notification.defaults |= Notification.DEFAULT_SOUND;
		
		// Vibration ben�tigt zus�tzliches Recht
		//notification.defaults |= Notification.DEFAULT_VIBRATE;
		
		// Licht
		//notification.defaults |= Notification.DEFAULT_LIGHTS;

		
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		// NotificationManager bekommt Meldung
		notificationManager.notify(SNOOZE_ID, notification);
	}
	/**
	 * @brief Notification wird gelöscht
	 */
	private void cancelNotification(){
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

}
