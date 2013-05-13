package de.Psychologie.socialintelligence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;

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
		Preference button_about = (Preference) findPreference("button_about");
		button_about
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						AlertDialog ad = new AlertDialog.Builder(
								UserSettingActivity.this).create();
						ad.setTitle(getResources().getString(
								R.string.title_about));
						ad.setCancelable(false); // This blocks the 'BACK'
													// button
						ad.setMessage(getResources().getString(
								R.string.message_about));
						ad.setButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						ad.show();
						return true;
					}
				});

		RingtonePreference ringtonepref = (RingtonePreference) findPreference("ringtone");
		ringtonepref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						Uri ringtoneUri = Uri.parse((String) newValue);
						Ringtone ringtone = RingtoneManager.getRingtone(
								UserSettingActivity.this, ringtoneUri);
						String name = ringtone.getTitle(UserSettingActivity.this);

						preference.setSummary( name);

						return true;
					}
				});

		ListPreference sleeptimechooser = (ListPreference) findPreference("Sleeptime");
		sleeptimechooser
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference
								.setSummary(((String) newValue) + "\tMinuten");
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
