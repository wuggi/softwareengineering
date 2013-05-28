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
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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
	private boolean vibrate = true;
	
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
		btn_action.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.alarm_button_green));
		btn_action.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (waitTimer != null) {
					waitTimer.cancel();
					waitTimer = null;
				}
				try {
					if (mMediaPlayer != null) {
						if (mMediaPlayer.isPlaying()) {
							mMediaPlayer.stop();
						}
						mMediaPlayer.release();
						mMediaPlayer = null;
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}		
				wl.release();
				startActivity(new Intent(Alarm_Activity.this,PopPollActivity.class));
         		overridePendingTransition(0, 0);
				finish();
			}
		});

		Button btn_sleep = (Button) findViewById(R.id.btn_snooze_front);
		btn_sleep.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.alarm_button_red));
		btn_sleep.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (waitTimer != null) {
					waitTimer.cancel();
					waitTimer = null;
				}
				try {
					if (mMediaPlayer != null) {
						if (mMediaPlayer.isPlaying()) {
							mMediaPlayer.stop();
						}
						mMediaPlayer.release();
						mMediaPlayer = null;
					}
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

		vibrate = prefs.getBoolean("vibrate", true);
		
		waitTimer = new CountDownTimer(60000, 1000) {

			public void onTick(long millisUntilFinished) {
				if (vibrate){
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				// Vibrate for 500 milliseconds
				v.vibrate(500);
				}
			}

			public void onFinish() {
				// After 60000 milliseconds (1 min) finish current
				try {
					if (mMediaPlayer != null) {
						if (mMediaPlayer.isPlaying()) {
							mMediaPlayer.stop();
						}
						mMediaPlayer.release();
						mMediaPlayer = null;
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				wl.release();
				setSnooze();
			}
		}.start();
	}
	
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
		Toast.makeText(Alarm_Activity.this,getResources().getString(R.string.txtPopPollSnooze), Toast.LENGTH_LONG).show();
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
		
		// Meldung schlie�en
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Meldungstext, wenn gew�hlt
		Context context = getApplicationContext();
		CharSequence contentTitle = "Umfrage";
		CharSequence contentText  = "Bitte beantworten Sie die Umfrage.";
		
		Intent notificationIntent = new Intent(this, PopPollActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, notificationIntent, 1);
	
		// Ton hinzuf�gen
		//notification.defaults |= Notification.DEFAULT_SOUND;
		
		// Vibration ben�tigt zus�tzliches Recht
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
		else return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
	}

}