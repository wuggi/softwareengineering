package de.Psychologie.socialintelligence;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AdminSettingsActivity extends PreferenceActivity {
	
	static boolean reset=false;
	static Uri filedir=null;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
	super.onStart();
	
	// Get the xml/prefx.xml preferences
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());	
	
	// Set emailto summary to chosen one or default	
	String to = prefs.getString("emailto", getResources().getString(R.string.std_Email_Adress));
	Preference topref = (Preference) findPreference("emailto");		
	topref.setSummary(to);		
	
	// Set emailsubject summary to chosen one or default
    String subject = prefs.getString("emailsubject", getResources().getString(R.string.std_Email_Subject));
	Preference subjectpref = (Preference) findPreference("emailsubject");		
	subjectpref.setSummary(subject);	    
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		
		Preference button_reset = (Preference) findPreference("button_reset");
		button_reset
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {						
						reset = true;
						SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
										
						FileHandler file = new FileHandler(db.getUserCode()+".CSV");
						filedir = Uri.fromFile(file.createExternalFile(db.getPollCsvContext()));
						
						Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_export_success), Toast.LENGTH_SHORT).show();		
						
						db.deleteDB();
						// db.close();
						// Alle Einstellungen werden gelöscht
						final SharedPreferences settings = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("ringtone", "");
						editor.putString("Sleeptime", null);
						editor.commit();
						Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_deleteDB_success),Toast.LENGTH_SHORT).show();
					
				        return true;
					}
				});		
		
		Preference button_password_change2 = (Preference) findPreference("password");
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
		
		Preference button_export_email = (Preference) findPreference("button_export_email");
		button_export_email
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {		
						

						//get Preferences
						final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						
			            String to = settings.getString("emailto", getResources().getString(R.string.std_Email_Adress));
			            String subject = settings.getString("emailsubject", getResources().getString(R.string.std_Email_Subject));
						Uri uri = null;

						Intent i = new Intent(Intent.ACTION_SEND);
						//i.setType("message/rfc822");
						//i.setType("text/csv");
						i.setType("application/csv");
						
						if (reset){
							uri=filedir;
						}
						else{
						
						SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
						
						FileHandler handler = new FileHandler(db.getUserCode() + ".CSV");
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
		
		
	}

	@Override
	public void onBackPressed() {
		finish();
		if (reset)
			startActivity(new Intent(AdminSettingsActivity.this,MainActivity.class));
		else {
			overridePendingTransition(0, 0);
			startActivity(new Intent(AdminSettingsActivity.this,UserSettingActivity.class));
		}
	}
};