package com.mhenro.engine.model

data class QuestGame(
        var engineVersion: Int = 1,
        var gameName: String = "",
        var author: String = "",
        var description: String = "",
        var contents: List<QuestChapter> = emptyList(),
        var gameNodes: List<QuestGameNode> = emptyList(),
        var inventory: List<QuestInventoryItem> = emptyList(),
        var currentGame: QuestCurrentGame = QuestCurrentGame()
)