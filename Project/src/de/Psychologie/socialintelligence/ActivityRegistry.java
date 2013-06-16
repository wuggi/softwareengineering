package de.Psychologie.socialintelligence;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
* @class ActivityRegistry
* @brief //TODO
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file ActivityRegistry.java 
*/ 
public class ActivityRegistry {
	/**
	 * @brief eine Liste aller registrierten Activities
	 */
	private static List<Activity> _activities;

	/**
	 * @param activity
	 * @brief fügt die activity zu der Liste von Activities hinzu
	 */
	public static void register(Activity activity) {
		if (_activities == null) {
			_activities = new ArrayList<Activity>();
		}
		_activities.add(activity);
	}

	/**
	 * @brief beendet alle Activities
	 * 
	 */
	public static void finishAll() {
		for (Activity activity : _activities) {
			activity.finish();
		}
	}
}
