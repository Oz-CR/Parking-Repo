package com.example.parking

import android.app.Application

class App {
    class App : Application() {
        companion object {
            lateinit var instance: App
                private set
        }

        override fun onCreate() {
            super.onCreate()
            instance = this
        }
    }
}