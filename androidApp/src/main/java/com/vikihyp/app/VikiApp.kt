package com.vikihyp.app

import android.app.Application
import com.vikihyp.shared.tool.setApplication

class VikiApp: Application() {
    override fun onCreate() {
        super.onCreate()

        setApplication(this)
    }
}