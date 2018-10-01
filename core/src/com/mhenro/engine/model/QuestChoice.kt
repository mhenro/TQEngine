package com.mhenro.engine.model

data class QuestChoice(
        var text: String = "",
        var dependsOn: List<QuestInventoryItem> = emptyList(),
        var nextNode: Int = 0
)