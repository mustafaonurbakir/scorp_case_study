package com.mustafaonurbakir.scorp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // We may need here in the later stages of the project.
    }

}