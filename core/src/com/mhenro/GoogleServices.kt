package com.mhenro

interface GoogleServices {
    fun prepareAds()
    fun isAdVideoLoaded(): Boolean
    fun loadRewardedVideoAd()
    fun showRewardedVideoAd()
    fun setVideoEventListener(listener: AdVideoEventListener)
}