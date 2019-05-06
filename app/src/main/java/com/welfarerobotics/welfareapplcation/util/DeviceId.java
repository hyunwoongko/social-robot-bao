package com.welfarerobotics.welfareapplcation.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceId {
    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private volatile static UUID uuid;
    private Context context;

    @SuppressLint("StaticFieldLeak")
    private static DeviceId instance = null;

    public DeviceId(Context context) {
        this.context = context;
    }

    public static DeviceId getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceId(context);
        }
        return instance;
    }

    public String getUUID() {
        if (uuid == null) {
            synchronized (DeviceId.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(
                                context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId
                                        .getBytes("utf8"));
                            } else {
                                @SuppressLint("MissingPermission") final String deviceId = (
                                        (TelephonyManager) context
                                                .getSystemService(Context.TELEPHONY_SERVICE))
                                        .getDeviceId();
                                uuid = deviceId != null ? UUID
                                        .nameUUIDFromBytes(deviceId
                                                .getBytes("utf8")) : UUID
                                        .randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        // Write the value out to the prefs file
                        prefs.edit()
                                .putString(PREFS_DEVICE_ID, uuid.toString())
                                .commit();
                    }
                }
            }
        }
        return uuid.toString();
    }
}