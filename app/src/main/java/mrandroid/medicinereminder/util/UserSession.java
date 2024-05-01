package mrandroid.medicinereminder.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static final String PREF_NAME = "UserPrefs";
    private static final String IS_LOGIN = "isLoggedIn";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
