package io.github.athorfeo.template

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.athorfeo.template.network.NetworkConnectionUtil
import javax.inject.Inject

@HiltAndroidApp
class TemplateApp: Application() {
    @Inject lateinit var networkConnectionUtil: NetworkConnectionUtil

    override fun onCreate() {
        super.onCreate()
        networkConnectionUtil.registerNetworkCallback(applicationContext)
    }
}
