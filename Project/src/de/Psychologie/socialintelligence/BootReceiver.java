<<<<<<< HEAD
package de.Psychologie.socialintelligence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// Klasse startet mit dem Systemstart
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {	
	    //start activity
		Intent i = new Intent(context,BootActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
	}
=======
package de.Psychologie.socialintelligence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
* @class BootReceiver
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file BootReceiver.java

*
* @brief //TODO Diese Klasse macht.....
* Klasse startet mit dem Systemstart
* 
*
* 
*/ 
// Klasse startet mit dem Systemstart
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {	
	    //start activity
		Intent i = new Intent(context,BootActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
	}
>>>>>>> fb53854a5727ab37faa63bfe3051e72ab6feb557
}