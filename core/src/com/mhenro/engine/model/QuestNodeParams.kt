package com.mhenro.engine.model

data class QuestNodeParams(
        var message: String? = null,
        var infoMessage: Boolean? = null,
        var duration: Int? = null,
        var choices: List<QuestChoice>? = null,
        var url: Boolean? = null,
        var location: String? = null
)