package com.example.dietcalender

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestartAlarmReceiver : BroadcastReceiver() {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private lateinit var functions: AlarmFunctions

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            functions = AlarmFunctions(context)
            coroutineScope.launch {
                var time = App.prefs.bTime.toString()
                var code = 1
                var content = "BREAKFAST"
                functions.callAlarm(time, code, content) // 알람 실행

                time = App.prefs.lTime.toString()
                code = 2
                content = "LUNCH"
                functions.callAlarm(time, code, content) // 알람 실행

                time = App.prefs.dTime.toString()
                code = 3
                content = "DINNER"
                functions.callAlarm(time, code, content) // 알람 실행
            }
        }
    }
}