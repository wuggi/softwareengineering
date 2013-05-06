package de.Psychologie.socialintelligence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLHandler extends SQLiteOpenHelper {
 
	private static final String DATABASE_NAME = "socialintelligence.db";
	private static final int DATABASE_VERSION = 2;
	
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
												  "startDay INTEGER NULL)";
												  
			
			
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
		db.execSQL("INPUT INTO status VALUES (1,0,10,NULL)");
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS poll");
		db.execSQL("DROP TABLE IF EXISTS time");
		db.execSQL("DROP TABLE IF EXISTS status");
	}
	
	/////////////////////////////////////////////////////////////
	//// Query
	/////////////////////////////////////////////////////////////
	
	// MAIN Activitiy
	
	boolean getSnooze(){
<<<<<<< HEAD
		SQLiteDatabase db= this.getReadableDatabase();
		boolean snoozeActiv = false;
		Cursor c = db.rawQuery("SELECT snoozeActiv FROM status WHERE ID=1",null);
		while(c.moveToNext()){
			// prüfen, ob Snooze gesetzt
		    if(c.getInt(0)== 1){
		    	snoozeActiv = true;
		    }
		}
		return snoozeActiv;
=======
		//SQLiteDatabase db= this.getReadableDatabase();
		
		
		return false;
>>>>>>> origin/master
	}
	
	
	// add User Code
	void addUserCode(String code){
		SQLiteDatabase db= this.getWritableDatabase();
		
		//content Typ, to import String code as table value
		ContentValues cv = new ContentValues();
		cv.put("code", code);
		
		db.insert("user", "code", cv);
		db.close();
	}
	
	// add Week times
	void addDayTime(int day,String time){
		if(day<7 && day>0){
			SQLiteDatabase db= this.getWritableDatabase();
			
			ContentValues cv = new ContentValues();
			cv.put("day",day);
			//time in HH:MM:SS
			time += ":00";
			cv.put("time", time);
			
			db.insert("time", null, cv);
			db.close();
		}
	}
	
<<<<<<< HEAD
=======
	/*
	public Cursor getUserByID(int id){
		 SQLiteDatabase db=this.getReadableDatabase();
		 return db.rawQuery("SELECT ID as _id, name FROM "+tabUser+
			" WHERE ID="+id, null);
	}
	*/
	

	
>>>>>>> 9e3cef2b82999438e2ae9d4a4ab0ad84638b2335
	
	
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
	


