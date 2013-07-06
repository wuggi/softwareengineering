package de.Psychologie.socialintelligence;

import android.app.Activity;


/**
 * @brief Von dieser Klasse sollen PopPollActivity und Alarm_Activity erben
 * wird zurzeit noch nicht genutzt. Vorgesehen für Version 2.0
 * @author Steusloff
 *
 */

public class AlarmHandler extends Activity{
	private Alarm pollAlarm;
	
	public AlarmHandler(Alarm pollAlarm) {
		this.pollAlarm = pollAlarm;
	}
	
	// TODO: setSnooze, damit nicht an zwei Stellen �hnlicher Code steht
	
//	protected void setSnooze(int snoozetime){
//		// pr�fen, ob Snoozetime nicht gr��er ist als die Zeit bis zum n�chsten Alarm
//		int checkDifference = pollAlarm.getDifferenceToNextAlarm();
//		if(checkDifference > 0 && snoozetime > checkDifference){
//			// Umfrage speichern
//            String date = FormatHandler.withNull(cal.get(Calendar.DAY_OF_MONTH)) + "." + FormatHandler.withNull((cal.get(Calendar.MONTH)+1)) + "." + cal.get(Calendar.YEAR);
//            String alarmTime = pollAlarm.getCurrentAlarmTime();
//
//            pollAlarm.setNextAlarm();
//            db.setSnoozeActiv(false);
//			action_done=true;
//            //Werte in die DB eintragen
//            db.setPollEntry(date, alarmTime);
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txtPopPollBreak), Toast.LENGTH_LONG).show();
//            // Alle Activitys beenden
//            ActivityRegistry.finishAll();
//		} else {
//			pollAlarm.setSnooze(snoozetime);
//			db.setSnoozeActiv(true);
//			// Meldung
//			Toast.makeText(getApplicationContext(),getResources().getString(R.string.txtPopPollSnooze), Toast.LENGTH_LONG).show();
//			// Notification setzen
//			setNotification();
//		}
//	}
//	
//	private checkDifferenceToNextAlarm(int checkDif){
//		
//	}
}
