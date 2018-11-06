package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.mhenro.MyGdxGame
import java.lang.Exception

class MainMenuScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = MainMenuScreen::class.java.simpleName

    init {
        createLayout()
        game.playMenuMusic()
    }

    private fun createLayout() {
        wrapper.row().padTop(5f).padLeft(5f).padRight(5f)
        wrapper.add(createStartButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createOptionsButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createLikeButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        //wrapper.add(createQuestMarketButton()).fill().expand()
        //wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createCreditsButton()).fill().expand()
        wrapper.row().padLeft(5f).padRight(5f)
        //wrapper.add(createSocialFacebookButton()).fill().expand()
        //wrapper.row().padLeft(5f).padRight(5f).padBottom(5f)
        wrapper.layout()
    }

    private fun createStartButton(): Actor {
        val btnStartGame = ImageTextButton(if (MyGdxGame.questEngine.isHistoryAvailable()) MyGdxGame.i18NBundle.get("resumegame") else MyGdxGame.i18NBundle.get("newgame"), MyGdxGame.gameSkin)
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

    private fun createOptionsButton(): Actor {
        val btnOptions = ImageTextButton(MyGdxGame.i18NBundle.get("options"), MyGdxGame.gameSkin)
        btnOptions.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = OptionsScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnOptions
    }

    private fun createLikeButton(): Actor {
        val btnLike = ImageTextButton(MyGdxGame.i18NBundle.get("thumbsup"), MyGdxGame.gameSkin)
        btnLike.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                try {
                    Gdx.net.openURI("market://details?id=com.mhenro")
                } catch (e: Exception) {
                    Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.mhenro")
                }
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnLike
    }

    private fun createQuestMarketButton(): Actor {
        val btnQuestMarket = ImageTextButton(MyGdxGame.i18NBundle.get("otherquests"), MyGdxGame.gameSkin)
        return btnQuestMarket
    }

    private fun createCreditsButton(): Actor {
        val btnCredits = ImageTextButton(MyGdxGame.i18NBundle.get("credits"), MyGdxGame.gameSkin)
        btnCredits.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = CreditsScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnCredits
    }

    private fun createSocialFacebookButton(): Actor {
        val btnSocialFacebook = ImageTextButton(MyGdxGame.i18NBundle.get("followus"), MyGdxGame.gameSkin)
        btnSocialFacebook.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                if (MyGdxGame.questEngine.getLanguage() == "ru") {
                    Gdx.net.openURI("https://vk.com")
                } else {
                    Gdx.net.openURI("https://facebook.com")
                }
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnSocialFacebook
    }
}