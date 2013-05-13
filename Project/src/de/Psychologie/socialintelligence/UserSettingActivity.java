package de.Psychologie.socialintelligence;

import java.security.MessageDigest;

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
import android.widget.EditText;

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

		
		Preference button_about = (Preference) findPreference("button_about");
		button_about
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						AlertDialog ad = new AlertDialog.Builder(
								UserSettingActivity.this).create();
						ad.setTitle(getResources().getString(
								R.string.title_about));
						ad.setMessage(getResources().getString(
								R.string.message_about));
						ad.setButton(getResources().getString(
								R.string.OK),
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
		
		Preference button_admin_settings = (Preference) findPreference("button_admin_settings");
		button_admin_settings
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingActivity.this);
						builder.setTitle(getResources().getString(R.string.title_password_entry));
						final EditText input = new EditText(UserSettingActivity.this); 
						builder.setView(input);
				        builder.setMessage(getResources().getString(R.string.title_password_entry_text))
				               .setCancelable(false)
				               .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				           				// Passwortüberprüfung mit Salt
				                 		//if (MD5(input.toString()+"wro3to!1lEgl!p#iap6o8vl6@lech+a+")==" "){
										//	startActivity(new Intent(UserSettingActivity.this,AdminSettingsActivity.class));
				                  		//}		
										startActivity(new Intent(UserSettingActivity.this,AdminSettingsActivity.class));
				                   }
				               })
				               .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	   dialog.cancel();
				                   }
				               });
				        AlertDialog alert = builder.create();
				        alert.show(); 
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
	
	// MD5 Funktion für Passwörter
	public String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

}
