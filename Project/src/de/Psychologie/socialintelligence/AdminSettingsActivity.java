package de.Psychologie.socialintelligence;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
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
						
						final AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettingsActivity.this);
						builder.setTitle(getResources().getString(R.string.settings_deleteDB));

				        builder.setMessage(getResources().getString(R.string.settings_deleteDB2))
				               .setCancelable(false)	 			    
				               .setPositiveButton(getResources().getString(R.string.txtYes), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	    reset=true;
				                	    export_to_file();
				                	    
				                	    SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
										db.deleteDB();
										//db.close();
										//Alle Einstellungen werden gelöscht
				       					final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				       					SharedPreferences.Editor editor = settings.edit();
				       					editor.putString("ringtone", "");
				       					editor.putString("Sleeptime", "5 Minuten");				       					
				       					editor.commit();
				       					Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_deleteDB_success), Toast.LENGTH_SHORT).show();	
				                   }
				                })
				               .setNegativeButton(getResources().getString(R.string.txtNo), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {				                	   
				                	   dialog.cancel();
				                   }
				               }); 
				        
						final AlertDialog dialog = builder.create();   
						
				        dialog.show();
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
						
						if (reset){
							//TODO:export ready file
	       					Toast.makeText(getApplicationContext(),"Export nach reset muss noch implementiert werden!", Toast.LENGTH_SHORT).show();
						}
						else{
						
						SQLHandler db = new SQLHandler(AdminSettingsActivity.this);					
						
						//get Preferences
						final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			            String to = settings.getString("emailto", getResources().getString(R.string.std_Email_Adress));
			            String subject = settings.getString("emailsubject", getResources().getString(R.string.std_Email_Subject));
			            
						Intent i = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
						i.setType("plain/text");
						
						ArrayList<Uri> uris = new ArrayList<Uri>();						
						
						for (int j = 0; j < db.getUserCodes().length; j++) {
							FileHandler handler = new FileHandler(db.getUserCodes()[j] + ".CSV");
							File file = handler.createExternalFile(db.getPollCsvContext());
							uris.add(Uri.fromFile(file));
						}

						i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
						i.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
						i.putExtra(Intent.EXTRA_SUBJECT, subject);
						
						startActivity(Intent.createChooser(i, "E-mail"));
						}

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
	
	private void export_to_file() {
		SQLHandler db = new SQLHandler(AdminSettingsActivity.this);
		
		/*
		//Import Testdata

		db.addUserCode("TOMUJ");
		db.setPollEntry("01.01.2012","10:00:00","10:15:20",false,1,0,25);
		db.setPollEntry("01.01.2012","15:00:00","15:33:20",false,1,1,20);
		db.setPollEntry("01.01.2012","17:00:00");
		db.setPollEntry("01.01.2012","23:00:00","23:01:20",false,0,0,0);
		*/
		
		int i;
		for (i=0; i<db.getUserCodes().length; i++)
		{						
		FileHandler file = new FileHandler(db.getUserCodes()[i]+".CSV");
		file.createExternalFile(db.getPollCsvContext());
		}
		if (i>1)
			Toast.makeText(getApplicationContext(),i+" "+getResources().getString(R.string.settings_export_success_multi), Toast.LENGTH_SHORT).show();
		else
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_export_success), Toast.LENGTH_SHORT).show();		
	}
};