package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.mhenro.MyGdxGame

class GameScreen(private val game: MyGdxGame,
                 private var doNotBack: Boolean = false) : AbstractGameScreen() {
    private val tag = GameScreen::class.java.simpleName
    private lateinit var contentList: ScrollPane

    init {
        createLayout()
        game.playMusic()
        MyGdxGame.questEngine.startQuest(contentList)
    }

    private fun createLayout() {
        wrapper.row().padTop(20f).padBottom(15f)
        wrapper.add(createContentsButton()).left().padLeft(15f)
        wrapper.add(createInventoryButton()).expandX().left().padLeft(60f).padBottom(20f)
        wrapper.add(createSettingsButton()).right().padRight(60f)
        wrapper.add(createMainMenuButton()).right().padRight(25f)
        wrapper.row().padBottom(5f).padTop(5f)
        wrapper.add(createContentList()).fill().expand().colspan(4)
        wrapper.layout()
    }

    private fun createContentsButton(): Actor {
        val btnContents = ImageButton(MyGdxGame.gameSkin.getDrawable("contents"))
        btnContents.isTransform = true
        btnContents.scaleBy(0.3f)
        btnContents.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                MyGdxGame.questEngine.stopQuest()
                game.screen = ContentsScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnContents
    }

    private fun createInventoryButton(): Actor {
        val btnInventory = ImageButton(MyGdxGame.gameSkin.getDrawable("inventory"))
        btnInventory.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                MyGdxGame.questEngine.stopQuest()
                game.screen = InventoryScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnInventory
    }

    private fun createSettingsButton(): Actor {
        val btnSettings = ImageButton(MyGdxGame.gameSkin.getDrawable("settings"))
        btnSettings.isTransform = true
        btnSettings.scaleBy(0.3f)
        btnSettings.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                MyGdxGame.questEngine.stopQuest()
                game.screen = OptionsScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnSettings
    }

    private fun createMainMenuButton(): Actor {
        val btnMainMenu = ImageButton(MyGdxGame.gameSkin.getDrawable("globe"))
        btnMainMenu.isTransform = true
        btnMainMenu.scaleBy(0.3f)
        btnMainMenu.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                MyGdxGame.questEngine.stopQuest()
                game.screen = MainMenuScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnMainMenu
    }

    private fun createContentList(): Actor {
        val table = Table()
        contentList = ScrollPane(table)
        contentList.setScrollingDisabled(true, false)
        return contentList
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!doNotBack) {
                game.playClick()
                game.screen = MainMenuScreen(game)
            }
        } else {
            if (doNotBack) {
                doNotBack = false
            }
        }
    }
}
