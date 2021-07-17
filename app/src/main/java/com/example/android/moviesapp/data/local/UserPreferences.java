package com.example.android.moviesapp.data.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {


    // Initialization
    private static final String MyPREFERENCES = "MyPrefs";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;


    // Our constructor
    @SuppressLint("CommitPrefEdits")
    public UserPreferences(Context context) {
        pref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String retrieveData(String key) {
        return pref.getString(key, "");
    }
}
