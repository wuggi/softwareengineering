package de.Psychologie.socialintelligence;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Week extends Activity {

	Button timeslot1, timeslot2, timeslot3, timeslot4, timeslot5, timeslot6,
			timeslot7, timeslot8;
	Button timeslot9, timeslot10, timeslot11, timeslot12, timeslot13,
			timeslot14, timeslot15;
	Button mon, tue, wed, thur, fri, sat, sun;
	Button saveWeek;
	// Zeitslots verwalten
	Button[] ButtonHandler = new Button[15];
	// Tage verwalten
	Day week[] = new Day[7];
	// aktueller Tag
	Day currentDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week);
		
		////////////////////////////////////////////////////////////////////////
		// Buttons definieren
		////////////////////////////////////////////////////////////////////////
		// fetch Buttons times
		timeslot1 = (Button) findViewById(R.id.timeslot1);
		ButtonHandler[0] = timeslot1;
		timeslot2 = (Button) findViewById(R.id.timeslot2);
		ButtonHandler[1] = timeslot2;
		timeslot3 = (Button) findViewById(R.id.timeslot3);
		ButtonHandler[2] = timeslot3;
		timeslot4 = (Button) findViewById(R.id.timeslot4);
		ButtonHandler[3] = timeslot4;
		timeslot5 = (Button) findViewById(R.id.timeslot5);
		ButtonHandler[4] = timeslot5;
		timeslot6 = (Button) findViewById(R.id.timeslot6);
		ButtonHandler[5] = timeslot6;
		timeslot7 = (Button) findViewById(R.id.timeslot7);
		ButtonHandler[6] = timeslot7;
		timeslot8 = (Button) findViewById(R.id.timeslot8);
		ButtonHandler[7] = timeslot8;
		timeslot9 = (Button) findViewById(R.id.timeslot9);
		ButtonHandler[8] = timeslot9;
		timeslot10 = (Button) findViewById(R.id.timeslot10);
		ButtonHandler[9] = timeslot10;
		timeslot11 = (Button) findViewById(R.id.timeslot11);
		ButtonHandler[10] = timeslot11;
		timeslot12 = (Button) findViewById(R.id.timeslot12);
		ButtonHandler[11] = timeslot12;
		timeslot13 = (Button) findViewById(R.id.timeslot13);
		ButtonHandler[12] = timeslot13;
		timeslot14 = (Button) findViewById(R.id.timeslot14);
		ButtonHandler[13] = timeslot14;
		timeslot15 = (Button) findViewById(R.id.timeslot15);
		ButtonHandler[14] = timeslot15;

		// fetch Buttons week
		mon = (Button) findViewById(R.id.mon);
		tue = (Button) findViewById(R.id.tue);
		wed = (Button) findViewById(R.id.wed);
		thur = (Button) findViewById(R.id.thur);
		fri = (Button) findViewById(R.id.fri);
		sat = (Button) findViewById(R.id.sat);
		sun = (Button) findViewById(R.id.sun);
<<<<<<< HEAD
		
		////////////////////////////////////////////////////////////////////////
		// Methoden OnClick
		////////////////////////////////////////////////////////////////////////
		
		
=======

		// ////
		// Methoden OnClick
		// ////

>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
		// Week
		enableButton(mon, 0);
		enableButton(tue, 0);
		enableButton(wed, 0);
		enableButton(thur, 0);
		enableButton(fri, 0);
		enableButton(sat, 0);
		enableButton(sun, 0);

		// Row1
		enableButton(timeslot1, 1);
		enableButton(timeslot2, 1);
		enableButton(timeslot3, 1);
		enableButton(timeslot4, 1);
		// Row2
		enableButton(timeslot5, 2);
		enableButton(timeslot6, 2);
		enableButton(timeslot7, 2);
		// Row3
		enableButton(timeslot8, 3);
		enableButton(timeslot9, 3);
		enableButton(timeslot10, 3);
		enableButton(timeslot11, 3);
		// Row4
<<<<<<< HEAD
		enableButton(timeslot12,4);
		enableButton(timeslot13,4);
		enableButton(timeslot14,4);
		enableButton(timeslot15,4);
		
		////////////////////////////////////////////////////////////////////////
		// start Activity
		////////////////////////////////////////////////////////////////////////
		
		// Standardeinstellungen
		// Alle deaktivieren
		disableWeek();
		disableAllTimeSlots();
		// Disable all
		enableAllTimes(false);
		
		
		// Zeitdaten aus der Datenbank holen
		if(getWeekFromDatabase()){
			// Zeiten w�hlbar
			enableAllTimes(true);
			// Woche auslesen
			for (int i = 0; i < week.length; i++) {
				if(week[i] != null){
					// Wochentage zur�cksetzen
					disableWeek();
					// aktuellen Wochentag makieren
					setButtonSelect((Button) findViewById(Day.getViewIDfromWeekID(i)));
					// Zeitslots zur�cksetzen
					disableAllTimeSlots();
					for (int j = 0; j < week[i].getTimeSlotsButton().length; j++) {
						// setze Zeitslots f�r diesen Tag
						setButtonSelect(week[i].getTimeSlotsButton()[j]);
					}
				}
			}
		} 
		
		////////////////////////////////////////////////////////////////////////
		// save Week
		////////////////////////////////////////////////////////////////////////
