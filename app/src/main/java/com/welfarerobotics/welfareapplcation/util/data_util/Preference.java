package com.welfarerobotics.welfareapplcation.util.data_util;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @Author : Hyunwoong
 * @When : 2018-08-25 오전 4:09
 * @Homepage : https://github.com/gusdnd852
 */
public class Preference {

    private static Preference mInstance = null;
    private SharedPreferences mSharedPreference;

    private Preference(Context context) {
        this.mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized Preference get(Context context) {
        if (mInstance == null)
            mInstance = new Preference(context);
        return mInstance;
    } // Singleton


    public void remove(String paramString) {
        mSharedPreference.edit()
                .remove(paramString)
                .apply();
    }


    public boolean getBoolean(String paramString, boolean paramBoolean) {
        return mSharedPreference.getBoolean(paramString, paramBoolean);
    }

    public double getDouble(String paramString) {
        return Double.longBitsToDouble(mSharedPreference.getLong(paramString, Double.doubleToRawLongBits(-1.0D)));
    }

    public float getFloat(String paramString, float paramFloat) {
        return mSharedPreference.getFloat(paramString, paramFloat);
    }

    public int getInt(String paramString) {
        return mSharedPreference.getInt(paramString, -1);
    }

    public long getLong(String paramString, long paramLong) {
        try {
            return mSharedPreference.getLong(paramString, paramLong);
        } catch (ClassCastException e) {
            return 0L;
        }
    }

    public String getString(String paramString1, String paramString2) {
        return mSharedPreference.getString(paramString1, paramString2);
    }

    public boolean setBoolean(String paramString, boolean paramBoolean) {
        return mSharedPreference.edit()
                .putBoolean(paramString, paramBoolean)
                .commit();
    }

    public boolean setDouble(String paramString, double paramDouble) {
        return mSharedPreference.edit()
                .putLong(paramString, Double.doubleToRawLongBits(paramDouble))
                .commit();
    }

    public boolean setFloat(String paramString, float paramFloat) {
        return mSharedPreference.edit()
                .putFloat(paramString, paramFloat)
                .commit();
    }

    public boolean setInt(String paramString, int paramInt) {
        return mSharedPreference.edit()
                .putInt(paramString, paramInt)
                .commit();
    }

    public boolean setLong(String paramString, long paramLong) {
        return mSharedPreference.edit()
                .putLong(paramString, paramLong)
                .commit();
    }

    public boolean setString(String paramString1, String paramString2) {
        return mSharedPreference.edit()
                .putString(paramString1, paramString2)
                .commit();
    }
}