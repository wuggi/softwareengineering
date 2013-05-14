package de.Psychologie.socialintelligence;

import android.app.AlertDialog;
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
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(final Preference arg0) {
						
						AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettingsActivity.this);
						builder.setTitle(getResources().getString(R.string.title_password_entry));
						final EditText input = new EditText(AdminSettingsActivity.this);
						//final EditText input2 = new EditText(AdminSettingsActivity.this);
						// input2.setInputType(InputType.TYPE_CLASS_TEXT |
						// InputType.TYPE_TEXT_VARIATION_PASSWORD);
						/*
						  Context context = mapView.getContext(); LinearLayout
						  layout = new LinearLayout(context);
						  layout.setOrientation(LinearLayout.VERTICAL);
						  
						  final EditText titleBox = new EditText(context);
						  titleBox.setHint("Title"); layout.addView(titleBox);
						  
						  final EditText descriptionBox = new
						  EditText(context);
						  descriptionBox.setHint("Description");
						  layout.addView(descriptionBox);
						  
						  dialog.setView(layout);
						 */
						
						input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						builder.setView(input);
						
				        builder.setCancelable(false)
				               .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	// Salt and hash the imput and write it manuelly to the preferences
				       				final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AdminSettingsActivity.this);
				       				SharedPreferences.Editor editor = settings.edit();
				       				editor.putString("password", UserSettingActivity.MD5((input.getText().toString()) + getResources().getString(R.string.salt)));
				       				editor.commit();				                	
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
};