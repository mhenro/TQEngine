package com.mhenro

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

class IOSLauncher: IOSApplication.Delegate(), GoogleServices {
    override fun createApplication(): IOSApplication {
        val config = IOSApplicationConfiguration()
        val game = MyGdxGame(this)
        game.notificationHandler = IOSNotificationImpl()
        return IOSApplication(game, config)
    }

    override fun isAdVideoLoaded(): Boolean {
        return true
    }

    override fun loadRewardedVideoAd() {}

    override fun showRewardedVideoAd() {}

    override fun setVideoEventListener(listener: AdVideoEventListener) {}
}

fun main(argv: Array<String>) {
    val pool = NSAutoreleasePool()
    UIApplication.main<UIApplication, IOSLauncher>(argv, null, IOSLauncher::class.java)
    pool.close()
}