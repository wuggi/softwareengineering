package de.Psychologie.socialintelligence;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

//TODO: nur zu Testzwecken
@TargetApi(Build.VERSION_CODES.FROYO)
public class SQLHandler extends SQLiteOpenHelper {
 
	private static final String DATABASE_NAME = "socialintelligence.sql";
	private static final int DATABASE_VERSION = 3;
	
	/////////////////////////////////////////////////////////////
	//// CREATE TABLES
	/////////////////////////////////////////////////////////////
	
	private static final String tabCreateUser = "CREATE TABLE IF NOT EXISTS user ( " +
												"ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
												"code VARCHAR(5) NOT NULL)";
	
	private static final String tabCreatePoll = "CREATE TABLE IF NOT EXISTS poll ( " +
												"ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
												"date DATE NULL , " +
												"alarm TIME NULL , " +
												"answer TIME NULL , " +
												"break CHAR NULL , " +
												"contact INTEGER NULL , " +
												"hour INTEGER NULL , " +
												"minute INTEGER NULL)";
	
	private static final String tabCreateTime = "CREATE TABLE IF NOT EXISTS time ( " +
												"ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
												"day INTEGER NULL, " +
												"time TEXT NULL)";

	private static final String tabCreateStatus = "CREATE TABLE IF NOT EXISTS status ( " +
												  "ID INTEGER PRIMARY KEY NOT NULL, " +
												  "snoozeActiv INTEGER NULL, " +	
												  "snoozeTime INTEGER NULL, " + 
												  "lastAlarm Text NULL)";
			
	/////////////////////////////////////////////////////////////
	//// FIRST PROCESS
	/////////////////////////////////////////////////////////////
	
	public SQLHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if(BuildConfig.DEBUG){
			Log.v("SQL",tabCreateUser);
			Log.v("SQL",tabCreatePoll);
			Log.v("SQL",tabCreateTime);
			Log.v("SQL",tabCreateStatus);
		}
		db.execSQL(tabCreateUser);
		db.execSQL(tabCreatePoll);
		db.execSQL(tabCreateTime);
		db.execSQL(tabCreateStatus);
		// Default: ID, snooze deaktiv, snooze 10minutes, Starttag
		ContentValues values = new ContentValues();
		values.put("ID", 1);
		values.put("snoozeActiv", 0);		 // nicht aktiv
		values.put("snoozeTime", 5);		 // 5 Minuten
		values.put("lastAlarm", "00:00:00"); // Default keine Zeit
		db.insert("status", null, values);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS poll");
		db.execSQL("DROP TABLE IF EXISTS time");
		db.execSQL("DROP TABLE IF EXISTS status");
		
