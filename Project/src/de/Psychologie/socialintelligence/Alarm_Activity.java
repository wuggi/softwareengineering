package de.Psychologie.socialintelligence;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Alarm_Activity extends Activity {

	private static final int HITHERE_ID = 1;
	private CountDownTimer waitTimer;
	private PowerManager.WakeLock wl;
	private MediaPlayer mMediaPlayer;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);   
		setContentView(R.layout.activity_alarm);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		// Stay unlocked if not pw-secured
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		
		TextView text = (TextView) findViewById(R.id.textview_alarm);
		Calendar c = Calendar.getInstance();		
		text.setText(FormatHandler.withNull(c.get(Calendar.HOUR_OF_DAY))+":"+FormatHandler.withNull(c.get(Calendar.MINUTE))+" Uhr\nExperiment Umfrage");
		
		
		Button btn_action = (Button) findViewById(R.id.btn_action_front);
		btn_action.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (waitTimer != null) {
					waitTimer.cancel();
					waitTimer = null;
				}
				try{
					mMediaPlayer.stop();
					mMediaPlayer.release();				
					} catch (IllegalStateException e){
						e.printStackTrace();
						}		
				wl.release();
				startActivity(new Intent(Alarm_Activity.this,PopPollActivity.class));
				finish();
			}
		});

		Button btn_sleep = (Button) findViewById(R.id.btn_snooze_front);
		btn_sleep.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (waitTimer != null) {
					waitTimer.cancel();
					waitTimer = null;
				}
				try{
					mMediaPlayer.stop();
					mMediaPlayer.release();				
					} catch (IllegalStateException e) {
						e.printStackTrace();
						}
				wl.release();
				setSnooze();
			}
		});
		// Play selected Ringtone
		prefs = PreferenceManager.getDefaultSharedPreferences(Alarm_Activity.this);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Lights are ON");
		wl.acquire();

		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.reset();
			String path = prefs.getString("ringtone",RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_ALARM).toString());
			mMediaPlayer.setDataSource(path);

		    Log.d("Alarm_Activity", "playSong :: " + path);
		    
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		waitTimer = new CountDownTimer(60000, 70000) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {
				// After 60000 milliseconds (1 min) finish current
				mMediaPlayer.stop();
				mMediaPlayer.release();
				wl.release();
				setSnooze();
			}
		}.start();
	}
	
	// TODO: setSnooze und set Notification werden in PopPollActivity auch genutzt (fast 1:1)
	private void setSnooze(){
		//Snoozezeit aus den Settings auslesen, sonst 5 Minuten
		String time= prefs.getString("Sleeptime", "5");
		int snoozetime = Integer.parseInt(time);

		Alarm pollAlarm = new Alarm(this);
		pollAlarm.setSnooze(snoozetime);
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(this);
		db.setSnoozeActiv(true);
		// Meldung
		Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtPopPollSnooze), Toast.LENGTH_LONG).show();
		// Notification setzen
		setNotification();
		// App beenden
		finish();	
	}
	
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
		
		Intent notificationIntent = new Intent(this, PopPollActivity.class);
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
	
	// Forbid closing the view
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME)
			return true;
		else if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}