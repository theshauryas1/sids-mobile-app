package com.nurthure.monitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ReadingEntity::class, AlertEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NurthureDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
    abstract fun alertDao(): AlertDao
}
