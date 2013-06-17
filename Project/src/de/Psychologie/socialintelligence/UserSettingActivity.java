package de.Psychologie.socialintelligence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/**
* @class UserSettingActivity
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file UserSettingActivity.java
*
* @brief //TODO Diese Klasse macht.....
*
* 
*
* 
*/
public class UserSettingActivity extends PreferenceActivity {

	private boolean altUri= false;
	private boolean tested= false;
	private boolean playing=false;
	
	//TODO: why is this always created?

	private MediaPlayer mMediaPlayer;

	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityRegistry.register(this);
		//Add View for ActionBar
		setContentView(R.layout.custom_preferences);

		addPreferencesFromResource(R.xml.preferences);
		seekAndImportSongs();
		setSummaries();
		
		Preference button_week = findPreference("button_week");
		button_week.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						startActivity(new Intent(UserSettingActivity.this,
								Week.class));
						overridePendingTransition(0, 0);
						return true;
					}
				});
		Preference button_poll = findPreference("button_poll");
		button_poll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						startActivity(new Intent(UserSettingActivity.this,
								PopPollActivity.class));
						return true;
					}
				});		
		

		Preference button_test = findPreference("button_test");
		button_test
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				startActivity(new Intent(UserSettingActivity.this,	Alarm_Activity.class));
				return true;
			}
				});
		
		Preference button_about = findPreference("button_about");
		button_about
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				
					@Override
					public boolean onPreferenceClick(Preference arg0) {
						AlertDialog ad = new AlertDialog.Builder(
								UserSettingActivity.this).create();
						ad.setTitle(getResources().getString(
								R.string.title_about));
						
						   PackageInfo pinfo = null;
						try {
							pinfo = getPackageManager().getPackageInfo(getPackageName(),0);
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
							String versionName = pinfo.versionName;
							
					    //SpannableString about_text = new SpannableString(getResources().getString(R.string.message_about));
						//about_text.setSpan(new StyleSpan(Type), start, end, flags)
					    
						ad.setMessage(Html.fromHtml(getResources().getString(
								R.string.message_about)+getResources().getString(R.string.version)+" "+versionName));
					
						
						ad.setButton(getResources().getString(
								R.string.OK),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						ad.show();
						return true;
					}
				});

		CustomRingtonepreference ringtonepref = (CustomRingtonepreference) findPreference("ringtone");
		ringtonepref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,Object newValue) {
						
						//Log.d("OnprefChange", "newValue="+newValue.toString());
						Uri ringtoneUri = Uri.parse((String) newValue);
						String name = RingtoneManager.getRingtone(UserSettingActivity.this, ringtoneUri).getTitle(UserSettingActivity.this);
						if (name.equals("cygnus.ogg"))
							name = "cygnus";
						preference.setSummary( name);
						// Save Ringtone to preferences
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("ringtone", ringtoneUri.toString());
						editor.commit();

						return true;
					}
				});
		
		Preference volume = findPreference("volumepref");
		volume.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				
				
				LayoutInflater factory = LayoutInflater.from(UserSettingActivity.this);
				
				final View textEntryView = factory.inflate(R.layout.seekbar, null);
				
				final AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingActivity.this);

				
				alert.setTitle(getResources().getString(R.string.settings_volume)).setView(textEntryView);
				alert.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	   try {
				                			if (mMediaPlayer != null){
				                				if (mMediaPlayer.isPlaying())
				                					mMediaPlayer.stop();		
				                				mMediaPlayer.release();
				                				mMediaPlayer = null;
				                			}
				               		} catch (IllegalStateException e) {
				               			e.printStackTrace();
				               		}	
								dialog.cancel();
								AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
								int currentVolume = audio
										.getStreamVolume(AudioManager.STREAM_ALARM);
								CheckBoxPreference vibrieren = (CheckBoxPreference) findPreference("vibrate");
								if (!vibrieren.isChecked() & currentVolume == 0) {
									vibrieren.setChecked(true);
								}
				                   }
				});
				
		        setVolumeControlStream(AudioManager.STREAM_ALARM);
		        try
		        {
		        	playing = false;
		        	SeekBar volumeSeekbar = (SeekBar)textEntryView.findViewById(R.id.seekBar1);
		            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));  

		            volumeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		            {
		                @Override
		                public void onStopTrackingTouch(SeekBar arg0) 
		                {
		                }

		                @Override
		                public void onStartTrackingTouch(SeekBar arg0) 
		                {
		                	if (!playing){
		                	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		                	String path = prefs.getString("ringtone", RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_ALARM).toString());
		                    try {
		                    	mMediaPlayer = new MediaPlayer();
			                    mMediaPlayer.reset();
			                    mMediaPlayer.setDataSource(path);
			                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			                    mMediaPlayer.setLooping(true);
								mMediaPlayer.prepare();
			                    mMediaPlayer.start();
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
		                    playing=true;
		                	}
		                		
		                }

		                @Override
		                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) 
		                {
		                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
		                }
		            });
		        }
		        catch (Exception e) 
		        {
		            e.printStackTrace();
		        }

				final AlertDialog dialog = alert.create();
				dialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						try {
							if (mMediaPlayer != null){
								if (mMediaPlayer.isPlaying())
									mMediaPlayer.stop();		
								mMediaPlayer.release();
								mMediaPlayer = null;
							}
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
	       				AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	       				int currentVolume = audio.getStreamVolume(AudioManager.STREAM_ALARM);
	       				CheckBoxPreference vibrieren = (CheckBoxPreference) findPreference("vibrate");
	       				if (!vibrieren.isChecked() & currentVolume==0){
	       					vibrieren.setChecked(true);
	       				}
					}
				});
				
				dialog.show();
				return true;
			}
		});
		//Make sure that it either rings or it vibrates
		CheckBoxPreference vibrieren = (CheckBoxPreference) findPreference("vibrate");
		vibrieren.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audio.getStreamVolume(AudioManager.STREAM_ALARM);
				if (!Boolean.valueOf(newValue.toString()) & currentVolume==0){
					Toast.makeText(UserSettingActivity.this,getResources().getString(R.string.muteAndnoVibrate), Toast.LENGTH_LONG).show();
					return false;
				}
					
				return true;
			}
		});
		
		ListPreference sleeptimechooser = (ListPreference) findPreference("Sleeptime");
		sleeptimechooser
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference.setSummary(newValue + " \tMinuten");
						return true;
					}
				});
		
		Preference button_admin_settings = findPreference("button_admin_settings");
		button_admin_settings
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(final Preference arg0) {
						
						final AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingActivity.this);
						builder.setTitle(getResources().getString(R.string.title_password_entry));
						final EditText input = new EditText(UserSettingActivity.this); 
					    final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(UserSettingActivity.this);
											    					 			    
						input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						builder.setView(input)
				               .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				           			//Passwortueberpruefung mit Salt
				                	//Falls kein PW gesetzt ist, ist das standart PW: 
				                 	if (MD5(input.getText().toString()+getResources().getString(R.string.salt)).equals(settings.getString("password", MD5(getResources().getString(R.string.std_PW)+getResources().getString(R.string.salt))))){
				                        finish();
				                 		startActivity(new Intent(UserSettingActivity.this,AdminSettingsActivity.class));
				                 		overridePendingTransition(0, 0);
				                  		}	
				                   else
				                   {				                	   
				   						Toast.makeText(UserSettingActivity.this,getResources().getString(R.string.false_password), Toast.LENGTH_SHORT).show();
				   						onPreferenceClick(arg0);
				                   }
				                   }
				               })
				               .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {				                	   
				                	   dialog.cancel();
				                   }
				               }); 
				        
						final AlertDialog dialog = builder.create();   
						
					      //show keyboard
						    input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						        @Override
						        public void onFocusChange(View v, boolean hasFocus) {
						            if (hasFocus) {
						                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
						            }
						        }
						    });
						    
				        dialog.show();
						return true;
					}
				});
	}

	@Override
	public void onBackPressed() {
		// Wenn es nicht der knoten ist, soll es geschlossen werden
		if (!this.isTaskRoot())
			this.finish();
		else
			super.onBackPressed();
	}

	@Override
	public void onPause() {		
		try {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
    	playing = false;
		super.onPause();
	}
	
	@Override
	public void onStop() {
		try {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
    	playing = false;
		super.onStop();
	}
	
	// MD5 Funktion f�r Passw�rter
	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100)
                        .substring(1, 3));
            }
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private void seekAndImportSongs(){
		CustomRingtonepreference ringtonepref = (CustomRingtonepreference) findPreference("ringtone");
		
		//Import All Ringtones
		RingtoneManager rm = new RingtoneManager(UserSettingActivity.this);
		rm.setType(RingtoneManager.TYPE_ALARM|RingtoneManager.TYPE_RINGTONE );
	    Cursor ringtones = rm.getCursor();
	    

	    Integer i=0;
	    List<String> mEntries = new ArrayList<String>();
	    List<String> mEntryValues = new ArrayList<String>();
	    for (ringtones.moveToFirst(); !ringtones.isAfterLast(); ringtones.moveToNext()) {
			mEntries.add(ringtones.getString(RingtoneManager.TITLE_COLUMN_INDEX));
	    	//Log.i("RingtoneEntry["+i+"]",mEntries.get(i));
	    	Uri UriWithWithoutID = Uri.parse(ringtones.getString(RingtoneManager.URI_COLUMN_INDEX));
	    	//FIX for URI ending
	    	//normal: ../media
	    	//alternative: ../media/[songID]
	    	if (!tested){
	    		tested = true;
	    		if (UriWithWithoutID.getLastPathSegment().matches("-?\\d+(\\.\\d+)?")) //match a number with optional '-' and decimal.
	    			altUri=true;
	    	}
	    	if (altUri)
	    		mEntryValues.add(UriWithWithoutID.toString());
	    	else
	    		mEntryValues.add(UriWithWithoutID.toString()+"/"+ringtones.getInt(RingtoneManager.ID_COLUMN_INDEX));    		
	    	//Log.i("RingtoneValue["+i+"]",mEntryValues.get(i));
	    	i++;
	    }
	    ringtones.deactivate();

	    //Save file to directory if device has no ringtone
	    if (i==0){
		    //Import cygnus.ogg for devices without ringtones
	    	Log.e("Import","You had no Ringtones");
		    int MySongName = R.raw.cygnus;
	    	FileHandler h = new FileHandler("cygnus.ogg");
	    	h.saveAudio(MySongName, UserSettingActivity.this);
	    
	    	File Dir = new File(Environment.getExternalStorageDirectory(),"media/audio/notifications");

	    	mEntries.add(getResources().getResourceEntryName(MySongName));
	    	mEntryValues.add(Dir.getAbsolutePath() +"/cygnus.ogg");
	    	RingtoneManager.setActualDefaultRingtoneUri(UserSettingActivity.this, RingtoneManager.TYPE_ALARM,Uri.parse(Dir.getAbsolutePath() +"/cygnus.ogg"));
	    	
	    }
	    else
	    	if(RingtoneManager.getActualDefaultRingtoneUri(UserSettingActivity.this, RingtoneManager.TYPE_ALARM).toString()=="")
	    		RingtoneManager.setActualDefaultRingtoneUri(UserSettingActivity.this, RingtoneManager.TYPE_ALARM,Uri.parse(mEntryValues.get(0)));
	    ringtonepref.setEntryValues(mEntryValues.toArray(new CharSequence[mEntryValues.size()]));
	    ringtonepref.setEntries(mEntries.toArray(new CharSequence[mEntries.size()]));		
	}
	
	@SuppressWarnings("deprecation")
	private void setSummaries(){
		// Get the xml/prefx.xml preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		CustomRingtonepreference ringtonepref = (CustomRingtonepreference) findPreference("ringtone");
		// Set Sleeptime summary to chosen time
		String sleeptimesummary = prefs.getString("Sleeptime", "5");
		Preference sleeptimepref = findPreference("Sleeptime");
		sleeptimepref.setSummary(sleeptimesummary + " \tMinuten");

		// Set Ringtonepreference summary to chosen title
		String ringtonename = prefs.getString("ringtone", "");
		Uri ringtoneUri;// Set std Alarm
		if (ringtonename.equals("")) {
			// Standart wird gesetzt, falls noch keiner da
			ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
					UserSettingActivity.this, RingtoneManager.TYPE_ALARM);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("ringtone", ringtoneUri.toString());
			editor.commit();
			// Sets the default Alarm to the chosen Value
			ringtonepref.setValue(ringtoneUri.toString());
		} else
			ringtoneUri = Uri.parse(ringtonename);

		Ringtone ringtone = RingtoneManager.getRingtone(
				UserSettingActivity.this, ringtoneUri);
		String name = ringtone.getTitle(UserSettingActivity.this);
		// release Ringtone
		ringtone.stop();

		// Set summary of Alarm
		ringtonepref.setSummary(name);
	}
}
