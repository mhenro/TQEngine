package com.mhenro

import android.os.Bundle
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class AndroidLauncher : AndroidApplication(), GoogleServices, RewardedVideoAdListener {
    private lateinit var adRewardedVideoView: RewardedVideoAd
    private var adVideoListener: AdVideoEventListener? = null
    private var isAdVideoLoaded = false

    companion object {
        const val ALARM_TYPE_ELAPSE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* initialize Google ads */
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        setupRewarded()

        val config = AndroidApplicationConfiguration()
        val game = MyGdxGame(this)
        val notificationHandler = AndroidNotificationImpl(this)
        game.notificationHandler = notificationHandler
        config.useAccelerometer = false
        config.useCompass = false

        val gameView = initializeForView(game, config)
        val layout = RelativeLayout(this)
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(layout)
    }

    override fun loadRewardedVideoAd() {
        adRewardedVideoView.loadAd(getString(R.string.ad_unit_id_test), AdRequest.Builder().build())
    }

    private fun setupRewarded() {
        adRewardedVideoView = MobileAds.getRewardedVideoAdInstance(this)
        adRewardedVideoView.rewardedVideoAdListener = this
        loadRewardedVideoAd()
    }

    override fun setVideoEventListener(listener: AdVideoEventListener) {
        this.adVideoListener = listener
    }

    override fun onRewarded(item: RewardItem) {
        adVideoListener?.onRewardedEvent(item.type, item.amount)
    }

    override fun onRewardedVideoAdClosed() {
        isAdVideoLoaded = false
        loadRewardedVideoAd()
        adVideoListener?.onRewardedVideoAdClosedEvent()
    }

    override fun onRewardedVideoAdLoaded() {
        isAdVideoLoaded = true
        adVideoListener?.onRewardedVideoAdLoadedEvent()
    }

    override fun onRewardedVideoAdLeftApplication() {}

    override fun onRewardedVideoAdOpened() {}

    override fun onRewardedVideoCompleted() {}

    override fun onRewardedVideoStarted() {}

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {}

    override fun isAdVideoLoaded(): Boolean {
        if (isAdVideoLoaded) {
            return true
        }
        runOnUiThread {
            if (!adRewardedVideoView.isLoaded) {
                loadRewardedVideoAd()
            }
        }
        return false
    }

    override fun showRewardedVideoAd() {
        runOnUiThread {
            if (adRewardedVideoView.isLoaded) {
                adRewardedVideoView.show()
            } else {
                loadRewardedVideoAd()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        adRewardedVideoView.pause(this)
    }

    override fun onResume() {
        super.onResume()
        adRewardedVideoView.resume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        adRewardedVideoView.destroy(this)
    }
}