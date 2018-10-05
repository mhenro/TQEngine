package com.mhenro.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import com.mhenro.engine.model.*

class QuestEngine private constructor(private val questData: QuestGame,
                                      private val history: MutableList<Int> = ArrayList(),
                                      private var currentNode: QuestGameNode? = null,
                                      private var currentInventory: MutableSet<Int> = HashSet(),
                                      private var selectedLanguage: String = "en") {
    companion object {
        const val ENGINE_VERSION = 1

        fun getEngine(): QuestEngine {
            val data = Json().fromJson(QuestGame::class.java, Gdx.files.internal("quest.json"))
            val engine = QuestEngine(data)
            engine.validateQuest()
            return engine
        }
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

    fun getLanguage(): String {
        return selectedLanguage
    }

    fun setLanguage(lang: String) {
        selectedLanguage = lang
    }

    fun getSupportedLanguages(): List<String> {
        return questData.supportedLanguages
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

    fun addToInventory(newItems: List<Int>) {
        currentInventory.addAll(newItems)
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
        return history.isNotEmpty()//questData.currentGame.historyNodes.isNotEmpty()
    }

    fun getHistory(): List<Int> {
        return history  //questData.currentGame.historyNodes
    }

    fun addToHistory(nodeId: Int) {
        history.add(nodeId)
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
}