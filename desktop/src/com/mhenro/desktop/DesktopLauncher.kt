package com.mhenro.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mhenro.MyGdxGame

class DesktopLauncher

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    val game = MyGdxGame()
    val notificationImpl = DesktopNotificationImpl()
    game.notificationHandler = notificationImpl
    LwjglApplication(game, config)
}