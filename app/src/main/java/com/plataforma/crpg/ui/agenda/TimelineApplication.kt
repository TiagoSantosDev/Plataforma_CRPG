package com.plataforma.crpg.ui.agenda

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class TimelineApplication : Application() {

    companion object {
        lateinit var instance: TimelineApplication
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        instance = this
    }
}
