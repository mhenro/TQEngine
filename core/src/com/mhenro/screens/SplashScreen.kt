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
        val splashImage = Image(MyGdxGame.gameSkin, "splash-screen")
        splashImage.setFillParent(true)
        splashImage.addAction(
            Actions.sequence(
                Actions.fadeOut(0.00001f),
                Actions.fadeIn(1f),
                Actions.fadeIn(1f),
                Actions.fadeOut(1f),
                Actions.run(onSplashFinishedRunnable)
            )
        )
        stage.addActor(splashImage)
        createSplashTitle()
//        createAuthorLabel()
        createCompanyLogo()
    }

    private val onSplashFinishedRunnable = {
        game.screen = MainMenuScreen(game)
        game.googleServices.prepareAds()
    }

    private fun createSplashTitle() {
        val label = Label(/*MyGdxGame.i18NBundle.get("game-title")*/"Stay Alive", MyGdxGame.gameSkin, "title-2")
        label.setAlignment(Align.center)
        label.setWrap(true)
        label.setPosition(stage.width / 2 - label.width / 2, stage.height - label.height - 155)
        label.addAction(
            Actions.sequence(
                Actions.fadeOut(0.00001f),
                Actions.fadeIn(1f),
                Actions.fadeIn(1f),
                Actions.fadeOut(1f)
            )
        )
        stage.addActor(label)
    }

    private fun createAuthorLabel() {
        val label = Label(/*MyGdxGame.i18NBundle.get("game-author")*/"(c) Alexander Khadzhinov",
            MyGdxGame.gameSkin,
            "default-white"
        )
        label.setAlignment(Align.center)
        label.setWrap(true)
        label.setPosition(stage.width - label.width - 15, label.height)
        label.addAction(
            Actions.sequence(
                Actions.fadeOut(0.00001f),
                Actions.fadeIn(1f),
                Actions.fadeIn(1f),
                Actions.fadeOut(1f)
            )
        )
        stage.addActor(label)
    }

    private fun createCompanyLogo() {
        val logo = Image(MyGdxGame.gameSkin, "company")
        logo.setPosition(stage.width - logo.width - 15, 0f)
        logo.addAction(
            Actions.sequence(
                Actions.fadeOut(0.00001f),
                Actions.fadeIn(1f),
                Actions.fadeIn(1f),
                Actions.fadeOut(1f)
            )
        )
        stage.addActor(logo)
    }
}