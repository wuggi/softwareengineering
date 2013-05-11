package de.Psychologie.socialintelligence;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class UserSettingActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		Preference button_week = (Preference) findPreference("button_week");
		button_week
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						startActivity(new Intent(UserSettingActivity.this,
								Week.class));
						return true;
					}
				});

		Preference button_reset = (Preference) findPreference("button_reset");
		button_reset
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						SQLHandler db = new SQLHandler(UserSettingActivity.this);
						db.deleteDB();
						return true;
					}
				});

	}
	@Override
	public void onBackPressed() {
		// Wenn es nicht der knoten ist, soll es geschlossen werden
		if (!this.isTaskRoot())
			this.finish();
		else
			super.onBackPressed();
	}
}