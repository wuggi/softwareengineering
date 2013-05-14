package de.Psychologie.socialintelligence;

import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button btnWeiter;
	private EditText userCode;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(MainActivity.this);
		
		// Zeit und Datum holen
		Calendar cal = Calendar.getInstance();
		
		// TODO: Zeitzone setzen
		// zwecks Sommerzeit +2
		int timeZone = 2;
		
		// Wochentag ist in Android: Sonntag = 1, in unserer Datenbank ist Montag = 0,
		// daher muss (Wochentag+5) mod 7 erfolgen.
        int currentDay = (cal.get(Calendar.DAY_OF_WEEK)+5)%7;

        // Stunde auslesen
        int currentHour = cal.get(Calendar.HOUR_OF_DAY)+timeZone;
        // Minute und Sekunde ist Standardmäßig auf 00 gesetzt.
        // folgender String beschreibt Zeitformat
        String timeInDatabase = String.valueOf(currentHour)+":00:00";
        
        // Wurde die App automatisch gestartet?
        if(db.existDayTime(currentDay, timeInDatabase)){
        	// nächsten Alarm ermitteln
        	// Tag wählen
        	int nextDay;
        	String nextTime;
        	if(currentHour > 19){
        		// nach 19 Uhr ist letzter Zeitslot, der nächste Termin muss am folgenden Tag sein
        		nextDay = (currentDay+1)%7;
        		// erste Zeit vom nächsten Tag holen
        		nextTime = db.getFirstTimeFromDay(nextDay);
        	} else {
        		nextDay = currentDay;
        		nextTime = db.getNextTimeFromDayTime(currentDay, timeInDatabase);
        	}
        	
        	if(nextTime == "00:00:00"){
        		//TODO: Error
        		// setzen Sie Zeiten für den nächsten Tag
        	}
        	
        	// Stunde auslesen
        	int nextHour = Integer.valueOf(nextTime.substring(0, 2));
        	
        	// nächsten Alarm setzen
        	// TODO: Veraltet GregorianCalendar nehmen
        	Date alarm = cal.getTime();
        	alarm.setDate(nextDay);
        	alarm.setHours(nextHour);
        	cal.setTime(alarm);
        	
        	// Aufruf setzen
            Intent intent = new Intent(this, MainActivity.class);
            // 10000 ist einmalige Nummer für den Alarm
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
            
            // Weiterleiten zur Umfrage-Activity
            startActivity(new Intent(MainActivity.this,PopPollActivity.class));
			this.finish();
        }
	
		
		// Falls nicht erster Start(oder zurrueckgesetzt)
		if (db.existUserCode()) {
			startActivity(new Intent(MainActivity.this,
					UserSettingActivity.class));
			this.finish();
		} else {
			setContentView(R.layout.activity_main);

			userCode = (EditText) findViewById(R.id.userCode);
			userCode.setFilters(new InputFilter[] { new InputFilter.AllCaps(),
					new InputFilter.LengthFilter(5) });
			userCode.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable arg0) {
					enableSubmitIfReady();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}
			});

			// Weiter Button geklickt?
			btnWeiter = (Button) findViewById(R.id.btnWeiter);
			btnWeiter.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Usereingabe aus Textfeld holen und alles groï¿½ machen
					String code = userCode.getText().toString().toUpperCase();
					// SQL Handler fuer Datenbankimport
					SQLHandler db = new SQLHandler(MainActivity.this);
					db.addUserCode(code);
					// zur naechsten Activity
					startActivity(new Intent(MainActivity.this, Week.class));
					finish();
				}
			});

		}
	}

    //menü taste deaktiviert ansonsten das Blinken der texteingabe
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            
	            return true;
	    }

	    return super.onKeyDown(keycode, e);
	}

	// Aktiviert weiter button
	protected void enableSubmitIfReady() {

		boolean isReady = userCode.getText().toString().length() == 5;
		if (isReady) {
			btnWeiter.setEnabled(true);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(userCode.getWindowToken(), 0);
		} else {
			btnWeiter.setEnabled(false);
		}
	}

}
