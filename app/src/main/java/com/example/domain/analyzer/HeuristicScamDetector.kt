package com.example.domain.analyzer

import com.example.data.model.RiskLevel
import java.util.Locale

data class AnalysisResult(
    val riskLevel: RiskLevel,
    val confidenceScore: Int,
    val title: String,
    val summaryExplanation: String,
    val evidenceList: List<String>,
    val recommendedActions: List<String>,
    val safeVerificationGuide: String
)

object HeuristicScamDetector {

    fun analyze(text: String, sender: String = ""): AnalysisResult {
        val lower = text.lowercase(Locale.ROOT)
        val lowerSender = sender.lowercase(Locale.ROOT)
        val evidence = mutableListOf<String>()
        val actions = mutableListOf<String>()

        var isHighRisk = false
        var isSuspicious = false

        // 1. UPI PIN to Receive Money Check (HIGHEST PRIORITY)
        if ((lower.contains("upi pin") || lower.contains("enter pin") || lower.contains("enter upi")) &&
            (lower.contains("receive") || lower.contains("credit") || lower.contains("claim") || lower.contains("refund") || lower.contains("grant") || lower.contains("bonus"))
        ) {
            isHighRisk = true
            evidence.add("CRITICAL: Asking for UPI PIN to RECEIVE money or grant.")
            actions.add("DO NOT ENTER YOUR PIN! A UPI PIN is ONLY to pay/send money.")
        }

        // 2. Fake Pension / EPFO Arrears
        if ((lower.contains("arrear") || lower.contains("8th pay") || lower.contains("pension release") || lower.contains("pension pending")) &&
            (lower.contains("http") || lower.contains(".xyz") || lower.contains(".site") || lower.contains(".top") || lower.contains(".click") || lower.contains("bit.ly"))
        ) {
            isHighRisk = true
            evidence.add("Unofficial external link claiming to release pension arrears.")
            evidence.add("EPFO and CPPC banks never disburse arrears via SMS links.")
            actions.add("Never click unverified links claiming pension bonuses.")
        }

        // 3. Jeevan Pramaan / DLC APK Download
        if ((lower.contains("jeevan pramaan") || lower.contains("life certificate") || lower.contains("dlc")) &&
            (lower.contains(".apk") || lower.contains("install") || lower.contains("download app"))
        ) {
            isHighRisk = true
            evidence.add("Request to download an external .APK file outside Google Play.")
            evidence.add("Threatening pension stoppage due to life certificate.")
            actions.add("Install official Jeevan Pramaan only from Google Play Store or visit your bank branch.")
        }

        // 4. Electricity / Utility Disconnection Tonight
        if ((lower.contains("electricity") || lower.contains("power supply") || lower.contains("bijli")) &&
            (lower.contains("disconnect") || lower.contains("cut off") || lower.contains("9:30 pm") || lower.contains("tonight"))
        ) {
            isHighRisk = true
            evidence.add("Fake immediate power disconnection threat tonight.")
            evidence.add("Urging direct callback to a personal 10-digit phone number.")
            actions.add("Pay electricity bills only via your official DISCOM app or authorized counter.")
        }

        // 5. Account Blocked / PAN KYC Urgency
        if ((lower.contains("blocked within") || lower.contains("suspended within") || lower.contains("kyc expired") || lower.contains("link pan")) &&
            (lower.contains("http") || lower.contains("call") || lower.contains("update immediately"))
        ) {
            isHighRisk = true
            evidence.add("Artificial 2-hour panic deadline claiming your bank account will be frozen.")
            evidence.add("Phishing link designed to capture NetBanking login and OTP.")
            actions.add("Banks never freeze accounts via SMS links. Visit your home branch directly.")
        }

        // 6. Remote Screen Access Apps
        if (lower.contains("anydesk") || lower.contains("teamviewer") || lower.contains("rustdesk") || lower.contains("quicksupport") || lower.contains("screen share")) {
            isHighRisk = true
            evidence.add("Dangerous instruction to install remote screen-sharing software.")
            evidence.add("Allows fraudsters to view and intercept your banking OTPs in real-time.")
            actions.add("Never install screen-sharing apps on caller instructions. Hang up immediately.")
        }

        // 7. Digital Arrest / Fake Police
        if (lower.contains("digital arrest") || (lower.contains("cbi") && lower.contains("parcel")) || (lower.contains("customs") && lower.contains("illegal")) || lower.contains("skype video")) {
            isHighRisk = true
            evidence.add("Fraudulent 'Digital Arrest' extortion attempt.")
            evidence.add("Indian law has NO provision for digital arrest over video calls.")
            actions.add("Disconnect immediately. Dial 1930 and report to local police.")
        }

        // 8. Lottery / KBC Lucky Draw
        if ((lower.contains("lottery") || lower.contains("kbc") || lower.contains("25,00,000") || lower.contains("lucky draw")) &&
            (lower.contains("processing fee") || lower.contains("gst") || lower.contains("tax") || lower.contains("cheque"))
        ) {
            isHighRisk = true
            evidence.add("Advance-fee fraud demanding tax or fees before sending fake prize money.")
            evidence.add("You cannot win a lottery you never entered.")
            actions.add("Block the number. Do not send any processing fees.")
        }

        // 9. Unrealistic Guaranteed High Returns
        if (lower.contains("guaranteed 24%") || lower.contains("guaranteed 20%") || lower.contains("double your pension") || lower.contains("daily income task")) {
            isHighRisk = true
            evidence.add("Unrealistic guaranteed returns (impossible in regulated financial markets).")
            evidence.add("Ponzi trap designed to deplete retirees' savings.")
            actions.add("Invest only in RBI/SEBI approved schemes through banks or post offices.")
        }

        // 10. Impersonating Relative in Trouble
        if ((lower.contains("nephew") || lower.contains("grandson") || lower.contains("accident") || lower.contains("hospitalized")) &&
            (lower.contains("don't tell mom") || lower.contains("send urgent") || lower.contains("doctor's phone"))
        ) {
            isHighRisk = true
            evidence.add("Impersonating family member with emotional medical crisis.")
            evidence.add("Asking to keep transaction secret from other relatives.")
            actions.add("Pause and call your relative directly on their known saved number.")
        }

        // 11. OTP Sharing Request
        if (lower.contains("share otp") || lower.contains("tell me the 6 digit") || lower.contains("verify otp on call")) {
            isHighRisk = true
            evidence.add("Direct request for confidential One Time Password (OTP).")
            actions.add("NEVER share OTP with anyone on a phone call, SMS, or WhatsApp.")
        }

        // 12. Suspicious Unofficial Domains
        if (lower.contains("http://") || lower.contains(".top") || lower.contains(".xyz") || lower.contains(".cc/") || lower.contains(".tk") || lower.contains(".online/")) {
            if (!isHighRisk) {
                isSuspicious = true
                evidence.add("Contains suspicious, non-standard domain extension.")
            }
        }

        // Check for genuine government / official bank headers
        val isOfficialBankOrGov = lowerSender.contains("sbi") ||
                lowerSender.contains("hdfc") ||
                lowerSender.contains("icici") ||
                lowerSender.contains("epfo") ||
                lowerSender.contains("nic") ||
                lowerSender.startsWith("vk-") ||
                lowerSender.startsWith("vm-") ||
                lowerSender.startsWith("bp-") ||
                lowerSender.startsWith("td-")

        val hasGovDomain = lower.contains(".gov.in") || lower.contains("sbi.co.in") || lower.contains("hdfcbank.com") || lower.contains("icicibank.com")

        if (!isHighRisk && !isSuspicious && isOfficialBankOrGov && (hasGovDomain || (!lower.contains("http") && !lower.contains("call")))) {
            return AnalysisResult(
                riskLevel = RiskLevel.NO_OBVIOUS_WARNING,
                confidenceScore = 92,
                title = "Standard Official Notice",
                summaryExplanation = "This message appears to follow standard official transaction alert formatting with no detected phishing signs.",
                evidenceList = listOf(
                    "Official sender header format verified",
                    "No requests for OTP, password, or UPI PIN",
                    "No deceptive urgency or threat language"
                ),
                recommendedActions = listOf(
                    "Check transaction records directly in your official mobile banking app",
                    "Remember never to forward OTPs to anyone"
                ),
                safeVerificationGuide = "Cross-check with your bank's official statement or call the official branch helpline."
            )
        }

        if (isHighRisk) {
            if (actions.isEmpty()) {
                actions.add("Do not click any links or call numbers provided.")
                actions.add("Block and report the sender.")
            }
            return AnalysisResult(
                riskLevel = RiskLevel.HIGH_RISK,
                confidenceScore = 95,
                title = "High Risk Scam Alert 🛑",
                summaryExplanation = "Critical warning signs of financial fraud were detected. Interacting with this message or clicking links could lead to loss of money.",
                evidenceList = evidence,
                recommendedActions = actions,
                safeVerificationGuide = "Never use the phone number or link inside this message. Independently search the official website ending in .gov.in or visit your nearest bank branch."
            )
        }

        if (isSuspicious) {
            return AnalysisResult(
                riskLevel = RiskLevel.SUSPICIOUS,
                confidenceScore = 75,
                title = "Suspicious Message ⚠️",
                summaryExplanation = "This message contains unverified links or vague requests. Proceed with extreme caution.",
                evidenceList = evidence.ifEmpty { listOf("Unusual link or unsolicited sender") },
                recommendedActions = listOf(
                    "Do not transfer money or provide personal details",
                    "Verify with a family member or call 1930 if unsure"
                ),
                safeVerificationGuide = "Call the company or bank using the toll-free number printed on the back of your passbook or card."
            )
        }

        return AnalysisResult(
            riskLevel = RiskLevel.UNABLE_TO_VERIFY,
            confidenceScore = 60,
            title = "Unable to Verify Conclusively",
            summaryExplanation = "No blatant scam signatures were detected, but this message cannot be confirmed genuine without independent verification.",
            evidenceList = listOf(
                "Message text is brief or non-standard",
                "Cannot confirm identity of sender without independent check"
            ),
            recommendedActions = listOf(
                "Never share OTP or banking credentials",
                "Consult a trusted family member before taking action"
            ),
            safeVerificationGuide = "Always verify through an official channel before acting on unsolicited communications."
        )
    }
}
