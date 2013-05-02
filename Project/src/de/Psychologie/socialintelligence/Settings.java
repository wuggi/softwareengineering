package de.Psychologie.socialintelligence;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	/*
	 * File newSoundFile = new File("/sdcard/media/ringtone", "myringtone.oog");
	 * Uri mUri =
	 * Uri.parse("android.resource://com.your.package/R.raw.your_resource_id");
	 * ContentResolver mCr = app.getContentResolver(); AssetFileDescriptor
	 * soundFile; try { soundFile= mCr.openAssetFileDescriptor(mUri, "r"); }
	 * catch (FileNotFoundException e) { soundFile=null; }
	 * 
	 * try { byte[] readData = new byte[1024]; FileInputStream fis =
	 * soundFile.createInputStream(); FileOutputStream fos = new
	 * FileOutputStream(newSoundFile); int i = fis.read(readData);
	 * 
	 * while (i != -1) { fos.write(readData, 0, i); i = fis.read(readData); }
	 * 
	 * fos.close(); } catch (IOException io) { }
	 * 
	 * ContentValues values = new ContentValues();
	 * values.put(MediaStore.MediaColumns.DATA, newSoundFile.getAbsolutePath());
	 * values.put(MediaStore.MediaColumns.TITLE, "my ringtone");
	 * values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/oog");
	 * values.put(MediaStore.MediaColumns.SIZE, newSoundFile.length());
	 * values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
	 * values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
	 * values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
	 * values.put(MediaStore.Audio.Media.IS_ALARM, true);
	 * values.put(MediaStore.Audio.Media.IS_MUSIC, false);
	 * 
	 * Uri uri =
	 * MediaStore.Audio.Media.getContentUriForPath(newSoundFile.getAbsolutePath
	 * ()); Uri newUri = mCr.insert(uri, values);
	 * 
	 * 
	 * try { RingtoneManager.setActualDefaultRingtoneUri(getContext(),
	 * RingtoneManager.TYPE_RINGTONE, newUri); } catch (Throwable t) {
	 * Log.d(TAG, "catch exception"); }
	 */

}
