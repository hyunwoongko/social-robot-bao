package com.welfarerobotics.welfareapplcation.core.settings;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.alarm.AlarmActivity;
import com.welfarerobotics.welfareapplcation.core.menu.ConversationEdit;

public class SettingActivity extends PreferenceActivity {
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
        Preference BackButton = findPreference("BackButton");
        Preference SetupWifi = findPreference("SetupWifi");
        Preference TeachSentenceMenu = findPreference("TeachSentencesMenu");
        Preference Alarm = findPreference("Alarm");
        Preference Systemrefresh = findPreference("SystemRefresh");
        Preference info = findPreference("Info");

        info.setOnPreferenceClickListener(p -> {
            startActivity(new Intent(this, InfoActivity.class));
            return false;
        });

        Systemrefresh.setOnPreferenceClickListener(p -> {
            startActivity(new Intent(this, SysRefreshActivity.class));
            return false;
        });
        Alarm.setOnPreferenceClickListener(p -> {
            startActivity(new Intent(this, AlarmActivity.class));
            return false;
        });
        BackButton.setOnPreferenceClickListener(preference -> {
            onBackPressed();
            return false;
        });

        TeachSentenceMenu.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(this, ConversationEdit.class));
            return false;
        });
        SetupWifi.setOnPreferenceClickListener(preference -> {
            manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getApplicationContext(), "Wi-Fi 설정을 하려면 GPS를 켜야 합니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LocSettingsActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                startActivity(intent);
            }
            return false;

        });
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }
}
