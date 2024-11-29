package com.otaku.fetch2

import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import koin.apiModule
import koin.dbModule
import koin.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FetchApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        startKoin {
            androidContext(this@FetchApp)
            if (BuildConfig.DEBUG) androidLogger()
            modules(networkModule, apiModule, dbModule)
        }
    }

    companion object {
        lateinit var appInstance: FetchApp
    }
}