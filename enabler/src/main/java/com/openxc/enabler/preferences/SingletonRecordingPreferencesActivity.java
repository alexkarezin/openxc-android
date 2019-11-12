package com.openxc.enabler.preferences;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.openxc.enabler.viewTraces;

import com.openxcplatform.enabler.R;


public class SingletonRecordingPreferencesActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton_recording_preferences);

        initializeRecordingPrefs();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_recording_prefs);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SingletonRecordingPrefsAdapter mAdapter = new SingletonRecordingPrefsAdapter(this, recordingPrefsList);
        recyclerView.setAdapter(mAdapter);
    }

    private int NUM_RECORDING_PREFS = 8;
    private SingletonRecordingPrefsAdapter.preferenceItem[] recordingPrefsList;

    private void initializeRecordingPrefs() {
        recordingPrefsList = new SingletonRecordingPrefsAdapter.preferenceItem[NUM_RECORDING_PREFS];
        recordingPrefsList[0] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.recording_checkbox_key),
                getString(R.string.recording_checkbox_title),
                getString(R.string.recording_checkbox_summary),
                null,
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_CHECKBOX,
                null);
        recordingPrefsList[1] = new SingletonRecordingPrefsAdapter.preferenceItem(
                "no_key",
                getString(R.string.view_recorded_traces),
                null,
                null,
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_LAUNCH,
                viewTraces.class);
        recordingPrefsList[2] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.recording_directory_key),
                getString(R.string.recording_directory_title),
                getString(R.string.recording_directory_summary),
                "openxc-traces",
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_STRING,
                null);
        recordingPrefsList[3] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.uploading_checkbox_key),
                getString(R.string.uploading_checkbox_title),
                getString(R.string.uploading_checkbox_summary),
                null,
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_CHECKBOX,
                null);
        recordingPrefsList[4] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.uploading_path_key),
                getString(R.string.uploading_path_title),
                getString(R.string.uploading_path_summary),
                "http://",
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_STRING,
                null);
        recordingPrefsList[5] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.uploading_source_name_key),
                getString(R.string.uploading_source_name_title),
                getString(R.string.uploading_source_name_summary),
                "unknown",
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_STRING,
                null);
        recordingPrefsList[6] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.dweeting_checkbox_key),
                getString(R.string.dweeting_checkbox_title),
                getString(R.string.dweeting_checkbox_summary),
                null,
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_CHECKBOX,
                null);
        recordingPrefsList[7] = new SingletonRecordingPrefsAdapter.preferenceItem(
                getString(R.string.dweeting_thingname_key),
                getString(R.string.dweeting_thingname_title),
                getString(R.string.dweeting_thingname_summary),
                "openxc-dweet",
                SingletonRecordingPrefsAdapter.preferenceItem.PREF_STRING,
                null);
    }
}
