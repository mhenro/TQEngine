package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame
import com.mhenro.engine.model.QuestGameNode

class GameOverScreen(private val game: MyGdxGame, private val endNode: QuestGameNode): AbstractGameScreen() {
    private val tag = GameOverScreen::class.java.simpleName

    init {
        createLayout()
        MyGdxGame.questEngine.stopQuest()
    }

    private fun createLayout() {
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createTitle()).expandX().fill()
        wrapper.add(createCloseButton()).align(Align.left).padRight(25f)
        wrapper.row().fill().expand().padLeft(5f).padRight(5f)
        wrapper.add(createContentList()).colspan(2)
        wrapper.layout()
    }

    private fun createTitle(): Actor {
        val title = Label("\nGAME OVER\n", MyGdxGame.gameSkin, "title")
        title.setAlignment(Align.center)
        return title
    }

    private fun createCloseButton(): Actor {
        val btnClose = Button(MyGdxGame.gameSkin, "close")
        btnClose.isTransform = true
        btnClose.scaleBy(0.5f)
        btnClose.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                MyGdxGame.questEngine.restartGame()
                game.screen = MainMenuScreen(game)
                return true
            }
        })
        return btnClose
    }

    private fun createContentList(): Actor {
        val list = Table()
        val text = Label(endNode.additionalParams.message!!.locale[MyGdxGame.questEngine.getLanguage()], MyGdxGame.gameSkin)
        text.setWrap(true)
        text.setAlignment(Align.center)
        list.add(text).center().fill().expand().padLeft(15f).padRight(15f)
        list.row()
        list.add(createBackButton()).fill().expandX().padLeft(15f).padRight(15f).padTop(30f).padBottom(30f)
        list.row()
        list.add(createRespawnButton()).fill().expandX().padLeft(15f).padRight(15f).padTop(30f).padBottom(30f)

        val scrollPane = ScrollPane(list)
        scrollPane.layout()

        return scrollPane
    }

    private fun createBackButton(): Actor {
        val btnStartGame = ImageTextButton(MyGdxGame.i18NBundle.get("back"), MyGdxGame.gameSkin)
        btnStartGame.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                MyGdxGame.questEngine.restartGame()
                game.screen = MainMenuScreen(game)
                return true
            }
        })
        return btnStartGame
    }

    private fun createRespawnButton(): Actor {
        val btnRespawn = ImageTextButton(MyGdxGame.i18NBundle.get("respawn"), MyGdxGame.gameSkin)
        btnRespawn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                if (game.googleServices.isAdVideoLoaded()) {
                    game.backToSavepoint = true
                    game.googleServices.showRewardedVideoAd()
                    game.screen = GameScreen(game)
                }
                return true
            }
        })
        return btnRespawn
    }
}