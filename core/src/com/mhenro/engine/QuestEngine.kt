package com.mhenro.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Timer
import com.mhenro.MyGdxGame
import com.mhenro.engine.model.*
import com.mhenro.screens.GameOverScreen
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class QuestEngine private constructor(private val questData: QuestGame,
                                      private val game: MyGdxGame,
                                      private var savedCompletedTime: DateTime?,
                                      private val history: MutableList<Int> = ArrayList(),
                                      private var currentNode: QuestGameNode? = null,
                                      private var currentInventory: MutableSet<Int> = HashSet(),
                                      private var selectedLanguage: String = "en",
                                      private val gameTimer: Timer = Timer(),
                                      private var completedTime: DateTime = DateTime.now(),
                                      private var contentList: ScrollPane = ScrollPane(null)) {
    companion object {
        const val DEBUG_MODE = false
        const val ENGINE_VERSION = 1
        const val GAME_TIMER_DELAY = 3f
        const val GAME_TIMER_REPEAT_INTERVAL = 1f

        fun getEngine(game: MyGdxGame, completedTime: DateTime?): QuestEngine {
            val data = Json().fromJson(QuestGame::class.java, Gdx.files.internal("quest.json"))
            val engine = QuestEngine(data, game, completedTime)
            engine.validateQuest()
            return engine
        }
    }

    init {
        gameTimer.scheduleTask(object : Timer.Task() {
            override fun run() {
                val cellCount = (contentList.actor as Table).cells.count()
                savedCompletedTime?.let {
                    if (cellCount > 0) {
                        completedTime = it
                        savedCompletedTime = null
                    }
                }
                if (completedTime > DateTime.now() && !DEBUG_MODE) {
                    return
                }
                if (isHistoryAvailable() && cellCount == 0) {
                    setCurrentNode(getHistory().last())
                    for (i in 0 until getHistory().size - 1) {
                        val nodeId = getHistory()[i]
                        val gameNode = getNodeById(nodeId)
                        addNextMessage(gameNode, true)
                    }
                    val tmpHistory = history.dropLast(1)
                    history.clear()
                    history.addAll(tmpHistory)
                }
                if (getHistory().isEmpty() || getCurrentNode().id != getHistory().last()) {
                    val node = getCurrentNode()
                    addNextMessage(node)
                }
            }
        }, GAME_TIMER_DELAY, GAME_TIMER_REPEAT_INTERVAL)
        stopQuest()
    }

    private fun validateQuest() {
        if (questData.engineVersion != ENGINE_VERSION) {
            throw QuestParserException("Quest has a version which is not supported by quest engine")
        }
        if (questData.contents.isEmpty()) {
            throw QuestParserException("Contents must not be empty")
        }
        val wrongNode = questData.gameNodes.find { it.startNode && it.endNode }
        wrongNode?.let { throw QuestParserException("One of the nodes has Start and End markers") }
        questData.gameNodes.find { it.startNode } ?: throw QuestParserException("Start node is not found")
        if (questData.gameNodes.filter { it.startNode }.count() > 1) {
            throw QuestParserException("Quest must has only one Start node")
        }
        questData.gameNodes.filter { it.endNode }.forEach {
            if (it.type != 0) {
                throw QuestParserException("All End nodes must has type 0 (simple message)")
            }
        }
        questData.gameNodes.forEach {
            val type = it.type
            val params = it.additionalParams
            when (type) {
                0 -> {
                    if (params.message == null || params.infoMessage == null || params.duration == null) {
                        throw QuestParserException("Node with type 0 (simple message) must has message, infoMessage and duration properties")
                    }
                    if (it.nextNode == null) {
                        throw QuestParserException("Node with type 0 (simple message) must has a nextNode property")
                    }
                    params.notification?.let {notification ->
                        questData.supportedLanguages.forEach {
                            if (notification.locale[it] == null || notification.locale[it]!!.isBlank()) {
                                throw QuestParserException("If notification property is presented it should have not empty text for all supported languages")
                            }
                        }
                    }
                }
                1 -> {
                    if (params.choices == null || params.choices?.isEmpty()!!) {
                        throw QuestParserException("Node with type 1 (choice) must has choices property which must not be empty")
                    }
                }
                2 -> {
                    if (params.url == null || params.location == null || params.location?.isEmpty()!! || params.duration == null) {
                        throw QuestParserException("Node with type 2 (image) must has url, location and duration properties. Also location property must not be empty")
                    }
                    if (it.nextNode == null) {
                        throw QuestParserException("Node with type 2 (image) must has a nextNode property")
                    }
                }
            }
        }

        /* validating inventory */
        val wrongInventory = questData.inventory.find { it.name.locale[selectedLanguage]!!.isBlank() }
        if (wrongInventory != null) {
            throw QuestParserException("Each inventory must has a name property")
        }
        //TODO: validate inventory ids from CurrentGame object
    }

    private fun createMessage(node: QuestGameNode, history: Boolean = false) {
        val msg = node.additionalParams.message!!.locale[getLanguage()]
        val info = node.additionalParams.infoMessage!!
        val duration = node.additionalParams.duration!!
        val notification = node.additionalParams.notification
        val rewindIsAllowed = node.additionalParams.rewindIsAllowed

        val button = TextButton("\n$msg\n",
                MyGdxGame.gameSkin, if (info) "info-message" else "simple-message")
        button.label.setWrap(true)
        button.label.setAlignment(Align.left, Align.left)
        button.labelCell.padLeft(10f).padRight(10f)
        (contentList.actor as Table).add(button).fill().expandX().padLeft(15f).padRight(15f)
        (contentList.actor as Table).row().padBottom(5f)

        rewindIsAllowed?.let {
            if (!history) {
                val rewindButton = TextButton(MyGdxGame.i18NBundle.get("rewind"), MyGdxGame.gameSkin, "rewind")
                rewindButton.label.setWrap(true)
                rewindButton.label.setAlignment(Align.left, Align.left)
                rewindButton.labelCell.padLeft(10f).padRight(10f)
                (contentList.actor as Table).add(rewindButton).fill().expandX().padLeft(15f).padRight(15f)
                (contentList.actor as Table).row().padBottom(5f)
                rewindButton.addListener(object : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        if (game.googleServices.isAdVideoLoaded()) {
                            game.googleServices.showRewardedVideoAd()
                        }
                        return true
                    }
                })
            }
        }

        notification?.let {
            val text = it.locale[getLanguage()]!!
            game.notificationHandler.showNotification(getQuestName(), text, DateTime.now().plusMillis(duration))
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
            val btnChoice = TextButton("\n${it.text.locale[getLanguage()]}\n", MyGdxGame.gameSkin, "choice")
            btnChoice.label.setWrap(true)
            btnChoice.labelCell.padTop(5f).padBottom(5f)
            choicePanel.add(btnChoice).fill().expandX()

            if (!getPlayerInventoryItemIds().containsAll(it.dependsOn)) {
                btnChoice.isDisabled = true
                val missedInventory: MutableSet<Int> = HashSet()
                missedInventory.addAll(it.dependsOn)
                missedInventory.removeAll(getPlayerInventoryItemIds())
                val missedInventoryStr = missedInventory.map { getInventoryById(it).name.locale[getLanguage()] }.joinToString(",")
                btnChoice.setText("${btnChoice.text}\n${MyGdxGame.i18NBundle.get("needinventory")}\n[${missedInventoryStr}]")
            }

            if (!history) {
                btnChoice.addListener(object : InputListener() {
                    override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                        if (getCurrentNode().id != node.id) {
                            return
                        }
                        game.playClick()
                        btnChoice.isDisabled = true

                        setCurrentNode(it.nextNode)
                        completedTime = DateTime.now().plusMillis(node.additionalParams.duration ?: 100)
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
        if (!history && node.id != getPrevNode()) {
            addToHistory(node.id)
            addToInventory(node.newInventory.toSet())
            removeFromInventory(node.removeInventory.toSet())
        }
        if (node.endNode) {
            stopQuest()
            game.screen = GameOverScreen(game, node)
            restartGame()
            return
        }
        when (node.type) {
            0 -> {
                val duration = node.additionalParams.duration!!
                createMessage(node, history)

                if (!history) {
                    setCurrentNode(node.nextNode!!)
                    completedTime = DateTime.now().plusMillis(duration)
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
                    setCurrentNode(node.nextNode!!)
                    completedTime = DateTime.now().plusMillis(duration)
                }
            }
        }

//        (contentList.actor as Table).row().expand()
//        (contentList.actor as Table).add()
//        (contentList.actor as Table).row()
        (contentList.actor as Table).layout()
        contentList.layout()
        contentList.scrollTo(0f, 0f, 0f, 0f)
    }

    fun getLanguage(): String {
        return selectedLanguage
    }

    fun setLanguage(lang: String) {
        selectedLanguage = lang
    }

    fun getSupportedLanguages(): Set<String> {
        return questData.supportedLanguages.toSet()
    }

    fun getQuestName(): String {
        return questData.gameName.locale[getLanguage()]!!
    }

    fun getStartNode(): QuestGameNode {
        val node = questData.gameNodes.find { it.startNode }
        return node ?: throw QuestParserException("Can't find start node")
    }

    fun getNodeById(id: Int): QuestGameNode {
        val node = questData.gameNodes.find { it.id == id }
        return node ?: throw QuestParserException("Can't find node with id $id")
    }

    fun getInventoryById(id: Int): QuestInventoryItem {
        val item = questData.inventory.find { it.id == id }
        return item ?: throw QuestParserException("Can't find inventory item with id $id")
    }

    fun getContents(): List<QuestChapter> {
        return questData.contents
    }

    fun getPlayerInventoryItemIds(): Set<Int> {
        return currentInventory
    }

    fun addToInventory(newItems: Set<Int>) {
        currentInventory.addAll(newItems)
    }

    fun removeFromInventory(items: Set<Int>) {
        currentInventory.removeAll(items)
    }

    fun clearInventory() {
        currentInventory.clear()
    }

    fun setCurrentNode(id: Int) {
        val node = getNodeById(id)
        currentNode = node
    }

    fun getCurrentNode(): QuestGameNode {
        return currentNode ?: getStartNode()
    }

    fun isHistoryAvailable(): Boolean {
        return history.isNotEmpty()
    }

    fun getHistory(): List<Int> {
        return history
    }

    fun addToHistory(nodeId: Int) {
        history.add(nodeId)
    }

    fun addToHistory(nodeIds: List<Int>) {
        history.addAll(nodeIds)
    }

    fun getPrevNode(): Int? {
        if (history.isNotEmpty()) {
            return history.last()
        }
        return null
    }

    fun clearHistory() {
        history.clear()
    }

    fun restartGame() {
        clearHistory()
        clearInventory()
        setCurrentNode(getStartNode().id)
    }

    fun startQuest(scrollPane: ScrollPane) {
        contentList = scrollPane
        (contentList.actor as Table).clearChildren()
        gameTimer.start()
        completedTime = DateTime.now().plusSeconds(1)
    }

    fun stopQuest() {
        gameTimer.stop()
    }

    fun getCompletedTime(): String {
        val formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
        return completedTime.toString(formatter)
    }

    fun skipWaiting() {
        completedTime = DateTime.now()
    }
}