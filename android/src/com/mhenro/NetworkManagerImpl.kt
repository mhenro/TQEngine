package com.mhenro

import android.content.Context
import android.net.ConnectivityManager

class NetworkManagerImpl(private val context: Context) : NetworkManager {
    override fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}