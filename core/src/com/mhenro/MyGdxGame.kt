package com.mhenro

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.mhenro.engine.QuestEngine
import com.mhenro.screens.SplashScreen
import com.mhenro.utils.Toast
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

class MyGdxGame(val googleServices: GoogleServices) : Game(), AdVideoEventListener {
    init {
        this.googleServices.setVideoEventListener(this)
    }

    private val tag = MyGdxGame::class.java.simpleName
    lateinit var notificationHandler: NotificationHandler
    lateinit var networkManager: NetworkManager
    var backToSavepoint = false
    private val toasts = LinkedList<Toast>()
    private lateinit var toastFactory: Toast.ToastFactory

    companion object {
        lateinit var questEngine: QuestEngine
        lateinit var gameSkin: Skin
        lateinit var soundClick: Sound
        lateinit var messageReceived: Sound
        lateinit var music: Music
        lateinit var gamePrefs: Preferences
        lateinit var i18NBundle: I18NBundle
    }

    override fun create() {
        gameSkin = Skin(Gdx.files.internal("sgxui/sgx-ui.json"))
//        this.setScreen(SplashScreen(this))

        gamePrefs = Gdx.app.getPreferences("settings")

        /* load completedTime from preferences */
        var completedTime: DateTime?
        if (gamePrefs.getString("completedTime").isNotBlank()) {
            val formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
            val completedTimeStr = gamePrefs.getString("completedTime")
            completedTime = DateTime.parse(completedTimeStr, formatter)
        } else {
            completedTime = null
        }

        questEngine = QuestEngine.getEngine(this, completedTime)

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/main_theme.mp3"))
        soundClick = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"))
        messageReceived = Gdx.audio.newSound(Gdx.files.internal("sounds/notification-2.mp3"))
        loadLanguage()
        initI18NBundle()
        playMusic()

        /* load previously saved data */
        if (gamePrefs.getString("history").isNotEmpty()) {
            val history = gamePrefs.getString("history").split(",").map { it.trim().toInt() }.toList()
            MyGdxGame.questEngine.addToHistory(history)
        }
        if (gamePrefs.getString("inventory").isNotEmpty()) {
            val inventory = gamePrefs.getString("inventory").split(",").map { it.trim().toInt() }.toSet()
            MyGdxGame.questEngine.addToInventory(inventory)
        }
        if (gamePrefs.getString("completedChapters").isNotEmpty()) {
            val completedChapters = gamePrefs.getString("completedChapters").split(",").map { it.trim().toInt() }.toSet()
            MyGdxGame.questEngine.addToCompletedChapters(completedChapters)
        }

        /* create toast factory */
        toastFactory = Toast.ToastFactory.Builder()
                .font(MyGdxGame.gameSkin.getFont("new-general-font-20"))
                .build()

//        this.setScreen(MainMenuScreen(this))
        this.setScreen(SplashScreen(this))
    }

    override fun onRewardedEvent(type: String, amount: Int) {
        if (backToSavepoint) {
            backToSavepoint = false
            questEngine.respawn()
            questEngine.resumeQuest()
        } else {
            questEngine.skipWaiting()
        }
    }

    override fun onRewardedVideoAdLoadedEvent() {}

    override fun onRewardedVideoAdClosedEvent() {}

    private fun loadLanguage() {
        val language = "ru" //TODO: gamePrefs.getString("selectedLanguage", "en")
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
            soundClick.play()
        } else {
            soundClick.stop()
        }
    }

    fun messageReceived() {
        if (gamePrefs.getBoolean("soundEnabled", true)) {
            messageReceived.play()
        } else {
            messageReceived.stop()
        }
    }

    fun showLongToast(message: String) {
        toasts.add(toastFactory.create(message, Toast.Length.LONG))
    }

    fun showShortToast(message: String) {
        toasts.add(toastFactory.create(message, Toast.Length.SHORT))
    }

    override fun render() {
        super.render()

        /* handle toast queue and display */
        val it = toasts.iterator()
        while (it.hasNext()) {
            val t = it.next()
            if (!t.render(Gdx.graphics.deltaTime)) {
                it.remove() // toast finished -> remove
            } else {
                break // first toast still active, break the loop
            }
        }

//
//        batch.begin()
//        batch.draw(img, 0f, 0f)
//        batch.end()
    }

    override fun pause() {
        MyGdxGame.gamePrefs.putString("history", MyGdxGame.questEngine.getHistory().joinToString())
        MyGdxGame.gamePrefs.putString("inventory", MyGdxGame.questEngine.getPlayerInventoryItemIds().joinToString())
        MyGdxGame.gamePrefs.putString("completedTime", MyGdxGame.questEngine.getCompletedTime())
        MyGdxGame.gamePrefs.putString("completedChapters", MyGdxGame.questEngine.getCompletedChapters().joinToString())
        MyGdxGame.gamePrefs.flush()
    }

    override fun resume() {
        Gdx.app.log(tag, "on resume")
    }

    override fun dispose() {

    }
}