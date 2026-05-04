package com.programmsoft.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDeepLinkBuilder
import com.programmsoft.broadcasts.NotificationReceivers
import com.programmsoft.sevgisherlari.App
import com.programmsoft.fragments.DialogFragment
import com.programmsoft.sevgisherlari.R
import com.programmsoft.models.ContentDataItem
import com.programmsoft.sevgisherlari.MainActivity
import com.programmsoft.room.database.RoomDB
import com.programmsoft.room.entity.ContentData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Functions {
    val db = RoomDB.getInstance(App.instance)
    const val NOTIFICATION_CHANNEL = "ContentNotification"
    const val NOTIFICATION_ID = 100

    val fragmentList = setOf(
        R.id.nav_home,
        R.id.nav_menu,
        R.id.nav_bookmark,
        R.id.nav_settings,
        R.id.fragment_search,
        R.id.fragment_category
    )
    val destinationsWithoutBottomNav = listOf(R.id.fragment_search, R.id.fragment_category)
//    private val categoryUzbList = listOf(
//        "Inson","Narsa","Orol","Ko'prik","Yegulik","Hayvon","Shahar"
//    )
//    private val categoryEngList = listOf(
//        "human","thing","island","bridge","food","animal","city"
//    )

//    fun insertCategories() {
//        categoryUzbList.zip(categoryEngList).forEach { (categoryUzb, categoryEng) ->
//            val categories = ContentDataCategory(nameEng = categoryEng, nameUzb = categoryUzb)
//            db.contentDataCategoryDao().insert(categories)
//        }
//    }

    fun connectivityManager(context: Context) {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            ConnectivityManagers.networkRequest, ConnectivityManagers.networkCallback
        )
    }

    fun appearanceStatusNavigationBars(
        window: Window,
        mode: Int,
        appearanceLightStatusBars: Boolean,
        appearanceNavigationBars: Boolean
    ) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = true
        }
        if (mode == 0) {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = appearanceLightStatusBars
            wic.isAppearanceLightNavigationBars = appearanceNavigationBars
        } else {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = appearanceLightStatusBars
            wic.isAppearanceLightNavigationBars = appearanceNavigationBars
        }
    }

    fun statusNavigationBarsColor(
        window: Window,
        context: Context,
        colorStatus: Int,
        colorNavigation: Int
    ) {
        window.statusBarColor = ContextCompat.getColor(context, colorStatus)
        window.navigationBarColor = ContextCompat.getColor(context, colorNavigation)
    }

    private val categoriesList = listOf("Nutrition", "Fitness", "Mental Health", "Sleep Routine")

    fun insertData(contentResponse: ContentDataItem) {
        val existContent = db.contentDataDao().isContentExist(contentResponse.id)
        if (existContent == 0) {
            val content = ContentData()
            content.contentId = contentResponse.id
            val categoryIndex = categoriesList.indexOf(contentResponse.caption)
            content.categoryId = if (categoryIndex >= 0) categoryIndex + 1 else 1
            content.news = contentResponse.isNew
            content.text = contentResponse.text
            content.bookmark = 0
            db.contentDataDao().insert(content)
        }
    }

    fun showDialogWithArgument(fragmentManager: FragmentManager, contentId: Long) {
        val dialog = DialogFragment()
        val id = bundleOf("content_id" to contentId)
        dialog.arguments = id
        dialog.show(fragmentManager, "content_dialog")
    }

    fun showDialog(fragmentManager: FragmentManager, tag: String) {
        val dialog = DialogFragment()
        dialog.show(fragmentManager, tag)
    }

    fun rateApp(context: Context) {
        val playStoreUrl = "https://play.google.com/store/apps/details?id=" + context.packageName
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
        context.startActivity(browserIntent)
    }
    fun privacyPolice(context: Context) {
        val url = "http://programmsoft.uz/privacy_policy.html"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun otherApps(context: Context) {
        val url = context.resources.getString(R.string.google_play)
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        context.startActivity(launchBrowser)
    }

    fun shareApp(context: Context) {
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, App.instance.resources.getString(R.string.app_name) +
                        "\n" +
                        "\uD83D\uDC47Download app\uD83D\uDC47\n" +
                        "https://play.google.com/store/apps/details?id=${context.packageName}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }


    fun sendData(context: Context, data: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.putExtra("sms_body", data)
        sendIntent.type = "vnd.android-dir/mms-sms"
        context.startActivity(sendIntent)
    }

    fun shareData(context: Context, data: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun copyData(context: Context, data: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("label", data)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Nusxa olindi.", Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("InlinedApi")
    fun appNotifications(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun isAllowNotifications(context: Context): Boolean {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    fun showNotification(context: Context, contentId: Long) {
        val text = db.contentDataDao().getContentTextById(contentId)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("from_notification", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bundle = Bundle().apply {
            putBoolean("from_notification", true)
        }
        val deepLink = NavDeepLinkBuilder(context).setGraph(R.navigation.content_nav)
            .setComponentName(MainActivity::class.java).setDestination(R.id.nav_home)
            .setArguments(bundle).createPendingIntent()
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setSmallIcon(R.drawable.logo_notification)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(deepLink)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setChannelId(NOTIFICATION_CHANNEL)
            .setAutoCancel(true)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun setTimeOfAlarmManager(context: Context) {
        val hour = SharedPreference.notificationHour
        val minute = SharedPreference.notificationMinute
        if (System.currentTimeMillis() > getDateInMilliseconds(getDate(), hour, minute)) {
            setAlarmManager(
                1, context, getDateInMilliseconds(getDateAddedDay(1), hour, minute)
            )
        } else {
            setAlarmManager(
                1, context, getDateInMilliseconds(getDate(), hour, minute)
            )
        }

    }

    private fun setAlarmManager(requestCode: Int, context: Context, time: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createPendingIntent(context, requestCode)
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, NotificationReceivers::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        intent.putExtra("request_code", requestCode)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager ?: return
        val importance = NotificationManager.IMPORTANCE_HIGH
        val id = NOTIFICATION_CHANNEL
        val name = NOTIFICATION_CHANNEL
        val channel = NotificationChannel(id, name, importance)
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(channel)
    }

    fun getDateInMilliseconds(date: String, hour: Int, minute: Int): Long {
        val updatedDateTimeString = String.format(Locale.ENGLISH, "%02d:%02d %s", hour, minute, date)
        val format = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.ENGLISH)
        val d = format.parse(updatedDateTimeString)
        return d?.time ?: 0
    }

    fun getDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat.format(currentDate)
    }

    fun getDateAddedDay(addDay: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, addDay)
        val nextDate = calendar.time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat.format(nextDate)
    }
    fun preloadMockData() {
        val count = db.contentDataDao().getAllContent().size
        if (count == 0) {
            val tips = listOf(
                "Drink a glass of water immediately after waking up to kickstart your metabolism and hydrate your brain.",
                "Take a 5-minute break every hour to stand up, stretch, and look at something 20 feet away to rest your eyes.",
                "Incorporate 15 minutes of dynamic stretching into your morning routine to improve flexibility and blood flow.",
                "Avoid screens 1 hour before bed. The blue light inhibits melatonin production, making it harder to fall asleep.",
                "Practice the 4-7-8 breathing technique when feeling stressed: inhale for 4s, hold for 7s, exhale for 8s.",
                "Try swapping out one processed snack for a handful of nuts or fresh fruit today.",
                "Go for a brisk 10-minute walk after your heaviest meal to stabilize blood sugar levels.",
                "Keep a journal by your bed and write down three things you are grateful for before going to sleep.",
                "Keep your bedroom cool, ideally around 65°F (18°C), to promote deeper, more restorative sleep.",
                "Include a source of protein in every meal to keep your energy levels stable throughout the day.",
                "Try to get at least 15 minutes of sunlight exposure every morning to help regulate your circadian rhythm.",
                "Swap your second cup of coffee for herbal tea or green tea to avoid the afternoon caffeine crash.",
                "Do 20 squats while brushing your teeth to effortlessly sneak some lower-body exercise into your day.",
                "Establish a calming pre-sleep ritual, such as reading a book or listening to soft music.",
                "Take 10 deep belly breaths before responding to a frustrating email or message.",
                "Take the stairs instead of the elevator whenever you have the option. Small habits compound over time.",
                "Chew your food slowly and mindfully. It improves digestion and prevents overeating.",
                "Set an alarm to go to bed at the exact same time every day, even on weekends.",
                "Unfollow social media accounts that make you feel anxious or inadequate.",
                "Engage your core by sitting up perfectly straight for 5 minutes every time you sit at your desk."
            )
            val categories = listOf(1, 3, 2, 4, 3, 1, 2, 3, 4, 1, 3, 1, 2, 4, 3, 2, 1, 4, 3, 2)
            for (i in tips.indices) {
                val item = com.programmsoft.room.entity.ContentData()
                item.contentId = (i + 1).toLong()
                item.text = tips[i]
                item.categoryId = categories[i]
                item.news = 1
                item.bookmark = 0
                db.contentDataDao().insert(item)
            }
        }
    }

}