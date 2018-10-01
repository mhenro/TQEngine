package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame

class OptionsScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = GameScreen::class.java.simpleName

    init {
        createLayout()
    }

    private fun createLayout() {
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createTitle()).expandX().fill()
        wrapper.add(createCloseButton()).align(Align.left).padRight(15f)
        wrapper.row().fill().expand().padLeft(5f).padRight(5f)
        wrapper.add(createContentList())
        wrapper.layout()
    }

    private fun createTitle(): Actor {
        val title = Label("\nOPTIONS\n", MyGdxGame.gameSkin)
        title.setAlignment(Align.center)
        return title
    }

    private fun createCloseButton(): Actor {
        val btnClose = Button(MyGdxGame.gameSkin, "close")
        btnClose.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = GameScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnClose
    }

    private fun createContentList(): Actor {
        val list = Table()
        createSoundCheckbox(list)
        createMusicCheckbox(list)

        list.row().expand().padBottom(15f)
        list.add()

        val scrollPane = ScrollPane(list)
        scrollPane.layout()

        return scrollPane
    }

    private fun createSoundCheckbox(list: Table) {
        list.row().padBottom(15f)
        val chkSound = CheckBox("Sounds", MyGdxGame.gameSkin)
        chkSound.isChecked = MyGdxGame.gamePrefs.getBoolean("soundEnabled", true)
        list.add(chkSound).left().left().padLeft(15f)

        chkSound.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                MyGdxGame.gamePrefs.putBoolean("soundEnabled", (event.listenerActor as CheckBox).isChecked)
                MyGdxGame.gamePrefs.flush()
                game.playClick()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
    }

    private fun createMusicCheckbox(list: Table) {
        list.row().padBottom(15f)
        val chkMusic = CheckBox("Music", MyGdxGame.gameSkin)
        chkMusic.isChecked = MyGdxGame.gamePrefs.getBoolean("musicEnabled", true)
        list.add(chkMusic).left().left().padLeft(15f)
        chkMusic.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                MyGdxGame.gamePrefs.putBoolean("musicEnabled", (event.listenerActor as CheckBox).isChecked)
                MyGdxGame.gamePrefs.flush()
                game.playMusic()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
    }
}