package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

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
		Preference button_password_change = (Preference) findPreference("button_password_change");
		button_password_change

		.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {

				// Salt and hash the imput and return it
				return true;
			}
		});
	}
};