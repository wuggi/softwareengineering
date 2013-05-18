package de.Psychologie.socialintelligence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import android.os.Environment;
import android.util.Log;

// Rechte nötig
// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

public class FileHandler{
	private String filename;
	
	public FileHandler() {
		this("Umfrage.csv");
	}
	
	public FileHandler(String filename){
		this.filename = filename;
	}
	
	public void createExternalFile(String context){
	    File sdCard = Environment.getExternalStorageDirectory();
	    File dir = new File (sdCard.getAbsolutePath());
	    dir.mkdirs();
	    Log.i("dir",dir.toString());
	    Log.i("context","db="+context);
	    File file = new File(dir, this.filename);
	    try {
	        FileOutputStream f = new FileOutputStream(file,false); //True = Append to file, false = Overwrite
	        OutputStreamWriter out = new OutputStreamWriter(f);
	         out.write(context);
	         out.close();
	        
	        /*
	        PrintStream p = new PrintStream(f);
	        p.print(context);
	        p.close();
	        */
	        f.close();


	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        Log.i("test", "******* File not found. Did you" +
	                        " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.i("test", "hier ist wohl was falsch!");
	    }   
	}
}
