package com.openxc.enabler.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.buglabs.dweetlib.DweetLib;
import com.openxc.sinks.DweetSink;
import com.openxc.sinks.VehicleDataSink;
import com.openxcplatform.enabler.R;

/**
 * Enable or disable sending of vehicle data to dweet.io.
 *
 * The thingname to send data is read from the shared
 * preferences.
 */
public class DweetingPreferenceManager extends VehiclePreferenceManager {
    private final static String TAG = "DweetPreferenceManager";
    private VehicleDataSink mDweeter;

    public DweetingPreferenceManager(Context context) {
        super(context);
    }
    @Override
    public void close() {
        super.close();
        stopDweeting();
    }

    protected PreferenceListener createPreferenceListener() {
        return new PreferenceListenerImpl(this);
    }

    private void setDweetingStatus(boolean enabled) {
        Log.i(TAG, "Setting dweet to " + enabled);
        SharedPreferences.Editor editor = getPreferences().edit();
        String thingname = getPreferenceString(R.string.dweeting_thingname_key);
        if (thingname == null || thingname.equals("")) {
            thingname = DweetLib.getInstance(getContext()).getRandomThingName();
            editor.putString(getString(R.string.dweeting_thingname_key), thingname);
            editor.putString(getString(R.string.dweeting_thingname_default), thingname);
            editor.apply();
        }
        if(enabled) {
            if(mDweeter != null) {
                stopDweeting();
            }

            try {
                mDweeter = new DweetSink(getContext(), thingname);
            } catch(Exception e) {
                Log.w(TAG, "Unable to add dweet sink", e);
                return;
            }
            getVehicleManager().addSink(mDweeter);

        } else {
            stopDweeting();
        }
    }

    private void stopDweeting() {
        if(getVehicleManager() != null){
            Log.d(TAG,"removing Dweet sink");
            getVehicleManager().removeSink(mDweeter);
            mDweeter = null;
        }
    }

    /**
     * Internal implementation of the {@link VehiclePreferenceManager.PreferenceListener}
     * interface.
     */
    private static final class PreferenceListenerImpl extends PreferenceListener {

        private final static int[] WATCHED_PREFERENCE_KEY_IDS = {
                R.string.dweeting_checkbox_key,
                R.string.dweeting_thingname_key
        };

        /**
         * Main constructor.
         *
         * @param reference Reference to the enclosing class.
         */
        private PreferenceListenerImpl(final VehiclePreferenceManager reference) {
            super(reference);
        }

        @Override
        protected void readStoredPreferences() {
            final DweetingPreferenceManager reference
                    = (DweetingPreferenceManager) getEnclosingReference();
            if (reference == null) {
                Log.w(TAG, "Can not read stored preferences, enclosing instance is null");
                return;
            }

            reference.setDweetingStatus(reference.getPreferences().getBoolean(
                    reference.getString(R.string.dweeting_checkbox_key), false));
        }

        @Override
        protected int[] getWatchedPreferenceKeyIds() {
            return WATCHED_PREFERENCE_KEY_IDS;
        }
    }
}
