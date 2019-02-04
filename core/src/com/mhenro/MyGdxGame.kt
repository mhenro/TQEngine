package com.mhenro

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.mhenro.engine.QuestEngine
import com.mhenro.screens.GameScreen
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
        lateinit var ringStart: Sound
        lateinit var ringEnd: Sound
        lateinit var music: Music
        lateinit var menuMusic: Music
        lateinit var gamePrefs: Preferences
        lateinit var i18NBundle: I18NBundle
        var isTyping: Boolean = false
    }

    override fun create() {
        gameSkin = Skin(Gdx.files.internal("sgxui/sgx-ui.json"))
        this.setScreen(SplashScreen(this))

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
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu_theme.mp3"))
        soundClick = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"))
        messageReceived = Gdx.audio.newSound(Gdx.files.internal("sounds/notification.mp3"))
        ringStart = Gdx.audio.newSound(Gdx.files.internal("sounds/ring-start.mp3"))
        ringEnd = Gdx.audio.newSound(Gdx.files.internal("sounds/ring-end.mp3"))
        loadLanguage()
        initI18NBundle()

        /* load previously saved data */
        if (gamePrefs.getString("history").isNotEmpty()) {
            val history = gamePrefs.getString("history").split(",").map { it.trim().toInt() }.toList()
            questEngine.addToHistory(history)
        }
        if (gamePrefs.getString("inventory").isNotEmpty()) {
            val inventory = gamePrefs.getString("inventory").split(",").map { it.trim().toInt() }.toSet()
            questEngine.addToInventory(inventory)
        }
        if (gamePrefs.getString("completedChapters").isNotEmpty()) {
            val completedChapters =
                gamePrefs.getString("completedChapters").split(",").map { it.trim().toInt() }.toSet()
            questEngine.addToCompletedChapters(completedChapters)
        }
        if (gamePrefs.getString("selectedChoices").isNotEmpty()) {
            val selectedChoices = HashMap<Int, Int>()
            gamePrefs.getString("selectedChoices").split(",").forEach {
                val key = it.split("-")[0].trim().toInt()
                val value = it.split("-")[1].trim().toInt()
                selectedChoices[key] = value
                questEngine.addToSelectedChoices(selectedChoices)
            }
        }

        /* create toast factory */
        toastFactory = Toast.ToastFactory.Builder()
            .font(gameSkin.getFont("new-general-font-20"))
            .build()
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
        var defaultLanguage = gamePrefs.getString("selectedLanguage")
        try {
            if (defaultLanguage.isEmpty()) {
                defaultLanguage = java.util.Locale.getDefault().toString().split("_")[0]
                gamePrefs.putString("selectedLanguage", defaultLanguage)
                gamePrefs.flush()
            }
        } catch (e: Exception) {
            defaultLanguage = "en"
            gamePrefs.putString("selectedLanguage", defaultLanguage)
            gamePrefs.flush()
        }
        val language = defaultLanguage
        questEngine.setLanguage(language)
    }

    fun initI18NBundle() {
        val file = Gdx.files.internal("i18n/SystemMessages")
        val locale = Locale(questEngine.getLanguage())
        i18NBundle = I18NBundle.createBundle(file, locale, "UTF-8")
    }

    fun playMusic() {
        menuMusic.stop()
        if (music.isPlaying) {
            return
        }
        if (gamePrefs.getBoolean("musicEnabled", true)) {
            music.position = 9f
            music.play()
            music.isLooping = true
        } else {
            music.pause()
        }
    }

    fun playMenuMusic() {
        music.pause()
        if (menuMusic.isPlaying) {
            return
        }
        if (gamePrefs.getBoolean("musicEnabled", true)) {
            menuMusic.play()
            menuMusic.isLooping = true
        } else {
            menuMusic.stop()
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

    fun ringStart() {
        if (gamePrefs.getBoolean("soundEnabled", true)) {
            ringStart.play()
        } else {
            ringStart.stop()
        }
    }

    fun ringEnd() {
        if (gamePrefs.getBoolean("soundEnabled", true)) {
            ringEnd.play()
        } else {
            ringEnd.stop()
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
    }

    override fun pause() {
        gamePrefs.putString("history", questEngine.getHistory().joinToString())
        gamePrefs.putString("inventory", questEngine.getPlayerInventoryItemIds().joinToString())
        gamePrefs.putString("completedTime", questEngine.getCompletedTime())
        gamePrefs.putString("completedChapters", questEngine.getCompletedChapters().joinToString())
        gamePrefs.putString(
            "selectedChoices",
            questEngine.getSelectedChoices().map { (k, v) -> "$k-$v" }.joinToString()
        )
        gamePrefs.flush()
    }

    override fun resume() {
        Gdx.app.log(tag, "on resume")
    }

    override fun dispose() {
        music.dispose()
        menuMusic.dispose()
    }
}