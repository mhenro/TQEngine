package com.mhenro.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mhenro.AdVideoEventListener
import com.mhenro.GoogleServices
import com.mhenro.MyGdxGame

class DesktopLauncher: GoogleServices {
    override fun isAdVideoLoaded(): Boolean {
        return true
    }

    override fun loadRewardedVideoAd() {

    }

    override fun showRewardedVideoAd() {

    }

    override fun setVideoEventListener(listener: AdVideoEventListener) {

    }
}

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    val launcher = DesktopLauncher()
    val game = MyGdxGame(launcher)
    val notificationImpl = DesktopNotificationImpl()
    game.notificationHandler = notificationImpl
    LwjglApplication(game, config)
}