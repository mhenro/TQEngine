package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.mhenro.MyGdxGame

class GameScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = GameScreen::class.java.simpleName
    private lateinit var contentList: ScrollPane

    init {
        createLayout()
        MyGdxGame.questEngine.startQuest(contentList)
    }

    private fun createLayout() {
        wrapper.row().padTop(20f).padBottom(15f)
        wrapper.add(createContentsButton()).left().padLeft(15f)
        wrapper.add(createInventoryButton()).expandX().left().padLeft(15f).padBottom(20f)
        wrapper.add(createSettingsButton()).right().padRight(15f)
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
//        btnContents.addListener(TextTooltip("Contents", MyGdxGame.gameSkin))
        return btnContents
    }

    private fun createInventoryButton(): Actor {
        val btnInventory = ImageButton(MyGdxGame.gameSkin.getDrawable("inventory"))
//        btnInventory.isTransform = true
//        btnInventory.scaleBy(0.2f)
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
//        btnInventory.addListener(TextTooltip("Inventory", MyGdxGame.gameSkin))
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
                game.screen = OptionsScreen(game, GameScreen::class.java)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
//        btnSettings.addListener(TextTooltip("Settings", MyGdxGame.gameSkin))
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
//        btnMainMenu.addListener(TextTooltip("Main menu", MyGdxGame.gameSkin))
        return btnMainMenu
    }

    private fun createContentList(): Actor {
        val table = Table()
        contentList = ScrollPane(table)
        contentList.setScrollingDisabled(true, false)
        return contentList
    }
}
