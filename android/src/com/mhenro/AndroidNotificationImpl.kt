package com.mhenro

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AndroidNotificationImpl(private val gameActivity: Activity): NotificationHandler {
    override fun showNotification(title: String, text: String, dateTime: DateTime) {
        val format = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
        scheduleNotifications(gameActivity, title, text, format.print(dateTime))
    }

    override fun stopNotifications() {
        val intent = Intent(gameActivity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(gameActivity, AndroidLauncher.ALARM_TYPE_ELAPSE, intent, 0)
        getAlarmManager().cancel(pendingIntent)
    }

    private fun scheduleNotifications(context: Context, title: String, text: String, dateTime: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("dateTime", dateTime)
        val alarmIntent = PendingIntent.getBroadcast(context, AndroidLauncher.ALARM_TYPE_ELAPSE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        getAlarmManager().setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR, alarmIntent)
    }

    private fun getAlarmManager(): AlarmManager {
        return gameActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}