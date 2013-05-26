package de.Psychologie.socialintelligence;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
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

@SuppressWarnings("deprecation")
public class Alarm_Activity extends Activity {

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
			mMediaPlayer.setDataSource(prefs.getString("ringtone",
					RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_ALARM).toString()));
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();

			//TODO: korrekte lautstärke
			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			mMediaPlayer.setVolume(audio.getStreamVolume(AudioManager.STREAM_RING),
					audio.getStreamVolume(AudioManager.STREAM_RING));

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
	
	private void setSnooze(){
		//Snoozezeit aus den Settings auslesen, sonst 5 Minuten
		String time= prefs.getString("Sleeptime", "5");
		int snoozetime = Integer.parseInt(time);

		Alarm pollAlarm = new Alarm(this);
		pollAlarm.setSnooze(snoozetime);
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(this);
		db.setSnoozeActiv(true);
		// App beenden
		finish();		
	}
	
	// Forbid closing the view
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        return true;
	    } else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	//TODO:
	//Try to ignore home button
	@Override
	public void onPause(){
		Log.i("Status","pause");
		// onPause() ist started after/before oncreate()
		
		
		super.onPause();
	}

}