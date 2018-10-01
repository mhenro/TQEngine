package com.mhenro

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Json
import com.mhenro.engine.QuestEngine
import com.mhenro.engine.model.QuestGame
import com.mhenro.screens.GameScreen
import com.mhenro.screens.MainMenuScreen
import com.mhenro.screens.OptionsScreen


class MyGdxGame : Game() {
    private val tag = MyGdxGame::class.java.simpleName

    companion object {
        lateinit var gameQuest: QuestGame
        lateinit var questEngine: QuestEngine
        lateinit var gameSkin: Skin
        lateinit var soundClick: Sound
        lateinit var music: Music
        lateinit var gamePrefs: Preferences
    }

    override fun create() {
        gameQuest = loadQuest()
        questEngine = QuestEngine(gameQuest)
        questEngine.validateQuest()

        gamePrefs = Gdx.app.getPreferences("settings")

        gameSkin = Skin(Gdx.files.internal("sgxui/sgx-ui.json"))
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/main_theme.mp3"))
        soundClick = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_click.mp3"))
        this.setScreen(MainMenuScreen(this))

        playMusic()
    }

    private fun loadQuest(): QuestGame {
        val json = Json()
        return json.fromJson(QuestGame::class.java, Gdx.files.internal("quest.json"))
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
        val prefs = Gdx.app.getPreferences("settings")
        val lastTime = prefs.getString("lastTime", "fuuuu")
        Gdx.app.log(tag, lastTime)
        Gdx.app.log(tag, "on pause")
        prefs.putString("lastTime", "10:10:10")
        prefs.flush()
    }

    override fun resume() {
        Gdx.app.log(tag, "on resume")
    }

    override fun dispose() {
    }
}