		onCreate(db);
	}
	
	// TODO: nur für Testzwecke, später löschen!
	public void deleteDB(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS poll");
		db.execSQL("DROP TABLE IF EXISTS time");
		db.execSQL("DROP TABLE IF EXISTS status");
		onCreate(db);
	}
	
	/////////////////////////////////////////////////////////////
	//// Query
	/////////////////////////////////////////////////////////////
	
	// Wartezeit auslesen 
	// MAIN Activitiy
	
	public boolean getSnoozeActiv(){
		SQLiteDatabase db= this.getReadableDatabase();
		boolean snoozeActiv = false;
		Cursor c = db.rawQuery("SELECT snoozeActiv FROM status WHERE ID=1",null);
		if(c != null){
			c.moveToFirst();
			// prÃ¼fen, ob Snooze gesetzt
		    if(c.getInt(0) == 1){
		    	snoozeActiv = true;
		    }
		}
		return snoozeActiv;
	}
	
	public void setSnoozeActiv(boolean activ){
		SQLiteDatabase db= this.getWritableDatabase();
		// bool umwandeln
		int value = activ?1:0;
		// values als cv
		ContentValues cv = new ContentValues();
		cv.put("snoozeActiv", value);
		// Datenbankupdate
		db.update("status", cv, "ID = 1", null);
		db.close();
	}
	
	// letzten Alarm setzen
	public void setLastAlarm(String lastAlarmTime){
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("lastAlarm", lastAlarmTime);
		db.update("status", cv, "ID = 1", null);
		db.close();
	}
	
	// letzen Alarm auslesen
	public String getLastAlarm() {
		SQLiteDatabase db= this.getReadableDatabase();
		String res = "00:00:00";
		Cursor c = db.rawQuery("SELECT lastAlarm FROM status WHERE ID=1",null);
		if(c != null){
			c.moveToFirst();
			res = c.getString(0);
		}
		return res;
	}
	
	
	// Wartezeit holen & setzen
	public int getSnoozeTime(){
		SQLiteDatabase db= this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT snoozeTime FROM status WHERE ID=1",null);
		if(c != null){
			c.moveToFirst();
			// prÃ¼fen, ob Snooze gesetzt
		    return c.getInt(0);
		} else {
			return -1;
		}
	}
	
	public void setSnoozeTime(int value){
		SQLiteDatabase db= this.getWritableDatabase();
		// nur positive Zeiten
		value = value>0?value:15;
		// values als cv
		ContentValues cv = new ContentValues();
		cv.put("snoozeTime", value);
		// Datenbankupdate
		db.update("status", cv, "ID = 1", null);
		db.close();
	}
	
	// add User Code
	public void addUserCode(String code){
		SQLiteDatabase db= this.getWritableDatabase();
		
		Log.v("code",code);
		//content Typ, to import String code as table value
		ContentValues cv = new ContentValues();
		cv.put("code", code);
		
		db.insert("user", "code", cv);
		db.close();
	}
	
	public boolean existUserCode(){
		SQLiteDatabase db= this.getReadableDatabase();
		boolean exist = false;
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM user",null);
		if(c != null){
			c.moveToFirst();
			if(c.getInt(0) > 0){
				exist = true;
			}
		}
		db.close();
		return exist;
	}
	
	// add Week times
	public void addDayTime(int day,String time){
		if(day<7 && day>=0){
			SQLiteDatabase db= this.getWritableDatabase();
			
			ContentValues cv = new ContentValues();
			cv.put("day",day);
			//time in HH:MM:SS
			time += ":00";
			cv.put("time", time);			
			// import
			db.insert("time", null, cv);
			
			db.close();
		}
	}
	
	public void deleteDay(int day){
		if(day<7 && day>=0){
			SQLiteDatabase db= this.getWritableDatabase();		
			// Tag löschen
			db.delete("time", "day="+day, null);		
			db.close();
		}
	}
	
	public Cursor getDayTime(){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cur=db.rawQuery("SELECT day,time from time",null);
		return cur;
	}
	
	public boolean existDayTime(int day,String time){
		SQLiteDatabase db= this.getReadableDatabase();
		boolean exist = false;
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM time WHERE day="+day+" AND time ='"+time+"'",null);
		if(c != null){
			c.moveToFirst();
			Log.v("test",String.valueOf(c.getInt(0)));
			if(c.getInt(0) > 0){
				exist = true;
			}
		}
		db.close();
		Log.v("test",String.valueOf(exist));
		return exist;
	}
	
	
	
	public String getNextTimeFromDayTime(int day,String time){
		String res = "00:00:00";
		if(existDayTime(day,time)){
			// SELECT time FROM time WHERE day = 0 AND ID = 1+(SELECT ID FROM time WHERE day= 0 AND time ='16:00:00')
			SQLiteDatabase db= this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT time FROM time " +
								   "WHERE day = "+day+" " +
								   "AND ID = 1+(SELECT ID " +
								    			 "FROM time " +
								    			 "WHERE day="+day+" " +
								    			 "AND time ='"+time+"')",null);
			if(c != null){
				c.moveToFirst();
				res = c.getString(0);
			}
			db.close();
			return res;
		} else {
			// TODO: else Pfad klappt immer, if nicht notwendig!
			// SELECT time FROM time WHERE day = 1 AND time(time) > time('18:00:00')
			SQLiteDatabase db= this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT time FROM time " +
								   "WHERE day = "+day+" " +
								   "AND time(time) > time('"+time+"')",null);
			if(c != null){
				c.moveToFirst();
				res = c.getString(0);
			}
			db.close();
			return res;
		}
	}
	
	public String getFirstTimeFromDay(int day){
		String res = "00:00:00";
		SQLiteDatabase db= this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT time FROM user WHERE day='"+day+"' ORDER BY ID",null);
		if(c != null){
			c.moveToFirst();
			res = c.getString(0);
		}
		db.close();
		return res;
	}
	
	
	// TODO: Es wird eine Methode benötigt, welche gesetzte Tage löscht, wenn dieser in der Vergangenheit liegt.
	

	/*
	public Cursor getUserByID(int id){
		 SQLiteDatabase db=this.getReadableDatabase();
		 return db.rawQuery("SELECT ID as _id, name FROM "+tabUser+
			" WHERE ID="+id, null);
	}
	*/
	
	
}
	
/*

	-- -----------------------------------------------------
	-- Table `mydb`.`status`
	-- -----------------------------------------------------
	CREATE  TABLE IF NOT EXISTS `mydb`.`status` (
	  `ID` INT NOT NULL AUTO_INCREMENT ,
	  `lastAlarm` TIME NULL ,
	  `snooze` CHAR NULL ,
	  `startDay` CHAR NULL ,
	  PRIMARY KEY (`ID`) )
	ENGINE = InnoDB;

*/
	


