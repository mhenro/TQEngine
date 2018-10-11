package com.mhenro.engine.model

data class QuestNodeParams(
        var message: QuestText? = null,
        var infoMessage: Boolean? = null,
        var rewindIsAllowed: Boolean? = null,
        var notification: QuestText? = null,
        var duration: Int? = null,
        var choices: List<QuestChoice>? = null,
        var url: Boolean? = null,
        var location: String? = null
)