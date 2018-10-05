package com.mhenro.engine.model

data class QuestInventoryItem(
        var id: Int = 0,
        var name: QuestText = QuestText(),
        var description: QuestText = QuestText()
)