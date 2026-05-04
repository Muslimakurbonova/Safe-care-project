package com.programmsoft.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.programmsoft.utils.Functions
import com.programmsoft.utils.SharedPreference

class ActionReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        SharedPreference.init(context!!)
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_BATTERY_CHANGED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            "android.intent.action.TIME_SET" -> {
                if (SharedPreference.isAllowNotification) {
                    Functions.setTimeOfAlarmManager(context!!)
                }
            }

            else -> {
                if (SharedPreference.isAllowNotification) {
                    Functions.setTimeOfAlarmManager(context!!)
                }
            }
        }
    }
}
