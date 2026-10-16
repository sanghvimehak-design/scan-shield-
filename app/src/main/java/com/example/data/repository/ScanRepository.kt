package com.example.data.repository

import com.example.data.local.ScanDao
import com.example.data.model.ScanRecord
import kotlinx.coroutines.flow.Flow

class ScanRepository(private val scanDao: ScanDao) {
    val allScans: Flow<List<ScanRecord>> = scanDao.getAllScans()
    val highRiskScans: Flow<List<ScanRecord>> = scanDao.getHighRiskScans()
    val totalScansCount: Flow<Int> = scanDao.getScanCount()
    val totalThreatsCount: Flow<Int> = scanDao.getThreatCount()

    suspend fun saveScan(record: ScanRecord): Long {
        return scanDao.insertScan(record)
    }

    suspend fun deleteScan(id: Long) {
        scanDao.deleteScanById(id)
    }

    suspend fun clearHistory() {
        scanDao.clearAll()
    }
}
