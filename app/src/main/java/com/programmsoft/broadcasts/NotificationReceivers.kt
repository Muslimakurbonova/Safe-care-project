package com.programmsoft.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.programmsoft.utils.Functions
import com.programmsoft.utils.SharedPreference

class NotificationReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        SharedPreference.init(context!!)
        if (SharedPreference.isAllowNotification) {
            val prefCategory = SharedPreference.preferredCategory
            val categoryId = when(prefCategory) {
                "Nutrition" -> 1
                "Fitness" -> 2
                "Mental Health" -> 3
                "Sleep Routine" -> 4
                else -> 1
            }
            
            val randomTipId = Functions.db.contentDataDao().getRandomTipIdByCategory(categoryId)
            
            if (randomTipId != null) {
                Functions.showNotification(context, randomTipId)
            } else {
                // fallback
                if (Functions.db.contentDataDao().isContentExistById(SharedPreference.lastContentId) != 0) {
                    Functions.showNotification(context, SharedPreference.lastContentId)
                    SharedPreference.lastContentId += 1
                } else {
                    SharedPreference.lastContentId = 1
                    Functions.showNotification(context, SharedPreference.lastContentId)
                }
            }
            Functions.setTimeOfAlarmManager(context)
        }
    }
}