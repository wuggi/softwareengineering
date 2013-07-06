package de.Psychologie.socialintelligence;

import java.io.IOException;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
* @class CustomRingtonepreference
* @brief Klingeltonauswahl
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
* @file CustomRingtonepreference.java
*
* @brief Diese Klasse ahmt die Funktion des Ringtonedialogs nach, da dieser optisch nicht manipulierbar ist. Der hier erzeugte ist in jeder Form anpassbar.
* 
*/ 
public class CustomRingtonepreference extends ListPreference{

private MediaPlayer mMediaPlayer;
private CharSequence[] mEntries;
private CharSequence[] mEntryValues;
private int mClickedDialogEntryIndex;
private String mValue;

/**
 * 
 * @param context
 * @param attrs
 * @brief Konstruktor
 */
public CustomRingtonepreference(Context context, AttributeSet attrs) {
    super(context, attrs);
}
/**
 * 
 * @param context
 * @brief Konstruktor
 */
public CustomRingtonepreference(Context context) {
    super(context);
}
/**
 * Sets the value of the key. This should be one of the entries in
 * {@link #getEntryValues()}.
 * 
 * @param value The value to set for the key.
 */
public void setValue(String value) {
    mValue = value;

    persistString(value);
}
/**
 * @brief Sets the value to the given index from the entry values.
 * 
 * @param index The index of the value to set.
 */
public void setValueIndex(int index) {
    if (mEntryValues != null) {
        setValue(mEntryValues[index].toString());
    }
}

/**
 *@brief Returns the value of the key. This should be one of the entries in
 * {@link getEntryValues()}.
 * 
 * @return The value of the key.
 */
public String getValue() {
    return mValue; 
}

/**
 * Returns the entry corresponding to the current value.
 * 
 * @return The entry corresponding to the current value, or null.
 */
public CharSequence getEntry() {
    int index = getValueIndex();
    return index >= 0 && mEntries != null ? mEntries[index] : null;
}
/**
 * @brief findet den Index der Liste
 * @param Uri vom Klingelton
 * @return Index der Liste
 */
 public int findIndexOfValue(String value) {
    if (value != null && mEntryValues != null) {
        for (int i = mEntryValues.length - 1; i >= 0; i--) {
            if (mEntryValues[i].equals(value)) {
                return i;
            }
        }
    }
    return -1;
}

private int  getValueIndex() {

    return findIndexOfValue(mValue);
}

@Override
protected void onPrepareDialogBuilder(Builder builder) {
    super.onPrepareDialogBuilder(builder);

    mMediaPlayer = new MediaPlayer();
    mEntries = getEntries();
    mEntryValues = getEntryValues();

    if (mEntries == null || mEntryValues == null) {
        throw new IllegalStateException(
                "ListPreference requires an entries array and an entryValues array.");
    }

    mClickedDialogEntryIndex = getValueIndex();
    builder.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex,
            new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    mClickedDialogEntryIndex = which;

                    String value = mEntryValues[which].toString();
                    try {
                        playSong(value);
                    } catch (IllegalArgumentException e) {
            			e.printStackTrace();
            		} catch (SecurityException e) {
            			e.printStackTrace();
            		} catch (IllegalStateException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
                }
            });

    builder.setPositiveButton("OK", this);
    builder.setNegativeButton("Abbrechen", this);
}

private void playSong(String path) throws IllegalArgumentException,
    IllegalStateException, IOException {


    mMediaPlayer.reset();
    mMediaPlayer.setDataSource(path);
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
    mMediaPlayer.prepare();
    mMediaPlayer.start();
}

@Override
protected void onRestoreInstanceState(Parcelable state) {
    if (state == null || !state.getClass().equals(SavedState.class)) {
        // Didn't save state for us in onSaveInstanceState
        super.onRestoreInstanceState(state);
        return;
    }

    SavedState myState = (SavedState) state;
    super.onRestoreInstanceState(myState.getSuperState());
    setValue(myState.value);
}
/**
* @class SavedState
* @brief Standart Status speicherung
* @author Christian Steusloff, Jens Wiemann, Franz Kuntke und Patrick Wuggazer
* @date 16/06/2013
*/ 
private static class SavedState extends BaseSavedState {
    String value;

    public SavedState(Parcel source) {
        super(source);
        value = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(value);
    }

    public SavedState(Parcelable superState) {
        super(superState);
    }

    @SuppressWarnings("unused")
	public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}

 @Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult && mClickedDialogEntryIndex >= 0
				&& mEntryValues != null) {
			String value = mEntryValues[mClickedDialogEntryIndex].toString();
			if (callChangeListener(value)) {
				setValue(value);
			}
		}
		try {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
				}
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

@Override
protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getString(index);
}

@Override
protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    setValue(restoreValue ? getPersistedString(mValue) : (String) defaultValue);
}

@Override
protected Parcelable onSaveInstanceState() {
    final Parcelable superState = super.onSaveInstanceState();
    if (isPersistent()) {
        // No need to save instance state since it's persistent
        return superState;
    }

    final SavedState myState = new SavedState(superState);
    myState.value = getValue();
    return myState;
}
}