=======
		enableButton(timeslot12, 4);
		enableButton(timeslot13, 4);
		enableButton(timeslot14, 4);
		enableButton(timeslot15, 4);

		// ////
		// start Activity
		// ////

		// Alle deaktivieren
		disableWeek();
		disableAllTimeSlots();

		// Disable all
		enableAllTimes(false);

		// ////
		// save Week
		// ////
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82

		saveWeek = (Button) findViewById(R.id.saveWeek);
		saveWeek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Daten in Datenbank �berf�hren
				if (!writeWeekToDatabase()) {
					// Fehlermeldung ausgeben
<<<<<<< HEAD
					Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtWeekErrorTimeSlots), Toast.LENGTH_LONG).show();
=======
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(
									R.string.txtWeekErrorTimeSlots),
							Toast.LENGTH_SHORT).show();
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
				}
			}
		});
	}
<<<<<<< HEAD
	
	////////////////////////////////////////////////////////////////////////
	// Private Methoden
	////////////////////////////////////////////////////////////////////////
	
	// aktiviert nur den selektierten Button, alle anderen in der Zeile werden disabled
=======

	/*
	 * // week disableWeek(); // Wochentag angeklickt?
	 * if(activeDay.existButton()){ // alle Zeiten gew�hlt?
	 * if(activeTimes.existTimes()){ // Daten abspeichern (Tag und Zeit)
	 * SQLHandler db = new SQLHandler(Week.this);
	 * db.addDayTime(activeDay.getDayNum(), activeTimes.getTime1());
	 * db.addDayTime(activeDay.getDayNum(), activeTimes.getTime2());
	 * db.addDayTime(activeDay.getDayNum(), activeTimes.getTime3());
	 * db.addDayTime(activeDay.getDayNum(), activeTimes.getTime4());
	 * 
	 * // Ausgabe f�r den User showMessage(true); } } // neuen Button aktiv
	 * setzen activeDay.setButton(bnt.getId());
	 */

	@Override
	public void onBackPressed() {
		// Falls aufgerufen von Codeeingabe soll bei zur�ck die Settingsactivity
		// aufgerufen werden
		// sonst normal zur�ck zu settings

		if (this.isTaskRoot()){
			this.startActivity(new Intent(Week.this, UserSettingActivity.class));
			this.finish();
		}		
		super.onBackPressed();

	}

	// aktiviert nur den selektierten Button, alle anderen in der Zeile werden
	// disabled
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
	private void enableButton(final Button bnt, final int row) {
		bnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (row) {
				case 0:
					// korrekten Wochentag markieren
					disableWeek();
					// Auswahl zur�cksetzen
					disableAllTimeSlots();
					// existiert Tag bereits?
					if (existDay(bnt.getId())) {
						// dann setze alle Timeslots
						for (int i = 0; i < 4; i++) {
							if (week[Day.getWeekIDfromViewID(bnt.getId())]
									.getTimeSlotsButton()[i] != null) {
								setButtonSelect(week[Day
										.getWeekIDfromViewID(bnt.getId())]
										.getTimeSlotsButton()[i]);
							}
						}
						// aktuellen Tag merken
						currentDay = week[Day.getWeekIDfromViewID(bnt.getId())];
					} else {
						addDay(bnt.getId());
						// TODO: currentDay??
					}
					enableAllTimes(true);
					break;
				case 1:
					disableRow1();
					currentDay.setTime1(bnt);
					break;
				case 2:
					disableRow2();
					currentDay.setTime2(bnt);
					break;
				case 3:
					disableRow3();
					currentDay.setTime3(bnt);
					break;
				case 4:
					disableRow4();
					currentDay.setTime4(bnt);
					break;

				default:
					break;
				}

				// Button w�hlen (gr�n)
				setButtonSelect(bnt);
			}
		});

	}

