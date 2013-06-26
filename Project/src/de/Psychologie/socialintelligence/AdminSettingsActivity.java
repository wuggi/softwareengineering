package de.Psychologie.socialintelligence;

import java.io.File;

import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
* @class AdminSettingsActivity
* @brief .csv-Datein Export, Admin-Passwort aendern
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file AdminSettingsActivity.java 
*/ 

@SuppressWarnings("deprecation")
public class AdminSettingsActivity extends PreferenceActivity {
	/**
	 * @brief Ob der Reset-Button geklickt wurde oder nicht
	 */
	private static boolean reset=false;
	/**
	 * @brief Wenn die .csv Dateu erstellt wurde, ist diese die URI 
	 * 
	 */
	private static Uri filedir=null;
	/**
	 * @brief OnStart ist der Alarm überflüssig
	 */
	private Alarm alarm;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/**
		 * @brief Beim ersten Aufruf der App
		 */
		super.onCreate(savedInstanceState);

		//Add View for ActionBar
		setContentView(R.layout.custom_preferences);
		com.markupartist.android.widget.ActionBar bar = (com.markupartist.android.widget.ActionBar)findViewById(R.id.settings_actionbar);
		bar.setTitle(R.string.settings_admin);
		// Actionbar mit Zurueckknopf versehen
        bar.setHomeAction(new IntentAction(this, new Intent(AdminSettingsActivity.this, UserSettingActivity.class), R.drawable.back_button));
		
