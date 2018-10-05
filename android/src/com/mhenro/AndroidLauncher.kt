package com.mhenro

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    companion object {
        const val ALARM_TYPE_ELAPSE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        val game = MyGdxGame()
        val notificationHandler = AndroidNotificationImpl(this)
        game.notificationHandler = notificationHandler
        config.useAccelerometer = false
        config.useCompass = false
        initialize(game, config)
    }
}