package com.openxc.enabler.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.openxc.enabler.OpenXcEnablerApplication;
import java.lang.reflect.Field;

public class SingletonSystemPreferences {

    private final static String                     TAG = SingletonSystemPreferences.class.getSimpleName();
    private static SingletonSystemPreferences       mInstance = null;
    private SharedPreferences                       commonPrefs;

    // Memory copy of the System Preferences
    public String recording_output;                 // Key: recording_directory_key
    public String uploading_target;                 // Key: uploading_path_key
    public String uploading_source_name;            // Key: uploading_source_name_key
    public String dweeting_target;                  // Key: dweeting_thingname_key

    public boolean recording_enabled;               // Key: recording_checkbox_key. Check FileRecordingPreferenceManager::setFileRecordingStatus(boolean enabled) {
    public boolean uploading_enabled;               // Key: uploading_checkbox_key
    public boolean dweeting_enabled;                // Key: dweeting_checkbox_key

    // Need to restrict access to singleton so that multiple copies of the data do not exist at once
    public static SingletonSystemPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new SingletonSystemPreferences();
        }
        return mInstance;
    }

    // Note: Some System Preferences were formerly stored all in their individual repositories
    // instead of a single one with name value pairs

    private SingletonSystemPreferences() {      // Singletons use private constructors - There can be only 1 instance
        Context context = OpenXcEnablerApplication.getContext();

        //commonPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        commonPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        recording_output = commonPrefs.getString("recording_output", "openxc-traces");
        uploading_target = commonPrefs.getString("uploading_target", "http://");
        uploading_source_name = commonPrefs.getString("uploading_source_name", "unknown");
        dweeting_target = commonPrefs.getString("dweeting_target", "openxc-dweet");

        recording_enabled = commonPrefs.getBoolean("recording_enabled", false);
        uploading_enabled = commonPrefs.getBoolean("uploading_enabled", false);
        dweeting_enabled = commonPrefs.getBoolean("dweeting_enabled", false);
    }

    // From VehiclePreferenceManager.java
    // mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    // protected String getPreferenceString(int id) { return getPreferences().getString(mContext.getString(id), null); }

    // from FileRecordingPreferenceManager.java (Wrong way of setting the Pref Store Unit)
    //        SharedPreferences pref = getContext().getSharedPreferences("IsTraceRecording", Context.MODE_PRIVATE);
    //        SharedPreferences.Editor editor = pref.edit();
    //        editor.putBoolean("IsTraceRecording", enabled);
    //        editor.commit();

    public static void savePreferences() {
        SharedPreferences.Editor commonEditor = getInstance().commonPrefs.edit();
        commonEditor.putString("recording_output", getInstance().recording_output);
        commonEditor.putString("uploading_target", getInstance().uploading_target);
        commonEditor.putString("uploading_source_name", getInstance().uploading_source_name);
        commonEditor.putString("dweeting_target", getInstance().dweeting_target);

        commonEditor.putBoolean("recording_enabled", getInstance().recording_enabled);
        commonEditor.putBoolean("uploading_enabled", getInstance().uploading_enabled);
        commonEditor.putBoolean("dweeting_enabled", getInstance().dweeting_enabled);
        commonEditor.commit();
    }

    // Use reflection to get/set member names based upon their name.
    // In the case here the variable name in this class Matches the key name to avoid
    // confusion.

    public static boolean getBoolean(String fieldName) {
        boolean result = false;
        try {
            Field field = SingletonSystemPreferences.class.getField(fieldName);
            result = (boolean)field.get(getInstance());
        } catch (Exception exception) {
            // field name was not found!
            Log.e(TAG, "Failure getBoolean:"+exception.toString());
        }
        return result;
    }

    public static void setBoolean(String fieldName, boolean value) {
        try {
            Field field = SingletonSystemPreferences.class.getField(fieldName);
            field.set(getInstance(), value);
        } catch (Exception exception) {
            // field name was not found!
            Log.e(TAG, "Failure setBoolean:"+exception.toString());
        }
    }

    public static String getString(String fieldName) {
        String result = "";
        try {
            Field field = SingletonSystemPreferences.class.getField(fieldName);
            result = (String)field.get(getInstance());
        } catch (Exception exception) {
            // field name was not found!
            Log.e(TAG, "Failure getString:"+exception.toString());
        }
        return result;
    }

    public static void setString(String fieldName, String value) {
        try {
            Field field = SingletonSystemPreferences.class.getField(fieldName);
            field.set(getInstance(), value);
        } catch (Exception exception) {
            // field name was not found!
            Log.e(TAG, "Failure setString:"+exception.toString());
        }
    }

}
