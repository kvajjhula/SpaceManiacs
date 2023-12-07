package edu.uw.ischool.avajjh.spacemaniacs

import android.app.Application
import kotlinx.coroutines.runBlocking

class RepositoryApplication : Application() {
    lateinit var repository: DataRepository

    override fun onCreate() {
        super.onCreate()

    }
}