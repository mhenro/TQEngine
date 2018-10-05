package com.mhenro.engine.model

data class QuestGame(
        var engineVersion: Int = 1,
        var gameName: QuestText = QuestText(),
        var author: String = "",
        var description: QuestText = QuestText(),
        var supportedLanguages: List<String> = emptyList(),
        var contents: List<QuestChapter> = emptyList(),
        var gameNodes: List<QuestGameNode> = emptyList(),
        var inventory: List<QuestInventoryItem> = emptyList()
)