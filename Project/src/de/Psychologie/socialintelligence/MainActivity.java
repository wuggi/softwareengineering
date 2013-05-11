package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button btnWeiter;
	private EditText userCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Falls nicht erster Start(oder zurrueckgesetzt)
		SQLHandler db = new SQLHandler(MainActivity.this);

		if (db.existUserCode()) {
			startActivity(new Intent(MainActivity.this,
					UserSettingActivity.class));
			this.finish();
		} else {
			setContentView(R.layout.activity_main);

			userCode = (EditText) findViewById(R.id.userCode);
			userCode.setFilters(new InputFilter[] { new InputFilter.AllCaps(),
					new InputFilter.LengthFilter(5) });
			userCode.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable arg0) {
					enableSubmitIfReady();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}
			});

			// Weiter Button geklickt?
			btnWeiter = (Button) findViewById(R.id.btnWeiter);
			btnWeiter.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Usereingabe aus Textfeld holen und alles gro� machen
					String code = userCode.getText().toString().toUpperCase();
					// SQL Handler fuer Datenbankimport
					SQLHandler db = new SQLHandler(MainActivity.this);
					db.addUserCode(code);
					// zur naechsten Activity
					startActivity(new Intent(MainActivity.this, Week.class));
					finish();
				}
			});

		}
	}
/*
	// TODO: L�schen da nurnoch zum Start
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;

		}
		return true;
	}

	// Wird aufgerufen falls Result_settings>=0
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			saveUserSettings();
			break;

		}

	}

	protected void saveUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		ring = sharedPrefs.getString("ringtone", "");
	}
	*/

	// Aktiviert weiter button
	protected void enableSubmitIfReady() {

		boolean isReady = userCode.getText().toString().length() == 5;
		if (isReady) {
			btnWeiter.setEnabled(true);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(userCode.getWindowToken(), 0);
		} else {
			btnWeiter.setEnabled(false);
		}
	}

}
