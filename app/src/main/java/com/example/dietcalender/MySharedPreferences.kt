package com.example.dietcalender

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

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