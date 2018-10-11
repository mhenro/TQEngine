package com.mhenro

interface AdVideoEventListener {
    fun onRewardedEvent(type: String, amount: Int)
    fun onRewardedVideoAdLoadedEvent()
    fun onRewardedVideoAdClosedEvent()
}