package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.mhenro.MyGdxGame

class MainMenuScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = OptionsScreen::class.java.simpleName

    init {
        createLayout()
    }

    private fun createLayout() {
        wrapper.row().padTop(5f).padLeft(5f).padRight(5f)
        wrapper.add(createStartButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createLikeButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createQuestMarketButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createCreditsButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createSocialVKButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createSocialFacebookButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f).padBottom(5f)
        wrapper.layout()
    }

    private fun createStartButton(): Actor {
        val btnStartGame = ImageTextButton("NEW GAME", MyGdxGame.gameSkin)
        btnStartGame.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = GameScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnStartGame
    }

    private fun createLikeButton(): Actor {
        val btnLike = ImageTextButton("THUMBS UP", MyGdxGame.gameSkin)
        return btnLike
    }

    private fun createQuestMarketButton(): Actor {
        val btnQuestMarket = ImageTextButton("OTHER QUESTS", MyGdxGame.gameSkin)
        return btnQuestMarket
    }

    private fun createCreditsButton(): Actor {
        val btnCredits = ImageTextButton("CREDITS", MyGdxGame.gameSkin)
        return btnCredits
    }

    private fun createSocialVKButton(): Actor {
        val btnSocialVK = ImageTextButton("FOLLOW US ON\nVK.COM", MyGdxGame.gameSkin)
        return btnSocialVK
    }

    private fun createSocialFacebookButton(): Actor {
        val btnSocialFacebook = ImageTextButton("FOLLOW US ON\nFACEBOOK.COM", MyGdxGame.gameSkin)
        return btnSocialFacebook
    }
}