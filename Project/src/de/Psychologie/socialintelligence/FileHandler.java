package de.Psychologie.socialintelligence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;

// Rechte noetig
// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

/**
* @class FileHandler
* @brief Diese Klasse erleichtert das erstellen von Dateien wie CSV (Text) oder MP3 (Audio). //TODO
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file FileHandler.java
*/ 
public class FileHandler {
	private String filename;

	public FileHandler(String filename) {
		this.filename = filename;
	}

	public File createExternalFile(String context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File Dir = new File(Environment.getExternalStorageDirectory(),"Socialintelligence");

			if (!Dir.exists())
				Dir.mkdirs();

			File file = new File(Dir, this.filename);
			try {
				FileOutputStream f = new FileOutputStream(file, false);
				// True = Append to file, false = Overwrite
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(f,"UTF-8"));
				writer.write("Usercode;Datum;Alarmzeit;Antwortzeit;Abbruch;Kontakte;Stunden;Minuten\n");
				writer.write(context);
				writer.close();
				f.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		} else
			return null;
	}

	/**
	 * @brief Speichert die mitgebrachte Audiodatei auf dem Ger�t
	 * @param resSoundId
	 * @param context
	 * @return Ob Speichern erfolgreich verlief
	 */
	public boolean saveAudio(int resSoundId, Context context) {
		byte[] buffer;
		InputStream fIn = context.getResources().openRawResource(resSoundId);
		int size;

		try {
			size = fIn.available();
			buffer = new byte[size];
			fIn.read(buffer);
			fIn.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		File Dir = new File(Environment.getExternalStorageDirectory(),
				"media/audio/notifications");

		if (!Dir.exists())
			Dir.mkdirs();

		File file = new File(Dir, this.filename);
		FileOutputStream save;
		try {
			save = new FileOutputStream(file, false);
			save.write(buffer);
			save.flush();
			save.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * @brief Speichert die Datenbank in den �ffentlich zug�nglichen Ordner
	 * @param SQLHandler
	 * @return Ob Speichern erfolgreich verlief
	 */
	@SuppressWarnings("resource")
	public boolean saveDatabase(SQLHandler db){
		 try {
		        File sd = new File(Environment.getExternalStorageDirectory(),"Socialintelligence");
		        File data = Environment.getDataDirectory();

		        if (sd.canWrite()) {
		            String currentDBPath = "//data//de.Psychologie.socialintelligence//databases//socialintelligence.db";
		            File currentDB = new File(data, currentDBPath);	
		            File backupDB = new File(sd, this.filename);
		            if (currentDB.exists()) {
		                FileChannel src = new FileInputStream(currentDB).getChannel();
		                FileChannel dst = new FileOutputStream(backupDB).getChannel();
		                dst.transferFrom(src, 0, src.size());
		                src.close();
		                dst.close();
				    	return true;
		            }
		        }
		    } catch (Exception e) {
				e.printStackTrace();
		    	return false;
		    }
		return false;		
	}
}
