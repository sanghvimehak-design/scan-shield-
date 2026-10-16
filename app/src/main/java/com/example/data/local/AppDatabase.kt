package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.RiskLevel
import com.example.data.model.ScanRecord
import com.example.data.model.TrustedContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ScanRecord::class, TrustedContact::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "scan_shield_db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.scanDao(), database.contactDao())
                    }
                }
            }

            private suspend fun populateInitialData(scanDao: ScanDao, contactDao: ContactDao) {
                // Seed 2 default trusted contacts for initial demo
                contactDao.insertContact(
                    TrustedContact(
                        name = "Ramesh Kumar (Son)",
                        relationship = "Son",
                        phoneNumber = "+91 98765 43210",
                        isPrimary = true,
                        isVerified = true
                    )
                )
                contactDao.insertContact(
                    TrustedContact(
                        name = "Priya Sharma (Daughter)",
                        relationship = "Daughter",
                        phoneNumber = "+91 91234 56789",
                        isPrimary = false,
                        isVerified = true
                    )
                )

                // Seed initial sample scan records
                scanDao.insertScan(
                    ScanRecord(
                        timestamp = System.currentTimeMillis() - 86400000L * 2,
                        sourceType = "MESSAGE",
                        title = "Fake EPFO Pension Arrears SMS",
                        inputText = "Dear Pensioner, 8th Pay arrears of Rs 48,250 pending. Click http://epfo-claim-pension.xyz",
                        riskLevel = RiskLevel.HIGH_RISK,
                        confidenceScore = 96,
                        summaryExplanation = "Fraudulent EPFO impersonation asking to click non-governmental (.xyz) link.",
                        evidenceList = "Unofficial .xyz domain|Panic deadline|EPFO never pays via SMS links",
                        recommendedActions = "Do not click link|Block sender|Check on unifiedportal-mem.epfindia.gov.in",
                        safeVerificationGuide = "Use the official UMANG mobile app to verify passbook entries safely."
                    )
                )
                scanDao.insertScan(
                    ScanRecord(
                        timestamp = System.currentTimeMillis() - 86400000L * 5,
                        sourceType = "MESSAGE",
                        title = "Genuine SBI Pension Credit",
                        inputText = "Your A/C ending 4012 is credited with Rs 28,450.00 on 30-Sep-24 by Pension Disbursement EPFO.",
                        riskLevel = RiskLevel.NO_OBVIOUS_WARNING,
                        confidenceScore = 94,
                        summaryExplanation = "Legitimate monthly pension credit alert with official bank sender header.",
                        evidenceList = "Official VK-SBIINB sender ID|Standard bank credit template|No external links",
                        recommendedActions = "No action needed|Cross-check in official YONO SBI app",
                        safeVerificationGuide = "View updated statement through your physical passbook or official net banking."
                    )
                )
            }
        }
    }
}
