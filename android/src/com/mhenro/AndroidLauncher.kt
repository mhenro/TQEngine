package com.mhenro

import android.os.Bundle
import android.os.StrictMode
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
    private var adRewardedVideoView: RewardedVideoAd? = null
    private var adVideoListener: AdVideoEventListener? = null
    private var isAdVideoLoaded = false

    companion object {
        const val ALARM_TYPE_ELAPSE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = AndroidApplicationConfiguration()
        val game = MyGdxGame(this)
        val notificationHandler = AndroidNotificationImpl(this)
        game.notificationHandler = notificationHandler
        val networkManager = NetworkManagerImpl(this)
        game.networkManager = networkManager
        config.useAccelerometer = false
        config.useCompass = false

        val gameView = initializeForView(game, config)
        val layout = RelativeLayout(this)
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(layout)
//        enableStrictMode()
    }

    private fun enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder().detectAll()
                            .penaltyLog().penaltyDeath().build())
            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder().detectAll()
                            .penaltyLog().penaltyDeath().build()
            )
        }
    }

    override fun loadRewardedVideoAd() {
        isAdVideoLoaded = false
        adRewardedVideoView?.loadAd(getString(R.string.ad_unit_id), AdRequest.Builder().build())
    }

    override fun prepareAds() {
        /* initialize Google ads */
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        setupRewarded()
    }

    private fun setupRewarded() {
        adRewardedVideoView = MobileAds.getRewardedVideoAdInstance(this)
        runOnUiThread {
            adRewardedVideoView?.rewardedVideoAdListener = this
            loadRewardedVideoAd()
        }
    }

    override fun setVideoEventListener(listener: AdVideoEventListener) {
        this.adVideoListener = listener
    }

    override fun onRewarded(item: RewardItem) {
        isAdVideoLoaded = false
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
            if (adRewardedVideoView?.isLoaded == false) {
                loadRewardedVideoAd()
            }
        }
        return false
    }

    override fun showRewardedVideoAd() {
        runOnUiThread {
            if (adRewardedVideoView?.isLoaded == true) {
                adRewardedVideoView?.show()
            } else {
                loadRewardedVideoAd()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        adRewardedVideoView?.pause(this)
    }

    override fun onResume() {
        super.onResume()
        adRewardedVideoView?.resume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        adRewardedVideoView?.destroy(this)
    }
}