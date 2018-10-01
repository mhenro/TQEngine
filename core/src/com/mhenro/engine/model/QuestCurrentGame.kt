package com.mhenro.engine.model

data class QuestCurrentGame(
        var currentInventory: List<QuestInventoryItem> = emptyList(),
        var currentNode: Int = 0
)