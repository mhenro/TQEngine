package com.mhenro

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.mhenro.engine.QuestEngine
import com.mhenro.screens.MainMenuScreen
import org.joda.time.DateTime
import java.time.LocalDateTime
import java.util.*


class MyGdxGame : Game() {
    private val tag = MyGdxGame::class.java.simpleName
    lateinit var notificationHandler: NotificationHandler

    companion object {
        lateinit var questEngine: QuestEngine
        lateinit var gameSkin: Skin
        lateinit var soundClick: Sound
        lateinit var music: Music
        lateinit var gamePrefs: Preferences
        lateinit var i18NBundle: I18NBundle
    }

    override fun create() {
        questEngine = QuestEngine.getEngine(this)
        gamePrefs = Gdx.app.getPreferences("settings")
        gameSkin = Skin(Gdx.files.internal("sgxui/sgx-ui.json"))
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/main_theme.mp3"))
        soundClick = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_click.mp3"))
        loadLanguage()
        initI18NBundle()
        playMusic()
        notificationHandler.stopNotifications()

        /* load previously saved data */
        if (gamePrefs.getString("history").isNotEmpty()) {
            val history = gamePrefs.getString("history").split(",").map { it.trim().toInt() }.toList()
            MyGdxGame.questEngine.addToHistory(history)
        }
        if (gamePrefs.getString("inventory").isNotEmpty()) {
            val inventory = gamePrefs.getString("inventory").split(",").map { it.trim().toInt() }.toList()
            MyGdxGame.questEngine.addToInventory(inventory)
        }

        this.setScreen(MainMenuScreen(this))
    }

    private fun loadLanguage() {
        val language = gamePrefs.getString("selectedLanguage", "en")
        MyGdxGame.questEngine.setLanguage(language)
    }

    fun initI18NBundle() {
        val file = Gdx.files.internal("i18n/SystemMessages")
        val locale = Locale(questEngine.getLanguage())
        i18NBundle = I18NBundle.createBundle(file, locale, "UTF-8")
    }

    fun playMusic() {
        if (gamePrefs.getBoolean("musicEnabled", true)) {
            music.play()
            music.isLooping = true
        } else {
            music.stop()
        }
    }

    fun playClick() {
        if (gamePrefs.getBoolean("soundEnabled", true)) {
            soundClick.play(0.2f)
        } else {
            soundClick.stop()
        }
    }

    override fun render() {
        super.render()

//
//        batch.begin()
//        batch.draw(img, 0f, 0f)
//        batch.end()
    }

    override fun pause() {
        MyGdxGame.gamePrefs.putString("history", MyGdxGame.questEngine.getHistory().joinToString())
        MyGdxGame.gamePrefs.putString("inventory", MyGdxGame.questEngine.getPlayerInventoryItemIds().joinToString())
        MyGdxGame.gamePrefs.flush()
    }

    override fun resume() {
        Gdx.app.log(tag, "on resume")
    }

    override fun dispose() {

    }
}