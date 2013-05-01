package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Week extends Activity {

	Button timeslot1, timeslot2, timeslot3, timeslot4, timeslot5, timeslot6,
			timeslot7, timeslot8;
	Button timeslot9, timeslot10, timeslot11, timeslot12, timeslot13,
			timeslot14, timeslot15;
	Button mon, tue, wed, thur, fri, sat, sun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week);

		// Kein TAB-Men� erforderlich, da immer der selbe Kontext existiert
		// Eine Zeile mit Wochentagen anklicken und Buttons zur�ck setzen, jeweils abspeichern
		
		// fetch Buttons times
		timeslot1 = (Button) findViewById(R.id.timeslot1);
		timeslot2 = (Button) findViewById(R.id.timeslot2);
		timeslot3 = (Button) findViewById(R.id.timeslot3);
		timeslot4 = (Button) findViewById(R.id.timeslot4);
		timeslot5 = (Button) findViewById(R.id.timeslot5);
		timeslot6 = (Button) findViewById(R.id.timeslot6);
		timeslot7 = (Button) findViewById(R.id.timeslot7);
		timeslot8 = (Button) findViewById(R.id.timeslot8);
		timeslot9 = (Button) findViewById(R.id.timeslot9);
		timeslot10 = (Button) findViewById(R.id.timeslot10);
		timeslot11 = (Button) findViewById(R.id.timeslot11);
		timeslot12 = (Button) findViewById(R.id.timeslot12);
		timeslot13 = (Button) findViewById(R.id.timeslot13);
		timeslot14 = (Button) findViewById(R.id.timeslot14);
		timeslot15 = (Button) findViewById(R.id.timeslot15);
		
		// fetch Buttons week
		mon = (Button) findViewById(R.id.mon);
		tue = (Button) findViewById(R.id.tue);
		wed = (Button) findViewById(R.id.wed);
		thur = (Button) findViewById(R.id.thur);
		fri = (Button) findViewById(R.id.fri);
		sat = (Button) findViewById(R.id.sat);
		sun = (Button) findViewById(R.id.sun);
		
		// Row1
		enableButtonRow1(timeslot1,1);
		enableButtonRow1(timeslot2,1);
		enableButtonRow1(timeslot3,1);
		enableButtonRow1(timeslot4,1);
		// Row2
		enableButtonRow1(timeslot5,2);
		enableButtonRow1(timeslot6,2);
		enableButtonRow1(timeslot7,2);
		// Row3
		enableButtonRow1(timeslot8,3);
		enableButtonRow1(timeslot9,3);
		enableButtonRow1(timeslot10,3);
		enableButtonRow1(timeslot11,3);
		// Row4
		enableButtonRow1(timeslot12,4);
		enableButtonRow1(timeslot13,4);
		enableButtonRow1(timeslot14,4);
		enableButtonRow1(timeslot15,4);
	}
	
	private void enableButtonRow1(final Button bnt, final int row) {
		bnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (row) {
				case 1:
					disableRow1();
					break;
				case 2:
					disableRow2();
					break;
				case 3:
					disableRow3();
					break;
				case 4:
					disableRow4();
					break;

				default:
					break;
				}

				bnt.setBackgroundColor(getResources().getColor(
						R.color.checkButton));
			}
		});

	}

	private void disableRow1() {
		timeslot1.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot2.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot3.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot4.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
	}

	private void disableRow2() {
		timeslot5.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot6.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot7.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
	}

	private void disableRow3() {
		timeslot8.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot9.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot10.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot11.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
	}

	private void disableRow4() {
		timeslot12.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot13.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot14.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
		timeslot15.setBackgroundColor(getResources().getColor(
				R.color.uncheckedButton));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.week, menu);
		return true;
	}

}
