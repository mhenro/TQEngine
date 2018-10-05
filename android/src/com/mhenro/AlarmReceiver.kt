package com.mhenro

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, receivedIntent: Intent) {
        val title = receivedIntent.getStringExtra("title")
        val text = receivedIntent.getStringExtra("text")
        val dateTimeStr = receivedIntent.getStringExtra("dateTime")
        val format = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
        val dateTime = DateTime.parse(dateTimeStr, format)
        if (dateTime > DateTime.now()) {    //show notification only if time is expired
            return
        }
        val intent = Intent(context, AndroidLauncher::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(AndroidLauncher::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = buildLocalNotification(context, pendingIntent, title, text)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(AndroidLauncher.ALARM_TYPE_ELAPSE, notification)
    }

    private fun buildLocalNotification(context: Context, pendingIntent: PendingIntent, title: String, text: String): Notification {
        return NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(com.mhenro.R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .build()
    }
}