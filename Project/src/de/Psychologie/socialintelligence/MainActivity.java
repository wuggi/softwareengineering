package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button btnWeiter;
	EditText userCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Weiter Button geklickt?
		btnWeiter = (Button)findViewById(R.id.btnWeiter);
		btnWeiter.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// Usereingabe aus Textfeld holen
				userCode = (EditText)findViewById(R.id.userCode);
				String code = userCode.getText().toString();
				// SQL Handler fuer Datenbankimport
				SQLHandler db = new SQLHandler(MainActivity.this);
				db.addUserCode(code);
				// zur nächsten Activity
				startActivity(new Intent(MainActivity.this,Week.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
