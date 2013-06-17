package de.Psychologie.socialintelligence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
* @class BootActivity
<<<<<<< HEAD
* @brief Es wird überprüft, ob eine Umfrage verpasst wurde, während das Handy aus war
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file BootActivity.java
=======
* @brief Diese Activity wird transparent gestartet, sodass der User nichts davon mitbekommt. Sie dient zum setzen des n�chsten Alarms, sollte ein Alarm durch das ausgeschaltete Handy verpasst worden sein, wird sofort die Umfrage gestartet.
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file BootActivity.java
*
>>>>>>> 1b7c9712ead435f6930f7fa00f911ac9c6314323
*/ 
@SuppressLint("SimpleDateFormat")
public class BootActivity extends Activity{
	/**
	 * @brief Instanz von {@link Alarm}
	 */
	private Alarm alarm;
	/**
	 * @brief Instanz von {@link SQLHandler}
	 */
	private SQLHandler db; 
	/**
	 * @brief Instanz von Calendar
	 */
	private Calendar cal;
	
	/**
	 * @brief Es wird überprüft, ob eine Umfrage verpasst wurde: <br>
	 * {@code if((now.after(nextAlarm) && now.after(lastAlarm)) && nextAlarm.after(lastAlarm))}
	 * @param savedInstanceState
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v("test","BootActivity");
		
		// Alarm holen
		alarm = new Alarm(this);
		// Datenbank Verbindung aufbauen
		db = new SQLHandler(this);
		// Kalenderinstanze
		cal = Calendar.getInstance();
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		// Aktuelle Zeit
		Date now = cal.getTime();
		// letzter Alarm
		Date lastAlarm = null;
		// naechster Alarm
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
				startActivity(new Intent(this,PopPollActivity.class));
			} else {
				// Alarm wieder einstellen
				alarm.setNextAlarm();
			}
		}
		
		// Activity beenden
		finish();
	}
}