<<<<<<< HEAD
	private void enableAllTimes(boolean enable){
=======
	// //////////////////////////////////////
	// Private Methoden
	// //////////////////////////////////////
	private void enableAllTimes(boolean enable) {
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
		for (int i = 0; i < ButtonHandler.length; i++) {
			ButtonHandler[i].setEnabled(enable);
		}
	}

	@SuppressWarnings("deprecation")
	private void disableWeek() {
		mon.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		tue.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		wed.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		thur.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		fri.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		sat.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		sun.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow1() {

		// old way:
		// setBackgroundColor(getResources().getColor(R.color.uncheckedButton));
		// die neue Methode ist zwar veraltet, aber so k�nnen auch alte Ger�te
		// es nutzen

		timeslot1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot3.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot4.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow2() {
		timeslot5.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot6.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot7.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow3() {
		timeslot8.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot9.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot10.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot11.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
	private void disableRow4() {
		timeslot12.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot13.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot14.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
		timeslot15.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_red));
	}

	@SuppressWarnings("deprecation")
<<<<<<< HEAD
	private void setButtonSelect(Button bnt){
		if(bnt != null){
			bnt.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
		}
=======
	private void setButtonSelect(Button bnt) {
		bnt.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_green));
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
	}

	private void disableAllTimeSlots() {
		disableRow1();
		disableRow2();
		disableRow3();
		disableRow4();
	}

	private void addDay(int rID) {
		// aktuellen Tag erstellen
		currentDay = new Day(rID);
		// der Woche hinzuf�gen
		week[currentDay.getWeekID()] = currentDay;
	}

	// pr�ft, ob Tag bereits gesetzt ist
	private boolean existDay(int rID) {
		boolean res = false;
		for (int i = 0; i < week.length; i++) {
			if (week[i] != null) {
				if (week[i].getViewID() == rID) {
					res = true;
				}
			}
		}
		return res;
	}

	// TODO:
	private boolean writeWeekToDatabase() {
		// Datenbankverbindung aufbauen
		SQLHandler db = new SQLHandler(Week.this);
		// jeden Wochentag durchgehen
		for (int i = 0; i < week.length; i++) {
			// wurde Wochentag gesetzt?
			if (week[i] != null) {
				// wurden alle Zeiten f�r den Wochentag gesetzt?
<<<<<<< HEAD
				if(week[i].existAllTimes()){
					// alten Tag l�schen
					db.deleteDay(week[i].getWeekID());
=======
				if (week[i].existAllTimes()) {
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
					// alle Zeitslots w�hlen
					for (int j = 0; j < week[i].getTimeSlots().length; j++) {
						// Wochentag und Zeitslot in Datenbank schreiben
						db.addDayTime(week[i].getWeekID(),
								week[i].getTimeSlots()[j]);
					}
				} else {
					// es wurden nicht f�r jeden Tag 4 Zeitslots gew�hlt
					db.close();
					return false;
				}
			}
		}
		// Datenbankverbindung schlie�en
		db.close();
		// Import ist erfolgreich
		return true;
	}
<<<<<<< HEAD
	
	private boolean getWeekFromDatabase(){
		// Datenbankverbindung aufbauen
		SQLHandler db = new SQLHandler(Week.this);
		Cursor cur = db.getDayTime();
		int check = 0;
		if(cur != null){
			if(cur.moveToFirst()){
				do{
					// Tag erstellen, wenn noch nicht existent
					if(!existDay(Day.getViewIDfromWeekID(cur.getInt(0)))){
						week[cur.getInt(0)] = new Day(Day.getViewIDfromWeekID(cur.getInt(0)));
					}
					// Zeitslots setzen, alle Buttons werden �bergeben
					week[cur.getInt(0)].setTimeSlots(cur.getString(1), ButtonHandler);
					// check
					check++;
				}while(cur.moveToNext());
			}
		}
		cur.close();
		db.close();
		
		return check>3;
		
	}

	////////////////////////////////////////////////////////////////////////
	// Private Klassen
	////////////////////////////////////////////////////////////////////////

	private static class Day{
=======

	private void getWeekFromDatabase() {
		// Datenbankverbindung aufbauen
		SQLHandler db = new SQLHandler(Week.this);
		Cursor cur = db.getDayTime();
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					cur.getInt(0);
					cur.getString(1);
				} while (cur.moveToNext());
			}
		}

	}

	// Methode die Daten aus der Datenbank holt und in Klasse Day �bertr�gt
	// Methode die Daten in die Datenbank �berf�hrt

	// //////////////////////////////////////
	// Private Klassen
	// //////////////////////////////////////

	private static class Day {
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
		private int rID;
		private int weekID;
		private String[] timeSlots = new String[4];
		private Button[] timeSlotsButton = new Button[4];
<<<<<<< HEAD
		private int timeSlotID;
		
		Day(int rID){
=======

		Day(int rID) {
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
			this.rID = rID;
			this.weekID = Day.getWeekIDfromViewID(rID);
			this.timeSlotID = 0;
		}

		public static int getWeekIDfromViewID(int rID) {
			switch (rID) {
			case R.id.mon:
				return 0;
			case R.id.tue:
				return 1;
			case R.id.wed:
				return 2;
			case R.id.thur:
				return 3;
			case R.id.fri:
				return 4;
			case R.id.sat:
				return 5;
			case R.id.sun:
				return 6;
			default:
				// for compiler
				return 99;
			}
		}
<<<<<<< HEAD
		
		public static int getViewIDfromWeekID(int wID){
			switch (wID) {
			case 0:
				return R.id.mon;
			case 1:
				return R.id.tue;
			case 2:
				return R.id.wed;
			case 3:
				return R.id.thur;
			case 4:
				return R.id.fri;
			case 5:
				return R.id.sat;
			case 6:
				return R.id.sun;
			default:
				// for compiler
				return 99;			
			}
		}
		
		
		
		public int getWeekID(){
=======

		public int getWeekID() {
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
			return this.weekID;
		}

		public void setTime1(Button time) {
			this.timeSlots[0] = time.getText().toString();
			this.timeSlotsButton[0] = time;
		}

		public void setTime2(Button time) {
			this.timeSlots[1] = time.getText().toString();
			this.timeSlotsButton[1] = time;
		}

		public void setTime3(Button time) {
			this.timeSlots[2] = time.getText().toString();
			this.timeSlotsButton[2] = time;
		}

		public void setTime4(Button time) {
			this.timeSlots[3] = time.getText().toString();
			this.timeSlotsButton[3] = time;
		}
<<<<<<< HEAD
		
		public boolean existAllTimes(){
			return  timeSlots[0] != null &&
					timeSlots[1] != null &&
					timeSlots[2] != null &&
					timeSlots[3] != null;
			
			/*
			return timeSlots[0].length() > 0 &&
				   timeSlots[1].length() > 0 &&
				   timeSlots[2].length() > 0 &&
				   timeSlots[3].length() > 0;
				   */
=======

		public boolean existAllTimes() {
			return timeSlots[0].length() > 0 && timeSlots[1].length() > 0
					&& timeSlots[2].length() > 0 && timeSlots[3].length() > 0;
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
		}

		public String[] getTimeSlots() {
			return timeSlots;
		}
<<<<<<< HEAD
		
		public void setTimeSlots(String time, Button[] handler){
			// Zeit von HH:mm:ss in HH:mm umwandeln
			time = time.substring(0,5);
			// pr�fen, ob ein Button den Zeittext enth�lt
			for (int i = 0; i < handler.length; i++) {
				if(handler[i].getText().toString().compareTo(time) == 0){
					// Zeit abspeichern
					this.timeSlots[this.timeSlotID] = time;
					// zugeh�rigen Button speichern
					this.timeSlotsButton[this.timeSlotID] = handler[i];
					//this.timeSlotID = (this.timeSlotID+1)%4;
					this.timeSlotID++;
				}
			}
		}
		
		public int getViewID(){
=======

		public int getViewID() {
>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
			return rID;
		}

		public Button[] getTimeSlotsButton() {
			return timeSlotsButton;
		}
	}
<<<<<<< HEAD
=======

	/*
	 * // aktiver Wochentag private class activeDayButton { private boolean
	 * activ = false; private int bntID;
	 * 
	 * public void setButton(int checkBnt){ activ = true; bntID = checkBnt; }
	 * 
	 * public boolean existButton(){ return activ; }
	 * 
	 * 
	 * public int getDayNum(){ switch (bntID) { case R.id.mon: return 1; case
	 * R.id.tue: return 2; case R.id.wed: return 3; case R.id.thur: return 4;
	 * case R.id.fri: return 5; case R.id.sat: return 6; case R.id.sun: return
	 * 7; default: // for compiler return 99; } } }
	 * 
	 * // aktive Timeslots private class activeTimeButtons { private boolean
	 * activRow1 = false; private String timeRow1; private boolean activRow2 =
	 * false; private String timeRow2; private boolean activRow3 = false;
	 * private String timeRow3; private boolean activRow4 = false; private
	 * String timeRow4;
	 * 
	 * public void setTime(int row, String time){ switch (row) { case 1:
	 * activRow1 = true; timeRow1 = time; break; case 2: activRow2 = true;
	 * timeRow2 = time; break; case 3: activRow3 = true; timeRow3 = time; break;
	 * case 4: activRow4 = true; timeRow4 = time; break; default: break; } }
	 * 
	 * public boolean existTimes(){ return (activRow1 && activRow2 && activRow3
	 * && activRow4); }
	 * 
	 * public String getTime1(){ return timeRow1; }
	 * 
	 * public String getTime2(){ return timeRow2; }
	 * 
	 * public String getTime3(){ return timeRow3; }
	 * 
	 * public String getTime4(){ return timeRow4; }
	 * 
	 * }
	 */

>>>>>>> d7cba261b926ead064dba59ae0e820e35e213d82
}
