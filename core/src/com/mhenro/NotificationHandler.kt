package com.mhenro

import org.joda.time.DateTime

interface NotificationHandler {
    fun showNotification(title: String, text: String, dateTime: DateTime)
    fun stopNotifications()
}