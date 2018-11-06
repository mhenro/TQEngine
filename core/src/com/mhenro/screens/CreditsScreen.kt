package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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
        val splashImage = Image(MyGdxGame.gameSkin, "credits-screen")
        splashImage.setFillParent(true)
        stage.addActor(splashImage)

        stage.addActor(createTitle())
        stage.addActor(createCloseButton())

        val container = VerticalGroup()
        val labelName = Label("\n\n\n\n${MyGdxGame.questEngine.getQuestName()}\n\n\n\n", MyGdxGame.gameSkin, "default-white")
        val labelCredits = Label("${MyGdxGame.i18NBundle.get("authors")}\n\n", MyGdxGame.gameSkin, "default-white")
        val labelAuthor1 = Label("${MyGdxGame.i18NBundle.get("author1")}\n", MyGdxGame.gameSkin, "default-white")
        val labelAuthor2 = Label("${MyGdxGame.i18NBundle.get("author2")}\n", MyGdxGame.gameSkin, "default-white")
        val labelAuthor3 = Label("${MyGdxGame.i18NBundle.get("author3")}\n\n\n\n", MyGdxGame.gameSkin, "default-white")
        container.addActor(labelName)
        container.addActor(labelCredits)
        container.addActor(labelAuthor1)
        container.addActor(labelAuthor2)
        container.addActor(labelAuthor3)
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
        val btnStartGame = ImageTextButton("\n${MyGdxGame.i18NBundle.get("back")}\n", MyGdxGame.gameSkin)
        btnStartGame.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = MainMenuScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        btnStartGame.addAction(Actions.sequence(Actions.fadeOut(0.0001f), Actions.fadeOut(10f), Actions.fadeIn(2f)))
        return btnStartGame
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.playClick()
            game.screen = MainMenuScreen(game)
        }
    }
}