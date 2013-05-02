package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Week extends Activity {

	Button timeslot1, timeslot2, timeslot3, timeslot4, timeslot5, timeslot6,
			timeslot7, timeslot8;
	Button timeslot9, timeslot10, timeslot11, timeslot12, timeslot13,
			timeslot14, timeslot15;
	Button mon, tue, wed, thur, fri, sat, sun;
	
	// aktiven Wochentag speichern
	activeDayButton activeDay = new activeDayButton();
	// aktive Zeiten speichern
	activeTimeButtons activeTimes = new activeTimeButtons();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week);

		// Buttons definieren
		
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
		
		// Methoden bei Klick hinterlegen
		
		// Week
		enableButton(mon, 0);
		enableButton(tue, 0);
		enableButton(wed, 0);
		enableButton(thur, 0);
		enableButton(fri, 0);
		enableButton(sat, 0);	
		enableButton(sun, 0);
		
		// Row1
		enableButton(timeslot1,1);
		enableButton(timeslot2,1);
		enableButton(timeslot3,1);
		enableButton(timeslot4,1);
		// Row2
		enableButton(timeslot5,2);
		enableButton(timeslot6,2);
		enableButton(timeslot7,2);
		// Row3
		enableButton(timeslot8,3);
		enableButton(timeslot9,3);
		enableButton(timeslot10,3);
		enableButton(timeslot11,3);
		// Row4
		enableButton(timeslot12,4);
		enableButton(timeslot13,4);
		enableButton(timeslot14,4);
		enableButton(timeslot15,4);
		
		//////
		// start Activity
		//////
		
		// Alle deaktivieren
		disableWeek();
		disableRow1();
		disableRow2();
		disableRow3();
		disableRow4();
		
		
		
	}
	
	

	
	
	// aktiviert nur den selektierten Button, alle anderen in der Zeile werden disabled
	private void enableButton(final Button bnt, final int row) {
		bnt.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				switch (row) {
				case 0:
					// week
					disableWeek();
					// Wochentag angeklickt?
					if(activeDay.existButton()){
						// alle Zeiten gewählt?	
						if(activeTimes.existTimes()){
							// Daten abspeichern (Tag und Zeit)
							SQLHandler db = new SQLHandler(Week.this);
							db.addDayTime(activeDay.getDayNum(), activeTimes.getTime1());
							db.addDayTime(activeDay.getDayNum(), activeTimes.getTime2());
							db.addDayTime(activeDay.getDayNum(), activeTimes.getTime3());
							db.addDayTime(activeDay.getDayNum(), activeTimes.getTime4());
							
							// Ausgabe für den User
							showMessage(true);
						}
					}
					// neuen Button aktiv setzen
					activeDay.setButton(bnt.getId());
					break;
				case 1:
					disableRow1();
					activeTimes.setTime(1, bnt.getText().toString());
					break;
				case 2:
					disableRow2();
					activeTimes.setTime(2, bnt.getText().toString());
					break;
				case 3:
					disableRow3();
					activeTimes.setTime(3, bnt.getText().toString());
					break;
				case 4:
					disableRow4();
					activeTimes.setTime(4, bnt.getText().toString());
					break;

				default:
					break;
				}

				bnt.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.week, menu);
		return true;
	}
	
	////////////////////////////////////////
	// Private Methoden
	////////////////////////////////////////
	
	@SuppressWarnings("deprecation")
	private void disableWeek(){
		mon.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		tue.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		wed.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		thur.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		sat.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		sun.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
	}
	
	@SuppressWarnings("deprecation")
	private void disableRow1() {
		
		// old way:
		// setBackgroundColor(getResources().getColor(R.color.uncheckedButton));
		// die neue Methode ist zwar veraltet, aber so können auch alte Geräte es nutzen
		
		timeslot1.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot2.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot3.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot4.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow2() {
		timeslot5.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot6.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot7.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow3() {
		timeslot8.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot9.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot10.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot11.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow4() {
		timeslot12.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot13.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot14.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
		timeslot15.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red));
	}
	
	private void showMessage(boolean show){
		TextView message = (TextView) findViewById(R.id.savedTime);
		if(show){
			message.setText(getResources().getString(R.string.timeSaved));
		} else {
			message.setText(getResources().getString(R.string.notSaved));
		}
		
		
	}

	////////////////////////////////////////
	// Private Klassen
	////////////////////////////////////////
	
	// aktiver Wochentag
	private class activeDayButton {
		private boolean activ = false;
		private int bntID;
		
		public void setButton(int checkBnt){
			activ = true;
			bntID = checkBnt;
		}
		
		public boolean existButton(){
			return activ;
		}
		
		
		public int getDayNum(){
			switch (bntID) {
			case R.id.mon:
				return 1;
			case R.id.tue:
				return 2;
			case R.id.wed:
				return 3;
			case R.id.thur:
				return 4;
			case R.id.fri:
				return 5;
			case R.id.sat:
				return 6;
			case R.id.sun:
				return 7;
			default:
				// for compiler
				return 99;
			}
		}
	}
	
	// aktive Timeslots
	private class activeTimeButtons {
		private boolean activRow1 = false;
		private String timeRow1;
		private boolean activRow2 = false;
		private String timeRow2;
		private boolean activRow3 = false;
		private String timeRow3;
		private boolean activRow4 = false;
		private String timeRow4;
		
		public void setTime(int row, String time){
			switch (row) {
			case 1:
				activRow1 = true;
				timeRow1 = time;
				break;
			case 2:
				activRow2 = true;
				timeRow2 = time;
				break;
			case 3:
				activRow3 = true;
				timeRow3 = time;
				break;
			case 4:
				activRow4 = true;
				timeRow4 = time;
				break;
			default:
				break;
			}
		}
		
		public boolean existTimes(){
			return (activRow1 && activRow2 && activRow3 && activRow4);
		}
		
		public String getTime1(){
			return timeRow1;
		}
		
		public String getTime2(){
			return timeRow2;
		}
		
		public String getTime3(){
			return timeRow3;
		}
		
		public String getTime4(){
			return timeRow4;
		}
		
	}
	


}
