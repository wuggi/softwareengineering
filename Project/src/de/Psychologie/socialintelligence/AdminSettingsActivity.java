package de.Psychologie.socialintelligence;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AdminSettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.adminpreferences);
		Preference button_reset = (Preference) findPreference("button_reset");
		button_reset
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						SQLHandler db = new SQLHandler(
								AdminSettingsActivity.this);
						db.deleteDB();
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
					       					final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AdminSettingsActivity.this);
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
};