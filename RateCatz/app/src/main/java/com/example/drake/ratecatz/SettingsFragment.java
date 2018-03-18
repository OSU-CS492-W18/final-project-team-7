package com.example.drake.ratecatz;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v14.preference.PreferenceFragment;



/**
 * Created by gonzo on 3/17/2018.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
     /*   if (key.equals("pref_theme")) {
            EditTextPreference themePref = (EditTextPreference)findPreference(key);
            themePref.setSummary(themePref.getText());
        }*/
    }
     //adjust things if necessary

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //EditTextPreference userPref = (EditTextPreference) findPreference(getString(R.string.pref_theme_title));
        //userPref.setSummary(userPref.getText());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }
}
