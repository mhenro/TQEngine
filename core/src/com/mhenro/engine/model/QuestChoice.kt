package com.mhenro.engine.model

data class QuestChoice(
    var text: QuestText = QuestText(),
    var dependsOn: List<Int> = emptyList(),
    var nextNode: Int = 0
)