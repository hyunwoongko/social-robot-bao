package com.welfarerobotics.welfareapplcation;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        Preference ConnectWifi = findPreference("ConnectWifi");
        ConnectWifi.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
            startActivity(intent);
            return false;
        });
    }
}
