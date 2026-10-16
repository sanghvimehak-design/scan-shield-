package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RiskLevel(val label: String) {
    HIGH_RISK("High Risk"),
    SUSPICIOUS("Suspicious"),
    NO_OBVIOUS_WARNING("No Obvious Warning Signs"),
    UNABLE_TO_VERIFY("Unable to Verify")
}

@Entity(tableName = "scan_records")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val sourceType: String, // SCREENSHOT, MESSAGE, PHONE, LINK
    val title: String,
    val inputText: String,
    val imageUri: String? = null,
    val riskLevel: RiskLevel,
    val confidenceScore: Int = 85,
    val summaryExplanation: String,
    val evidenceList: String, // pipe-separated
    val recommendedActions: String, // pipe-separated
    val safeVerificationGuide: String,
    val languageCode: String = "en"
)
