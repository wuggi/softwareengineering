package de.Psychologie.socialintelligence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
* @class BootReceiver
* @brief Klasse startet mit dem Systemstart, startet die {@link BootActivity}
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file BootReceiver.java
*/ 
public class BootReceiver extends BroadcastReceiver {

	/**
	 * @param context 
	 * @brief startet die {@link BootActivity} und setzt flags??
	 * @param intent a Intent
	 */
	@Override
	public void onReceive(Context context, Intent intent) {	
	    //start activity
		Intent i = new Intent(context,BootActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
	}
}