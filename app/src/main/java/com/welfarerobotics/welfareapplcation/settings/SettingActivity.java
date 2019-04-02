package com.welfarerobotics.welfareapplcation.settings;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;

public class SettingActivity extends PreferenceActivity{
    private LocationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        Preference BackButton = findPreference("BackButton");
        Preference SetupWifi = findPreference("SetupWifi");
        
        BackButton.setOnPreferenceClickListener(preference -> {
            onBackPressed();
            return false;
        });
        SetupWifi.setOnPreferenceClickListener(preference -> {
            manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getApplicationContext(), "Wi-Fi 설정을 하려면 GPS를 켜야 합니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LocSettingsActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }
}
