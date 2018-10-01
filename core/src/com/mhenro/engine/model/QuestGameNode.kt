package com.mhenro.engine.model

data class QuestGameNode(
        var id: Int = 0,
        var type: Int = 0,
        var soundTheme: Int = 0,
        var startNode: Boolean = false,
        var endNode: Boolean = false,
        var nextNode: Int? = null,
        var additionalParams: QuestNodeParams = QuestNodeParams()
)