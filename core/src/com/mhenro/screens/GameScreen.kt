package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.mhenro.MyGdxGame
import com.mhenro.engine.model.QuestChoice
import com.mhenro.engine.model.QuestGameNode

class GameScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = GameScreen::class.java.simpleName
    private lateinit var contentList: ScrollPane

    init {
        createLayout()
        addNextMessage(MyGdxGame.questEngine.getStartNode())
    }

    private fun createLayout() {
        wrapper.row().padTop(10f).padBottom(5f)
        wrapper.add(createContentsButton()).expandX().left().padLeft(15f)
        wrapper.add(createSettingsButton()).right().padRight(15f)
        wrapper.add(createMainMenuButton()).right().padRight(15f)
        wrapper.row().padBottom(5f).padTop(5f)
        wrapper.add(createContentList()).fill().expand().colspan(3)
        wrapper.layout()
    }

    private fun createContentsButton(): Actor {
        val btnContents = ImageButton(MyGdxGame.gameSkin.getDrawable("contents"))
        btnContents.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = ContentsScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnContents
    }

    private fun createSettingsButton(): Actor {
        val btnSettings = ImageButton(MyGdxGame.gameSkin.getDrawable("settings"))
        btnSettings.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
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
        btnMainMenu.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
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

    private fun createMessage(msg: String, info: Boolean) {
        val button = TextButton(msg,
                MyGdxGame.gameSkin, if (info) "info-message" else "simple-message")
        button.label.setWrap(true)
        button.label.setAlignment(Align.left, Align.left)
        button.labelCell.padLeft(10f).padRight(10f)
        (contentList.actor as Table).add(button).fill().expandX().padLeft(15f).padRight(15f)
        (contentList.actor as Table).layout()
        (contentList.actor as Table).row().padBottom(5f)
    }

    private fun createAnswer(choices: List<QuestChoice>) {
        val choicePanel = Table()
        choices.forEach {
            val btnChoice = TextButton(it.text, MyGdxGame.gameSkin, "choice")
            btnChoice.label.setWrap(true)
            btnChoice.labelCell.padTop(5f).padBottom(5f)
            choicePanel.add(btnChoice).fill().expandX()

            btnChoice.addListener(object : InputListener() {
                override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                    game.playClick()

                    Timer.schedule(object : Timer.Task() {
                        override fun run() {
                            addNextMessage(MyGdxGame.questEngine.getNodeById(it.nextNode))
                        }
                    }, 10f/1000)
                }

                override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                    return true
                }
            })
        }
        (contentList.actor as Table).add(choicePanel).fill().expandX().padLeft(15f).padRight(15f).padBottom(5f)
        (contentList.actor as Table).layout()
        (contentList.actor as Table).row()

//        (contentList.actor as Table).row().expand()
//        (contentList.actor as Table).add()
//        (contentList.actor as Table).row()
    }

    private fun addNextMessage(node: QuestGameNode) {
        when (node.type) {
            0 -> {
                val message = node.additionalParams.message!!
                val duration = node.additionalParams.duration!!
                val info = node.additionalParams.infoMessage!!

                createMessage("\n$message\n", info)

                Timer.schedule(object : Timer.Task() {
                    override fun run() {
                        addNextMessage(MyGdxGame.questEngine.getNodeById(node.nextNode!!))
                    }
                }, duration.toFloat() / 1000)
            }
            1 -> {
                val choices = node.additionalParams.choices!!
                createAnswer(choices)
            }
        }

//        (contentList.actor as Table).row().expand()
//        (contentList.actor as Table).add()
//        (contentList.actor as Table).row()
        contentList.layout()
        contentList.scrollTo(0f, 0f, 0f, 0f)
    }
}
