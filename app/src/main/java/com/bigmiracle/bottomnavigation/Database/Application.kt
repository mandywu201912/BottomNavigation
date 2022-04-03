package com.bigmiracle.bottomnavigation.Database

import android.app.Application

class Application: Application() {

    val database: AppDatabase by lazy {AppDatabase.getDatabase(this)}

}