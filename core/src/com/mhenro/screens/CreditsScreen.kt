package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame

class CreditsScreen(private val game: MyGdxGame) : AbstractGameScreen() {
    private val tag = CreditsScreen::class.java.simpleName

    init {
        createLayout()
    }

    private fun createLayout() {
        stage.addActor(createTitle())
        stage.addActor(createCloseButton())

        val container = VerticalGroup()
        val labelName = Label("\n\n\n\n${MyGdxGame.questEngine.getQuestName()}\n\n\n\n", MyGdxGame.gameSkin)
        val labelCredits = Label("${MyGdxGame.i18NBundle.get("authors")}\n\n", MyGdxGame.gameSkin)
        val labelAuthor1 = Label("${MyGdxGame.i18NBundle.get("author1")}\n\n\n\n", MyGdxGame.gameSkin)
        container.addActor(labelName)
        container.addActor(labelCredits)
        container.addActor(labelAuthor1)
        container.width = stage.width
        container.height = stage.height
        container.setPosition(0f, -stage.height)
        container.addAction(Actions.moveTo(0f, 0f, 10f))
        container.addActor(createBackButton())
        stage.addActor(container)
    }

    private fun createTitle(): Actor {
        val title = Label("\n${MyGdxGame.i18NBundle.get("credits")}\n", MyGdxGame.gameSkin, "title")
        title.setAlignment(Align.center)
        title.setPosition(stage.width/2 - title.width/2, stage.height - title.height - 15)
        return title
    }

    private fun createCloseButton(): Actor {
        val btnClose = Button(MyGdxGame.gameSkin, "close")
        btnClose.isTransform = true
        btnClose.scaleBy(0.5f)
        btnClose.setPosition(stage.width - 30, stage.height - 30)
        btnClose.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                game.screen = MainMenuScreen(game)
                return true
            }
        })
        return btnClose
    }

    private fun createBackButton(): Actor {
        val btnStartGame = ImageTextButton("BACK TO MENU", MyGdxGame.gameSkin)
        btnStartGame.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                game.screen = MainMenuScreen(game)
                return true
            }
        })
        return btnStartGame
    }
}