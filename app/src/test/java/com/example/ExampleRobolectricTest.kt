package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.RiskLevel
import com.example.domain.analyzer.HeuristicScamDetector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Scan Shield", appName)
    }

    @Test
    fun `heuristic detector identifies UPI PIN receive scam as HIGH_RISK`() {
        val message = "Congratulations! To receive government grant Rs 5000 enter your UPI PIN now."
        val result = HeuristicScamDetector.analyze(message)
        assertEquals(RiskLevel.HIGH_RISK, result.riskLevel)
        assertTrue(result.evidenceList.any { it.contains("UPI PIN to RECEIVE") })
    }

    @Test
    fun `heuristic detector identifies electricity disconnection panic as HIGH_RISK`() {
        val message = "Dear Consumer, your electricity will be disconnected at 9:30 PM tonight. Call officer 9876543210."
        val result = HeuristicScamDetector.analyze(message)
        assertEquals(RiskLevel.HIGH_RISK, result.riskLevel)
    }

    @Test
    fun `heuristic detector identifies official bank credit alert as NO_OBVIOUS_WARNING`() {
        val message = "Your A/C ending 4012 is credited with Rs 28,450.00 on 30-Sep-24 by Pension Disbursement EPFO."
        val result = HeuristicScamDetector.analyze(message, sender = "VK-SBIINB")
        assertEquals(RiskLevel.NO_OBVIOUS_WARNING, result.riskLevel)
    }
}
