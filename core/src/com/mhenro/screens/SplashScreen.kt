package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame

class SplashScreen(private val game: MyGdxGame) : AbstractGameScreen() {
    private val tag = SplashScreen::class.java.simpleName

    override fun show() {
        super.show()
        val splashImage = Image(MyGdxGame.gameSkin, "splash-2")
        splashImage.setFillParent(true)
        splashImage.addAction(Actions.sequence(Actions.fadeOut(0.001f), Actions.fadeIn(1f), Actions.fadeIn(1f), Actions.fadeOut(1f), Actions.run(onSplashFinishedRunnable)))
        stage.addActor(splashImage)
        createSplashTitle()
        createAuthorLabel()
    }

    private val onSplashFinishedRunnable = {
        game.screen = MainMenuScreen(game)
    }

    private fun createSplashTitle() {
        val label = Label(MyGdxGame.i18NBundle.get("game-title"), MyGdxGame.gameSkin, "title-2")
        label.setAlignment(Align.center)
        label.setWrap(true)
        label.setPosition(stage.width/2 - label.width/2, stage.height - label.height - 155)
        label.addAction(Actions.sequence(Actions.fadeOut(0.001f), Actions.fadeIn(1f), Actions.fadeIn(1f), Actions.fadeOut(1f)))
        stage.addActor(label)
    }

    private fun createAuthorLabel() {
        val label = Label(MyGdxGame.i18NBundle.get("game-author"), MyGdxGame.gameSkin, "default-white")
        label.setAlignment(Align.center)
        label.setWrap(true)
        label.setPosition(stage.width - label.width - 15, label.height)
        label.addAction(Actions.sequence(Actions.fadeOut(0.001f), Actions.fadeIn(1f), Actions.fadeIn(1f), Actions.fadeOut(1f)))
        stage.addActor(label)
    }
}