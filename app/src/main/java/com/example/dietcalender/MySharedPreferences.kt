package com.example.dietcalender

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var bTime: String?
        get() = prefs.getString("btime", "00:00")
        set(value) = prefs.edit().putString("btime", value!!).apply()

    var lTime: String?
        get() = prefs.getString("ltime", "00:00")
        set(value) = prefs.edit().putString("ltime", value!!).apply()

    var dTime: String?
        get() = prefs.getString("dtime", "00:00")
        set(value) = prefs.edit().putString("dtime", value!!).apply()

    var bValue: Boolean?
        get() = prefs.getBoolean("breakfast", false)
        set(value) = prefs.edit().putBoolean("breakfast", value!!).apply()

    var lValue: Boolean?
        get() = prefs.getBoolean("lunch", false)
        set(value) = prefs.edit().putBoolean("lunch", value!!).apply()

    var dValue: Boolean?
        get() = prefs.getBoolean("dinner", false)
        set(value) = prefs.edit().putBoolean("dinner", value!!).apply()
}