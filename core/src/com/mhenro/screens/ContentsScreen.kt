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
        wrapper.add(createCloseButton()).align(Align.left).padRight(15f)
        wrapper.row().fill().expand().padLeft(5f).padRight(5f)
        wrapper.add(createContentList())
        wrapper.layout()
    }

    private fun createTitle(): Actor {
        val title = Label("\nCONTENTS\n", MyGdxGame.gameSkin)
        title.setAlignment(Align.center)
        return title
    }

    private fun createCloseButton(): Actor {
        val btnClose = Button(MyGdxGame.gameSkin, "close")
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
        for (i in 0 until MyGdxGame.gameQuest.contents.size) {
            val chapter = MyGdxGame.gameQuest.contents[i]
            createListElement(list, chapter, i == 0)
        }

        list.row().expand().padBottom(15f)
        list.add()

        val scrollPane = ScrollPane(list)
        scrollPane.layout()

        return scrollPane
    }

    private fun createListElement(list: Table, chapter: QuestChapter, restart: Boolean = false) {
        list.row().padBottom(15f)
        val btnBookmark = TextButton(if (restart) "[ Restart game ]" else chapter.name, MyGdxGame.gameSkin, "no-border")
        list.add(btnBookmark).left().padLeft(15f)
        list.add().expandX()
        list.add(Image(MyGdxGame.gameSkin, if (restart) "restart" else "bookmark")).center().padRight(15f)
        list.row().padBottom(15f)

        btnBookmark.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.playClick()
//                game.screen = GameScreen(game)
                Gdx.app.log(tag, "Goto node #${chapter.id}")
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
    }
}