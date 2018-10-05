package com.mhenro.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.mhenro.MyGdxGame
import com.mhenro.engine.model.QuestGameNode
import org.joda.time.DateTime

class GameScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = GameScreen::class.java.simpleName
    private lateinit var contentList: ScrollPane

    init {
        createLayout()
        if (MyGdxGame.questEngine.isHistoryAvailable()) {
            for (i in 0 until MyGdxGame.questEngine.getHistory().size - 1) {
                val nodeId = MyGdxGame.questEngine.getHistory()[i]
                val gameNode = MyGdxGame.questEngine.getNodeById(nodeId)
                addNextMessage(gameNode, true)
            }
        }
        addNextMessage(MyGdxGame.questEngine.getCurrentNode())
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
                game.screen = ContentsScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        btnContents.addListener(TextTooltip("Contents", MyGdxGame.gameSkin))
        return btnContents
    }

    private fun createInventoryButton(): Actor {
        val btnInventory = ImageButton(MyGdxGame.gameSkin.getDrawable("inventory"))
//        btnInventory.isTransform = true
//        btnInventory.scaleBy(0.2f)
        btnInventory.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = InventoryScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        btnInventory.addListener(TextTooltip("Inventory", MyGdxGame.gameSkin))
        return btnInventory
    }

    private fun createSettingsButton(): Actor {
        val btnSettings = ImageButton(MyGdxGame.gameSkin.getDrawable("settings"))
        btnSettings.isTransform = true
        btnSettings.scaleBy(0.3f)
        btnSettings.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = OptionsScreen(game, GameScreen::class.java)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        btnSettings.addListener(TextTooltip("Settings", MyGdxGame.gameSkin))
        return btnSettings
    }

    private fun createMainMenuButton(): Actor {
        val btnMainMenu = ImageButton(MyGdxGame.gameSkin.getDrawable("globe"))
        btnMainMenu.isTransform = true
        btnMainMenu.scaleBy(0.3f)
        btnMainMenu.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = MainMenuScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        btnMainMenu.addListener(TextTooltip("Main menu", MyGdxGame.gameSkin))
        return btnMainMenu
    }

    private fun createContentList(): Actor {
        val table = Table()
        contentList = ScrollPane(table)
        contentList.setScrollingDisabled(true, false)
        return contentList
    }

    private fun createMessage(msg: String, info: Boolean, duration: Int) {
        val button = TextButton(msg,
                MyGdxGame.gameSkin, if (info) "info-message" else "simple-message")
        button.label.setWrap(true)
        button.label.setAlignment(Align.left, Align.left)
        button.labelCell.padLeft(10f).padRight(10f)
        (contentList.actor as Table).add(button).fill().expandX().padLeft(15f).padRight(15f)
        (contentList.actor as Table).row().padBottom(5f)

        if (info) {
            game.notificationHandler.showNotification(MyGdxGame.questEngine.getQuestName(), MyGdxGame.i18NBundle.get("helpme"), DateTime.now().plusMillis(duration))
        }
    }

    private fun createImage(imgLocation: String) {
        val image = Image(MyGdxGame.gameSkin, imgLocation)
        (contentList.actor as Table).add(image).center().padLeft(15f).padRight(15f).padTop(30f).padBottom(30f)
        (contentList.actor as Table).row().padBottom(5f)
    }

    private fun createAnswer(node: QuestGameNode, history: Boolean = false) {
        val choicePanel = Table()
        node.additionalParams.choices!!.forEach {
            val btnChoice = TextButton(it.text.locale[MyGdxGame.questEngine.getLanguage()], MyGdxGame.gameSkin, "choice")
            btnChoice.label.setWrap(true)
            btnChoice.labelCell.padTop(5f).padBottom(5f)
            choicePanel.add(btnChoice).fill().expandX()

            if (!history) {
                btnChoice.addListener(object : InputListener() {
                    override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                        if (MyGdxGame.questEngine.getCurrentNode().id != node.id) {
                            return
                        }
                        game.playClick()
                        btnChoice.isDisabled = true

                        Timer.schedule(object : Timer.Task() {
                            override fun run() {
                                addNextMessage(MyGdxGame.questEngine.getNodeById(it.nextNode))
                            }
                        }, 10f / 1000)
                    }

                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        return !btnChoice.isDisabled
                    }
                })
            }
        }
        (contentList.actor as Table).add(choicePanel).fill().expandX().padLeft(15f).padRight(15f).padBottom(5f)
        (contentList.actor as Table).row()

//        (contentList.actor as Table).row().expand()
//        (contentList.actor as Table).add()
//        (contentList.actor as Table).row()
    }

    private fun addNextMessage(node: QuestGameNode, history: Boolean = false) {
        if (!history && node.id != MyGdxGame.questEngine.getPrevNode()) {
            MyGdxGame.questEngine.setCurrentNode(node.id)
            MyGdxGame.questEngine.addToHistory(node.id)
            MyGdxGame.questEngine.addToInventory(node.newInventory)
        }
        if (node.endNode) {
            game.screen = GameOverScreen(game, node)
            return
        }
        when (node.type) {
            0 -> {
                val message = node.additionalParams.message!!.locale[MyGdxGame.questEngine.getLanguage()]
                val duration = node.additionalParams.duration!!
                val info = node.additionalParams.infoMessage!!

                createMessage("\n$message\n", info, duration)

                if (!history) {
                    Timer.schedule(object : Timer.Task() {
                        override fun run() {
                            addNextMessage(MyGdxGame.questEngine.getNodeById(node.nextNode!!))
                        }
                    }, duration.toFloat() / 1000)
                }
            }
            1 -> {
                createAnswer(node, history)
            }
            2 -> {
                val location = node.additionalParams.location!!
                val duration = node.additionalParams.duration!!

                createImage(location)
                if (!history) {
                    Timer.schedule(object : Timer.Task() {
                        override fun run() {
                            addNextMessage(MyGdxGame.questEngine.getNodeById(node.nextNode!!))
                        }
                    }, duration.toFloat() / 1000)
                }
            }
        }

//        (contentList.actor as Table).row().expand()
//        (contentList.actor as Table).add()
//        (contentList.actor as Table).row()
        contentList.layout()
        contentList.scrollTo(0f, 0f, 0f, 0f)
    }
}
