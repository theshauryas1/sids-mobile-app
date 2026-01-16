package com.nurthure.monitor.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val severity: String,
    val title: String,
    val description: String,
    val key: String,
    val acknowledged: Boolean = false
)

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertEntity)
    
    @Query("SELECT * FROM alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>
    
    @Query("SELECT * FROM alerts WHERE key = :key AND timestamp >= :since LIMIT 1")
    suspend fun getRecentAlert(key: String, since: Long): AlertEntity?
    
    @Query("UPDATE alerts SET acknowledged = 1 WHERE id = :id")
    suspend fun acknowledge(id: Long)
    
    @Query("DELETE FROM alerts")
    suspend fun deleteAll()
}
