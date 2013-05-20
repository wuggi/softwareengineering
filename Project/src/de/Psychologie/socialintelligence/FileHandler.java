package de.Psychologie.socialintelligence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;

// Rechte nötig
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
				Log.i("test","******* File not found. Did you add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		} else
			return null;
	}
}
