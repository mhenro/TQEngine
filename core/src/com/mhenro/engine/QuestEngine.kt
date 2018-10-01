package com.mhenro.engine

import com.mhenro.engine.model.QuestGame
import com.mhenro.engine.model.QuestGameNode
import com.mhenro.engine.model.QuestParserException

class QuestEngine(private val questData: QuestGame) {
    companion object {
        const val ENGINE_VERSION = 1
    }

    fun validateQuest() {
        if (questData.engineVersion != ENGINE_VERSION) {
            throw QuestParserException("Quest has a version which is not supported by quest engine")
        }
        if (questData.contents.isEmpty()) {
            throw QuestParserException("Contents must not be empty")
        }
        val wrongNode = questData.gameNodes.find { it.startNode && it.endNode }
        wrongNode?.let { throw QuestParserException("One of the nodes has Start and End markers") }
        questData.gameNodes.find { it.startNode } ?: throw QuestParserException("Start node is not found")
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

    }

    fun getStartNode(): QuestGameNode {
        val node = questData.gameNodes.find { it.startNode }
        return node ?: throw QuestParserException("Can't find start node")
    }

    fun getNodeById(id: Int): QuestGameNode {
        val node = questData.gameNodes.find { it.id == id }
        return node ?: throw QuestParserException("Can't find node with id $id")
    }
}