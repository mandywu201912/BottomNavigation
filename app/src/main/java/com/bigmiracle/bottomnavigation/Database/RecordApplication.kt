package com.bigmiracle.bottomnavigation.Database

import android.app.Application

class RecordApplication: Application() {

    val database: RecordDatabase by lazy {RecordDatabase.getDatabase(this)}

}