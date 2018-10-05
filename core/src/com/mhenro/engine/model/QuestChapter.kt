package com.mhenro.engine.model

data class QuestChapter(
        var id: Int = 0,
        var name: QuestText = QuestText(),
        var completed: Boolean = false
)