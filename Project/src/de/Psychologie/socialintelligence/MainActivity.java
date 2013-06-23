package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * 
 * @mainpage SocialIntelligence App for Android
 * <table border="0" align="center">
 * <tr>
 * 		<th><img src="pics/ic_launcher.png" alt="pics/ic_launcher.png" width="200" height="200"></th>
 *   	<th><img src="pics/ic_stat_notify.png" alt="pics/ic_stat_notify.png" width="200" height="200"></th>
 * </tr>
 * <tr>
 * 		<td align="center">Launcher/Logo</td>
 *  	<td align="center">Notification</td>
 * </tr>
 * </table>

 * @section sec1 Informationen
 * Diese App für Android wurde im Rahmen der Veranstaltung "Softwareengineering", Fakultät für Informatik
 * an der Otto-von-Guericke Universität entwickelt.<br>
 * Der Zweck dieser App ist es, die Häufigkeit von Sozialen Kontakten zu erfassen.
 * @subsection subsec1 Anforderungen
 * Systemanforderungen: Android 2.1 oder höher<br>
 * @subsection subsec2 Entwickler
 * <b>Kontaktmöglichkeit:</b><br>
 * <table border="0" width="450">
 * 	<tr>
 * 		<td>Christian Steusloff</td>  
 *  	<td><a href="christian.steusloff@st.ovgu.de">christian.steusloff@st.ovgu.de</a></td>
 *	</tr>
 * 	<tr>
 * 		<td>Jens Wiemann</td>
 * 		<td><a href="jens.wiemann@st.ovgu.de">jens.wiemann@st.ovgu.de</a></td>
 * 	</tr>
 * 	<tr>
 * 		<td>Franz Kuntke</td>
 * 		<td><a href="franz.kuntke@st.ovgu.de">franz.kuntke@st.ovgu.de</a></td>
 * 	</tr>
 *  <tr>
 * 		<td>Patrick Wuggazer</td>
 * 		<td><a href="patrick.wuggazer@st.ovgu.de">patrick.wuggazer@st.ovgu.de</a></td>
 * 	</tr>
 * </table>
 * 
 * @subsection subsec3 Screenshots
 * <table border="1">
  <tr>
    <th>MainActivity</th>
    <th>Week</th>
    <th>UserSettingActivity</th>
  </tr>
  <tr>
    <td><img src="pics/01_Screenshot_Login.jpg" alt="pics/01_Screenshot_Login.jpg" width="300" height="500"></td>
    <td><img src="pics/02_Screenshot_Week.jpg" alt="pics/02_Screenshot_Week.jpg" width="300" height="500"></td>
    <td><img src="pics/03_Screenshot_Main.jpg" alt="pics/02_Screenshot_Main.jpg" width="300" height="500"></td>
  </tr>
  <tr>
  	<th>AdminSettingsActivity</th>
    <th>Alarm_Activity</th>
    <th>PopPollActivity</th>
  </tr>
  <tr>
   	<td><img src="pics/04_Screenshot_AdminMenu.jpg" alt="pics/02_Screenshot_AdminMenu.jpg" width="300" height="500"></td>
  	<td><img src="pics/05_Screenshot_Lockscreen.jpg" alt="pics/02_Screenshot_Lockscreen.jpg" width="300" height="500"></td>
  	<td><img src="pics/06_Screenshot_Alarm.jpg" alt="pics/02_Screenshot_Alarm.jpg" width="300" height="500"></td>
 
  </tr>
</table>

* @class MainActivity
* @brief Eingabe des Usercodes. Diese Activity wird einmalig ausgeführt. 
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file MainActivity.java 
*/ 
public class MainActivity extends Activity {
	
	/**
	 * @brief Weiter-Button
	 */
	private Button btnWeiter;
	/**
	 * @brief eingegebener Usercode
	 */
	private EditText userCode;

	/**
	 * @brief Weiterleitungen
	 * <ul>
  			<li>Wenn Alarm aktiv --> Weiterleitung zu {@link PopPollActivity}</li>
  			<li>Wenn bereits ein User existiert --> Weiterleitung zu {@link UserSettingActivity}</li>
  		    <li>sonst, Eingabe des Usercodes</li>
		</ul>
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Datenbank Verbindung aufbauen
		SQLHandler db = new SQLHandler(MainActivity.this);

		// Alarm aktiv, Weiterleitung zur Umfrage	
		if(db.getSnoozeActiv()){
			startActivity(new Intent(MainActivity.this,PopPollActivity.class));
			finish();
			
		// User existiert, Weiterleitung zu Einstellungsuebersicht
		
		} else if (db.existUserCode()) {
			startActivity(new Intent(MainActivity.this,UserSettingActivity.class));
			finish();

		// erster App-Start
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

			btnWeiter.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
			
			btnWeiter.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Usereingabe aus Textfeld holen und alles gross machen
					String code = userCode.getText().toString().toUpperCase();
					// SQL Handler fuer Datenbankimport
					SQLHandler db = new SQLHandler(MainActivity.this);
					db.addUserCode(code);
					// zur naechsten Activity
					MainActivity.this.finish();
					startActivity(new Intent(MainActivity.this, Week.class));
				}
			});

		}
	}

    //menue taste deaktiviert ansonsten das Blinken der texteingabe
	/**
	 * @brief //TODO
	 * @param int keycode
	 */
	@Override
	public boolean onKeyDown(int keycode,KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            
	            return true;
	    }

	    return super.onKeyDown(keycode, e);
	}

	// Aktiviert weiter button
	/**
	 * @brief Aktiviert den btnWeiter-Button, wenn die Länger der Eingabe == 5
	 */
    void enableSubmitIfReady() {

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
