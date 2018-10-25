package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame
import com.mhenro.engine.model.QuestChapter

class ContentsScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = ContentsScreen::class.java.simpleName

    init {
        createLayout()
    }

    private fun createLayout() {
        wrapper.row().padLeft(5f).padRight(5f)
        wrapper.add(createTitle()).expandX().fill()
        wrapper.add(createCloseButton()).align(Align.left).padRight(25f)
        wrapper.row().fill().expand().padLeft(5f).padRight(5f)
        wrapper.add(createContentList())
        wrapper.layout()
    }

    private fun createTitle(): Actor {
        val title = Label("\n${MyGdxGame.i18NBundle.get("contents")}\n", MyGdxGame.gameSkin, "title")
        title.setAlignment(Align.center)
        return title
    }

    private fun createCloseButton(): Actor {
        val btnClose = Button(MyGdxGame.gameSkin, "close")
        btnClose.isTransform = true
        btnClose.scaleBy(0.5f)
        btnClose.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
                game.screen = GameScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        return btnClose
    }

    private fun createContentList(): Actor {
        val list = Table()
        for (i in 0 until MyGdxGame.questEngine.getContents().size) {
            val chapter = MyGdxGame.questEngine.getContents()[i]
            if (i == 0 || MyGdxGame.questEngine.getCompletedChapters().contains(i)) {
                createListElement(list, chapter, i == 0)
            }
        }

        list.row().expand().padBottom(15f)
        list.add()

        val scrollPane = ScrollPane(list)
        scrollPane.layout()

        return scrollPane
    }

    private fun createListElement(list: Table, chapter: QuestChapter, restart: Boolean = false) {
        list.row().padBottom(15f)
        val btnBookmark = TextButton(if (restart) MyGdxGame.i18NBundle.get("restart") else chapter.name.locale[MyGdxGame.questEngine.getLanguage()], MyGdxGame.gameSkin, "no-border")
        list.add(btnBookmark).left().padLeft(15f)
        list.add().expandX()
        list.add(Image(MyGdxGame.gameSkin, if (restart) "restart" else "bookmark")).center().padRight(15f)
        list.row().padBottom(15f)

        btnBookmark.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                Gdx.app.log(tag, "Goto node #${chapter.id}")

                if (restart) {
                    MyGdxGame.questEngine.restartGame()
                    game.screen = GameScreen(game)
                } else {
                    MyGdxGame.questEngine.rewindToChapter(chapter.startFromNode)
                    game.screen = GameScreen(game)
                }

                return true
            }
        })
    }
}