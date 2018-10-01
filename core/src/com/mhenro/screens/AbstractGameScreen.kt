package com.mhenro.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.mhenro.MyGdxGame

abstract class AbstractGameScreen: Screen {
    protected val stage = Stage(ExtendViewport(480f, 800f))
    protected val wrapper = Table()

    init {
        wrapper.setFillParent(true)
        stage.addActor(wrapper)
    }

     override fun hide() {}

     override fun show() {
         Gdx.input.inputProcessor = stage
     }

     override fun render(delta: Float) {
         Gdx.gl.glClearColor(23f / 255, 23f / 255, 38f / 255, 1f)
         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
         stage.act()
         stage.draw()
     }

     override fun pause() {}

     override fun resume() {}

     override fun resize(width: Int, height: Int) {
         stage.viewport.update(width, height, true)
     }

     override fun dispose() {
         stage.dispose()
     }
 }