package com.nurthure.monitor.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "readings")
data class ReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val respirationValue: Float?,
    val bodyTempValue: Float?,
    val co2Value: Int?,
    val vocValue: Float?,
    val postureState: String?,
    val audioState: String?,
    val radarActive: Boolean
)

@Dao
interface ReadingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: ReadingEntity)
    
    @Query("SELECT * FROM readings WHERE timestamp >= :since ORDER BY timestamp DESC")
    suspend fun getReadingsLastHours(since: Long): List<ReadingEntity>
    
    @Query("SELECT * FROM readings ORDER BY timestamp DESC")
    suspend fun getAllReadings(): List<ReadingEntity>
    
    @Query("SELECT * FROM readings ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestReading(): ReadingEntity?
    
    @Query("DELETE FROM readings WHERE timestamp < :before")
    suspend fun deleteOldReadings(before: Long)
}
