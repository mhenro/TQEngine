package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.mhenro.MyGdxGame

class OptionsScreen(private val game: MyGdxGame): AbstractGameScreen() {
    private val tag = OptionsScreen::class.java.simpleName

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
        val title = Label("\n${MyGdxGame.i18NBundle.get("options")}\n", MyGdxGame.gameSkin, "title")
        title.setAlignment(Align.center)
        return title
    }

    private fun createCloseButton(): Actor {
        val btnClose = Button(MyGdxGame.gameSkin, "close")
        btnClose.isTransform = true
        btnClose.scaleBy(0.5f)
        btnClose.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.playClick()
                game.screen = MainMenuScreen(game)
                return true
            }
        })
        return btnClose
    }

    private fun createContentList(): Actor {
        val list = Table()
        createSoundCheckbox(list)
        createMusicCheckbox(list)
        createLanguageSelectBox(list)

        list.row().expand().padBottom(15f)
        list.add()

        val scrollPane = ScrollPane(list)
        scrollPane.layout()

        return scrollPane
    }

    private fun createSoundCheckbox(list: Table) {
        list.row().padBottom(15f)
        val chkSound = CheckBox("  ${MyGdxGame.i18NBundle.get("sounds")}", MyGdxGame.gameSkin)
        chkSound.isChecked = MyGdxGame.gamePrefs.getBoolean("soundEnabled", true)
        list.add(chkSound).left().left().padLeft(15f)

        chkSound.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                MyGdxGame.gamePrefs.putBoolean("soundEnabled", (event.listenerActor as CheckBox).isChecked)
                MyGdxGame.gamePrefs.flush()
                game.playClick()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
    }

    private fun createMusicCheckbox(list: Table) {
        list.row().padBottom(15f)
        val chkMusic = CheckBox("  ${MyGdxGame.i18NBundle.get("music")}", MyGdxGame.gameSkin)
        chkMusic.isChecked = MyGdxGame.gamePrefs.getBoolean("musicEnabled", true)
        list.add(chkMusic).left().left().padLeft(15f)
        chkMusic.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                MyGdxGame.gamePrefs.putBoolean("musicEnabled", (event.listenerActor as CheckBox).isChecked)
                MyGdxGame.gamePrefs.flush()
                game.playMenuMusic()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
    }

    private fun createLanguageSelectBox(list: Table) {
        list.row().padBottom(15f)
        val label = Label(MyGdxGame.i18NBundle.get("language"), MyGdxGame.gameSkin, "small")
        val languageBox = SelectBox<String>(MyGdxGame.gameSkin)
        val wrapper = Table()
        /*MyGdxGame.questEngine.getSupportedLanguages()*/listOf("ru").forEach { languageBox.items.add(it) }
        /*MyGdxGame.questEngine.getSupportedLanguages()*/listOf("ru").forEach { languageBox.list.items.add(it) }
//        languageBox.selectedIndex = 0
        languageBox.selected = MyGdxGame.questEngine.getLanguage()
        wrapper.row().padLeft(3f)
        wrapper.add(label)
        wrapper.add(languageBox).padLeft(10f)
        list.add(wrapper).left().left().padLeft(15f)
        languageBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val language = (actor as SelectBox<String>).selection.lastSelected
                MyGdxGame.gamePrefs.putString("selectedLanguage", language)
                MyGdxGame.questEngine.setLanguage(language)
                MyGdxGame.gamePrefs.flush()
                game.initI18NBundle()
                game.screen = OptionsScreen(game)
            }
        })
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.playClick()
            game.screen = MainMenuScreen(game)
        }
    }
}