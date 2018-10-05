package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame
import com.mhenro.engine.model.QuestChapter
import com.mhenro.engine.model.QuestInventoryItem

class InventoryScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = InventoryScreen::class.java.simpleName

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
        val title = Label("\nINVENTORY\n", MyGdxGame.gameSkin, "title")
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
        MyGdxGame.questEngine.getPlayerInventoryItemIds().forEach {
            val item = MyGdxGame.questEngine.getInventoryById(it)
            createListElement(list, item)
        }

        list.row().expand().padBottom(15f)
        list.add()

        val scrollPane = ScrollPane(list)
        scrollPane.layout()

        return scrollPane
    }

    private fun createListElement(list: Table, item: QuestInventoryItem) {
        list.row().padBottom(15f)
        val btnItem = TextButton(item.name.locale[MyGdxGame.questEngine.getLanguage()], MyGdxGame.gameSkin, "no-border")
        list.add(btnItem).left().padLeft(15f)
        list.add().expandX()
        list.add(Image(MyGdxGame.gameSkin, "cup")).center().padRight(15f)
        list.row().padBottom(15f)

        btnItem.addListener(TextTooltip(item.description.locale[MyGdxGame.questEngine.getLanguage()], MyGdxGame.gameSkin))
    }
}