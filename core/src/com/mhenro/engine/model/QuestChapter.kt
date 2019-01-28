package com.mhenro.engine.model

data class QuestChapter(
    var id: Int = 0,
    var name: QuestText = QuestText(),
    var startFromNode: Int = 0
)