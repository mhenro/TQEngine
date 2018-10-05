package com.mhenro

import org.robovm.apple.uikit.UIApplication
import org.robovm.apple.uikit.UIUserNotificationSettings
import org.robovm.apple.uikit.UIUserNotificationType
import org.robovm.apple.foundation.NSTimeZone
import org.robovm.apple.uikit.UILocalNotification
import org.robovm.apple.foundation.NSDate
import org.robovm.apple.foundation.NSOperationQueue



class IOSNotificationImpl: NotificationHandler {
    init {
//        //Registers notifications, it will ask user if ok to receive notifications from this app, if user selects no then no notifications will be received
//        UIApplication.getSharedApplication().registerUserNotificationSettings(UIUserNotificationSettings.create(UIUserNotificationType.Alert, null));
//        UIApplication.getSharedApplication().registerUserNotificationSettings(UIUserNotificationSettings.create(UIUserNotificationType.Sound, null));
//        UIApplication.getSharedApplication().registerUserNotificationSettings(UIUserNotificationSettings.create(UIUserNotificationType.Badge, null));
//
//        //Removes notifications indicator in app icon, you can do this in a different way
//        UIApplication.getSharedApplication().setApplicationIconBadgeNumber(0);
//        UIApplication.getSharedApplication().cancelAllLocalNotifications();
    }

    override fun showNotification(title: String, text: String) {
        NSOperationQueue.getMainQueue().addOperation {
            val date = NSDate()
            //5 seconds from now
            val secondsMore = date.newDateByAddingTimeInterval(5.0)

            val localNotification = UILocalNotification()
            localNotification.fireDate = secondsMore
            localNotification.alertBody = title
            localNotification.alertAction = text
            localNotification.timeZone = NSTimeZone.getDefaultTimeZone()
            localNotification.applicationIconBadgeNumber = UIApplication.getSharedApplication().applicationIconBadgeNumber + 1

            UIApplication.getSharedApplication().scheduleLocalNotification(localNotification)
        }
    }
}