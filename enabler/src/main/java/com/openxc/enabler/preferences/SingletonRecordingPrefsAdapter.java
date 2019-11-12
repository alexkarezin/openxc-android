package com.openxc.enabler.preferences;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openxcplatform.enabler.R;

public class SingletonRecordingPrefsAdapter extends RecyclerView.Adapter<SingletonRecordingPrefsAdapter.ViewHolder> {
    private final static String TAG  = SingletonSystemPreferences.class.getSimpleName();
    private static Context mContext;
    private static preferenceItem[] mPrefList;

    public static class preferenceItem {
        public final static int PREF_CHECKBOX = 1;
        public final static int PREF_STRING = 2;
        public final static int PREF_LAUNCH = 3;
        public String prefName;
        public String title;
        public String summary;
        public String defaultValue;
        public int prefType;
        public Class<?> launchActivity;
        public preferenceItem(String prefName, String title, String summary, String defaultValue, int prefType, Class<?>launchActivity) {
            this.prefName = prefName;
            this.title = title;
            this.summary = summary;
            this.defaultValue = defaultValue;
            this.prefType = prefType;
            this.launchActivity = launchActivity;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;          // Line 1 of the list item
        public TextView summary;        // Line 2 of the list item
        public CheckBox checkBox;
        public int index;
        public int type;
        public String prefName;
        public Class<?> launchActivity;

        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            summary = (TextView)view.findViewById(R.id.summary);
            checkBox = (CheckBox)view.findViewById(R.id.checkbox);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(type) {
                case preferenceItem.PREF_CHECKBOX:
                    checkBox.setChecked(!checkBox.isChecked());
                    SingletonSystemPreferences.setBoolean(prefName, checkBox.isChecked());
                    SingletonSystemPreferences.savePreferences();
                    break;
                case preferenceItem.PREF_STRING:
                    FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
                    EnterStringDialogFragment dialogFragment = EnterStringDialogFragment.newInstance(
                            title.getText().toString(), prefName,
                            SingletonSystemPreferences.getString(prefName));
                    dialogFragment.setSummary(summary);
                    dialogFragment.show(fragmentManager, "DIALOG_GET_STRING");
                    break;
                case preferenceItem.PREF_LAUNCH:
                    if (launchActivity != null) {
                        mContext.startActivity(new Intent(mContext, launchActivity));
                    }
                    break;
            }
        }
    }

    public SingletonRecordingPrefsAdapter(Context context, SingletonRecordingPrefsAdapter.preferenceItem[] prefList) {
        mContext = context;
        mPrefList = prefList;
    }

    @Override
    public SingletonRecordingPrefsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recording_prefs_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mPrefList[position].title);
        holder.summary.setText(mPrefList[position].summary);

        if (mPrefList[position].prefType == preferenceItem.PREF_STRING) {
            String currentValue = SingletonSystemPreferences.getString(mPrefList[position].prefName);
            Log.e(TAG, currentValue + "," + mPrefList[position].defaultValue);
            if ((currentValue != null) && (currentValue.length() > 0) &&
                    (currentValue.compareToIgnoreCase(mPrefList[position].defaultValue) != 0)) {
                holder.summary.setText(currentValue);       // Overwrite the summary Value
            }
        }

        holder.checkBox.setVisibility((mPrefList[position].prefType == preferenceItem.PREF_CHECKBOX) ? View.VISIBLE : View.INVISIBLE);
        if (mPrefList[position].prefType == preferenceItem.PREF_CHECKBOX) {
            holder.checkBox.setChecked(SingletonSystemPreferences.getBoolean(mPrefList[position].prefName));
            SingletonSystemPreferences.savePreferences();
        }
        holder.type = mPrefList[position].prefType;
        holder.index = position;
        holder.prefName = mPrefList[position].prefName;
        holder.launchActivity = mPrefList[position].launchActivity;
    }

    @Override
    public int getItemCount() {
        return mPrefList.length;
    }

    //
    // EnterStringDialogFragment
    //       Place the existing value of the string into edit text value
    //       If a value has been placed, save it to the "summary" aka the
    //       second line of list item.
    //

    public static class EnterStringDialogFragment extends DialogFragment {
        private static final String DIALOG_TITLE            = "DIALOG_TITLE";
        private static final String DIALOG_REGISTRY_NAME    = "DIALOG_REGISTRY_NAME";
        private static final String DIALOG_CURRENT          = "DIALOG_CURRENT";

        private TextView summary;

        public static EnterStringDialogFragment newInstance(String title,
                                                            String registryName,
                                                            String currentValue) {

            EnterStringDialogFragment stringDialogFragment = new EnterStringDialogFragment();
            //stringDialogFragment.setStyle( DialogFragment.STYLE_NORMAL, R.style.MyStyle );

            Bundle args = new Bundle();
            args.putString(DIALOG_TITLE, title);
            args.putString(DIALOG_REGISTRY_NAME, registryName);
            args.putString(DIALOG_CURRENT, currentValue);
            stringDialogFragment.setArguments(args);

            return stringDialogFragment;
        }

        // Set the 2nd line of text for the list item view
        public void setSummary(TextView textView) {
            summary = textView;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_fragment_enter_string, null);

            Bundle bundle = getArguments();
            String title = bundle.getString(DIALOG_TITLE);
            final String registryName = bundle.getString(DIALOG_REGISTRY_NAME);
            String currentValue = bundle.getString(DIALOG_CURRENT);

            getDialog().setTitle(title);
            final EditText editText = view.findViewById(R.id.dialog_edit_text);
            editText.setText(currentValue);

            //
            // OK Button
            //

            view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                    String result = editText.getText().toString();
                    SingletonSystemPreferences.setString(registryName, result);
                    SingletonSystemPreferences.savePreferences();

                    if (summary != null) {
                         if ((result != null) && (result.length() > 0)) {
                             summary.setText(result);
                         }
                    }
                }
            });

            //
            // CANCEL Button
            //

            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            return view;
        }
    }

}