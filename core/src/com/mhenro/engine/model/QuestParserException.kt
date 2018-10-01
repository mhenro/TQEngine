package com.mhenro.engine.model

import java.lang.RuntimeException

class QuestParserException(private val msg: String): RuntimeException(msg)