package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Week extends Activity {
	
	Button timeslot1,timeslot2,timeslot3,timeslot4,timeslot5,timeslot6,timeslot7,timeslot8;
	Button timeslot9,timeslot10,timeslot11,timeslot12,timeslot13,timeslot14,timeslot15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week);
		
		// fetch Buttons 
		timeslot1=(Button)findViewById(R.id.timeslot1);
		timeslot2=(Button)findViewById(R.id.timeslot2);
		timeslot3=(Button)findViewById(R.id.timeslot3);
		timeslot4=(Button)findViewById(R.id.timeslot4);
		timeslot5=(Button)findViewById(R.id.timeslot5);
		timeslot6=(Button)findViewById(R.id.timeslot6);
		timeslot7=(Button)findViewById(R.id.timeslot7);
		timeslot8=(Button)findViewById(R.id.timeslot8);
		timeslot9=(Button)findViewById(R.id.timeslot9);
		timeslot10=(Button)findViewById(R.id.timeslot10);
		timeslot11=(Button)findViewById(R.id.timeslot11);
		timeslot12=(Button)findViewById(R.id.timeslot12);
		timeslot13=(Button)findViewById(R.id.timeslot13);
		timeslot14=(Button)findViewById(R.id.timeslot14);
		timeslot15=(Button)findViewById(R.id.timeslot15);
		
//		timeslot1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				this.
//				// TODO Auto-generated method stub
//				
//			}
//		})
//		
//		
//		OnClickListener selectButton = new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//
//		};
	}
	
	public void buttonHandler(final View view){
		switch(view.getId()){
		case R.id.timeslot1:
			timeslot1.setBackgroundColor(getResources().getColor(R.color.checkButton));
			timeslot2.setBackgroundColor(getResources().getColor(R.color.uncheckedButton));
			timeslot3.setBackgroundColor(getResources().getColor(R.color.uncheckedButton));
			timeslot4.setBackgroundColor(getResources().getColor(R.color.uncheckedButton));
			break;
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.week, menu);
		return true;
	}

}
