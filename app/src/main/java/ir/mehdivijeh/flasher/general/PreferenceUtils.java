package ir.mehdivijeh.flasher.general;

import android.content.Context;
import android.content.SharedPreferences;

import ir.mehdivijeh.flasher.general.repo.db.LocalDb;


public class PreferenceUtils {

    private static SharedPreferences sharedPreferences;

    public static void initPreferenceUtils(Context context) {
        if (sharedPreferences == null && context != null) {
            sharedPreferences =
                    context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static String getStringPreference(String tag) {
        if (sharedPreferences == null) return null;
        return sharedPreferences.getString(tag, null);
    }

    public static void putStringPreference(String tag, String value) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(tag, value).apply();
    }

    public static long getLongPreference(String tag) {
        if (sharedPreferences == null) return -1;
        return sharedPreferences.getLong(tag, -1);
    }

    public static void putLongPreference(String tag, long value) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putLong(tag, value).apply();
    }

    public static boolean getBooleanPreference(String pref) {
        if (sharedPreferences == null)
            return false;
        return sharedPreferences.getBoolean(pref, false);
    }

    public static void putBooleanPreference(String pref, boolean value) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putBoolean(pref, value).apply();
    }


    public static void removeAllSharedPreferences() {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().clear().apply();
        LocalDb.deleteAll();
    }
}
