package com.bigmiracle.bottomnavigation.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bigmiracle.bottomnavigation.Database.Record.RecordDao
import com.bigmiracle.bottomnavigation.Database.Record.RecordEntity

@Database(entities = arrayOf(RecordEntity::class), version = 1)
abstract class RecordDatabase: RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: RecordDatabase? = null

        fun getDatabase(context: Context): RecordDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    RecordDatabase::class.java,
                    "record_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}