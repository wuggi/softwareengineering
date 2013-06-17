<<<<<<< HEAD
package de.Psychologie.socialintelligence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;

// Rechte noetig
// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

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
}
=======
package de.Psychologie.socialintelligence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;

// Rechte noetig
// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

/**
* @class FileHandler
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file FileHandler.java
*
* @brief //TODO Diese Klasse macht.....
*
* 
*
* 
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
}
>>>>>>> fb53854a5727ab37faa63bfe3051e72ab6feb557
