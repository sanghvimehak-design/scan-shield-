package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ScanRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecord>>

    @Query("SELECT * FROM scan_records WHERE riskLevel = 'HIGH_RISK' ORDER BY timestamp DESC")
    fun getHighRiskScans(): Flow<List<ScanRecord>>

    @Query("SELECT COUNT(*) FROM scan_records")
    fun getScanCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'HIGH_RISK'")
    fun getThreatCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(record: ScanRecord): Long

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM scan_records")
    suspend fun clearAll()
}
