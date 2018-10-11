package com.mhenro

interface GoogleServices {
    fun isAdVideoLoaded(): Boolean
    fun loadRewardedVideoAd()
    fun showRewardedVideoAd()
    fun setVideoEventListener(listener: AdVideoEventListener)
}