        ImageButton headerButton = (ImageButton) findViewById(R.id.actionbar_home_btn);
		headerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();				
			}
		});
		
		addPreferencesFromResource(R.xml.adminpreferences);
		
		EditTextPreference emailtotextpref = (EditTextPreference) findPreference("emailto");
		emailtotextpref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference
								.setSummary((String) newValue);
						return true;
					}
				});
		
		EditTextPreference emailsubjecttextpref = (EditTextPreference) findPreference("emailsubject");
		emailsubjecttextpref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference
								.setSummary((String) newValue);
						return true;
					}
				});
		
		final Preference button_export_email = findPreference("button_export_email");
		button_export_email
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {		
						

						//get Preferences
						final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						
			            String to = settings.getString("emailto", getResources().getString(R.string.std_Email_Adress));
			            String subject = settings.getString("emailsubject", getResources().getString(R.string.std_Email_Subject));
			            //Parse subject for Codes
			            SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
			            
			            subject = subject.replace("%c", db.getUserCode());
			            subject = subject.replace("%s", db.getBorderDate(true));
			            subject = subject.replace("%e", db.getBorderDate(false));
			            
			            
						Uri uri;
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("application/csv");
						
						if (reset){
							uri=filedir;
						}
						else{						
							FileHandler handler = new FileHandler(db.getUserCode() + ".csv");
							File file = handler.createExternalFile(db.getPollCsvContext());
							uri= Uri.fromFile(file);
						}
						
						i.putExtra(Intent.EXTRA_STREAM,uri );
						i.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
						i.putExtra(Intent.EXTRA_SUBJECT, subject);
						
						startActivity(Intent.createChooser(i, "E-Mail"));						

						return true;
					}
				});			
		
		final Preference button_reset = findPreference("button_reset");
		button_reset
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {								
							reset = true;						
							SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
						
							db.deleteDB();
							
							// Alle Einstellungen werden geloescht
							final SharedPreferences prefs = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext());
							SharedPreferences.Editor editor = prefs.edit();
							editor.remove("ringtone");
							editor.remove("Sleeptime");
							editor.remove("export");
							editor.commit();
							Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_deleteDB_success),Toast.LENGTH_SHORT).show();
						
							button_reset.setEnabled(false);
				        return true;
					}
				});
		
		final Preference button_export = findPreference("button_export");
		button_export
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {	
							
						byte status = status();
						switch(status){
						case 0: 
							Toast.makeText(getApplicationContext(),getResources().getString(R.string.export_empty), Toast.LENGTH_LONG).show();
							break;
						case 1:
							Toast.makeText(getApplicationContext(),getResources().getString(R.string.export_empty), Toast.LENGTH_LONG).show();
							break;
						case 2:							
							SQLHandler db = new SQLHandler(AdminSettingsActivity.this);

							FileHandler file = new FileHandler(db.getUserCode()	+ ".csv");
							filedir = Uri.fromFile(file.createExternalFile(db.getPollCsvContext()));

							Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_export_success),Toast.LENGTH_LONG).show();
							
							button_reset.setEnabled(true);
							button_export_email.setEnabled(true);
							button_export.setEnabled(false);

							// Save condition in Settings
							SharedPreferences prefs = PreferenceManager
									.getDefaultSharedPreferences(getBaseContext());
							SharedPreferences.Editor editor = prefs.edit();
							editor.putBoolean("export", true);
							editor.commit();
							break;
						case 3:
							AlertDialog ad = new AlertDialog.Builder(AdminSettingsActivity.this).create();
							ad.setTitle(getResources().getString(R.string.Warning));
													
								
							ad.setMessage(getResources().getString(R.string.overrideFile));
							

							ad.setButton(getResources().getString(R.string.override),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
												SQLHandler db = new SQLHandler(AdminSettingsActivity.this);

												FileHandler file = new FileHandler(db.getUserCode()	+ ".csv");
												filedir = Uri.fromFile(file.createExternalFile(db.getPollCsvContext()));

												Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_export_success),Toast.LENGTH_LONG).show();
												
												button_reset.setEnabled(true);
												button_export_email.setEnabled(true);
												button_export.setEnabled(false);

												// Save condition in Settings
												SharedPreferences prefs = PreferenceManager
														.getDefaultSharedPreferences(getBaseContext());
												SharedPreferences.Editor editor = prefs.edit();
												editor.putBoolean("export", true);
												editor.commit();
											dialog.dismiss();
										}
									});
							ad.setButton2(getResources().getString(R.string.keepboth),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											SQLHandler db = new SQLHandler(AdminSettingsActivity.this);

											FileHandler file = new FileHandler(db.getUserCode()	+"("+ db.getBorderDate(false)+").csv");
											filedir = Uri.fromFile(file.createExternalFile(db.getPollCsvContext()));

											Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_export_success),Toast.LENGTH_LONG).show();
											
											button_reset.setEnabled(true);
											button_export_email.setEnabled(true);
											button_export.setEnabled(false);

											// Save condition in Settings
											SharedPreferences prefs = PreferenceManager
													.getDefaultSharedPreferences(getBaseContext());
											SharedPreferences.Editor editor = prefs.edit();
											editor.putBoolean("export", true);
											editor.commit();
											dialog.dismiss();
										}
									});
							ad.show();
							break;
						default:
							Log.v("DBandFileStatus","ERROR - Status="+status);
						}
				        return true;
					}
				});	
		
		Preference button_password_change2 = findPreference("password");
		button_password_change2
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(final Preference arg0) {
						
						LayoutInflater factory = LayoutInflater.from(AdminSettingsActivity.this);

						//text_entry is an Layout XML file containing two text field to display in alert dialog
						final View textEntryView = factory.inflate(R.layout.preferences_two_passwords, null);

						final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText1);
						final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);


						final AlertDialog.Builder alert = new AlertDialog.Builder(AdminSettingsActivity.this);
						
						alert.setTitle(getResources().getString(R.string.settings_password_change)).setView(textEntryView);
						
						alert.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	   if (input1.getText().toString().equals(input2.getText().toString())){
				                		   // Salt and hash the imput and write it manuelly to the preferences
					       					final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					       					SharedPreferences.Editor editor = settings.edit();
					       					editor.putString("password", UserSettingActivity.MD5((input1.getText().toString()) + getResources().getString(R.string.salt)));
					       					editor.commit();
					       					Toast.makeText(getApplicationContext(),getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();	
				                	   }
				                	   else
				                	   {
				                		   //Passwort erneut eingeben
					   						Toast.makeText(getApplicationContext(),getResources().getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
					   						onPreferenceClick(arg0);
				                	   }
				                   }
				               })
				               .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {				                	   
				                	   dialog.cancel();
				                   }
				               });

				        
						final AlertDialog dialog = alert.create();   
						
					      //show keyboard
						    input1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						        @Override
						        public void onFocusChange(View v, boolean hasFocus) {
						            if (hasFocus) {
						                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
						            }
						        }
						    });
						    
				        dialog.show();
						return true;
					}
				});		
		
	}
	
	/**
	 * @brief Email 
	 */
	@Override
	protected void onStart() {
	super.onStart();

	alarm = new Alarm(AdminSettingsActivity.this);
	alarm.stopSnooze();
	
	
	// Get the xml/prefx.xml preferences
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());	
	
	// Set emailto summary to chosen one or default	
	String to = prefs.getString("emailto", getResources().getString(R.string.std_Email_Adress));
	Preference topref = findPreference("emailto");
	((EditTextPreference) topref).setText(to);
	topref.setSummary(to);		
	
	// Set emailsubject summary to chosen one or default
    String subject = prefs.getString("emailsubject", getResources().getString(R.string.std_Email_Subject));
	Preference subjectpref = findPreference("emailsubject");
	((EditTextPreference) subjectpref).setText(subject);
	subjectpref.setSummary(subject);	    
	
	Boolean export = prefs.getBoolean("export", false);
	
	if (export){
		Preference button_export = findPreference("button_export");
		button_export.setEnabled(false);
		final Preference button_reset = findPreference("button_reset");
		button_reset.setEnabled(true);
		final Preference button_export_email = findPreference("button_export_email");
		button_export_email.setEnabled(true);	
	}
	}
	
	/**
	 * @brief Wenn die Activity geschlossen wird und der reset-Button nicht geklickt wurde, 
	 * wird der nächste Alarm gesetzt
	 */
	@Override
	public void onDestroy(){
		if (!reset)
			alarm.setNextAlarm();
		super.onDestroy();
	}
	
	/**
	 * @brief Überprüft, ob eine File existiert und ob etwas in der Datenbank liegt
	 * @return
	 * Codes: 
	 * <ul>
	 * 	<li>0 - DB Empty + no File </li>
	 * 	<li>1 - DB Empty + File exists </li>
	 * 	<li>2 - DB OK + no File </li>
	 * 	<li>3 - DB OK + File exists </li>
	 * </ul>
	 */
	private byte status(){
		byte ret=2;
		SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
		if(db.getPollCsvContext()=="")
			ret&=~(1<<1);
		else
			ret|=(1<<1);
		
		File file = new File(new File(Environment.getExternalStorageDirectory(),"Socialintelligence"), db.getUserCode()+".csv");
		Log.i("file", file.getAbsolutePath());
		if (file.exists())
			ret|= 1;
		else
			ret&=~1;
		Log.i("status", Byte.toString(ret));
			
		
		return ret;
	}

	@Override
	public void onBackPressed() {
		finish();
		if (reset)
			startActivity(new Intent(AdminSettingsActivity.this,MainActivity.class));	
		else {
			startActivity(new Intent(AdminSettingsActivity.this,UserSettingActivity.class));
			overridePendingTransition(0, 0);
		}	
	}
}