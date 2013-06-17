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
}