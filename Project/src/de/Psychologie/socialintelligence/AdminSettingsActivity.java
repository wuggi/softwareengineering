package de.Psychologie.socialintelligence;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.widget.EditText;
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
		Preference button_password_change = (Preference) findPreference("password");
		
		button_password_change
		.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {			
				// Salt and hash the imput and write it manuelly to the preferences
				final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AdminSettingsActivity.this);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("password", UserSettingActivity.MD5(((String) newValue) + getResources().getString(R.string.salt)));
				editor.commit();
				//Dont change it to newValue
				return false;
			}
		});
	}
};