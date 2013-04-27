package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
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
		btnWeiter = (Button)findViewById(R.id.btnWeiter);
		btnWeiter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userCode = (EditText)findViewById(R.id.userCode);
				String code = userCode.getText().toString();
				SQLHandler db = new SQLHandler(MainActivity.this);
				
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
