package com.example.data.model

data class DailySafetyTip(
    val id: String,
    val title: String,
    val summary: String,
    val detailedAdvice: String,
    val category: String,
    val audioScript: String
)

data class BankEmergencyContact(
    val bankName: String,
    val tollFreeNumber: String,
    val alternateNumber: String,
    val fraudEmailOrUrl: String,
    val colorHex: Long = 0xFF5B45A6
)

object SafetyData {
    val dailyTips: List<DailySafetyTip> = listOf(
        DailySafetyTip(
            id = "tip_upi_pin",
            title = "Golden Rule: UPI PIN is Only to SEND Money",
            summary = "You never need to enter your UPI PIN to receive pension, refunds, or lottery prizes.",
            detailedAdvice = "If someone claims they are sending you money and asks you to enter your UPI PIN or scan a QR code, PAUSE! Entering your PIN will immediately debit money from your account.",
            category = "UPI Safety",
            audioScript = "Namaste. Remember the golden rule: Your UPI PIN is only used when you are sending money. You never need to type your UPI PIN to receive money or pension."
        ),
        DailySafetyTip(
            id = "tip_digital_arrest",
            title = "Digital Arrest is 100% Fake",
            summary = "Indian law has no concept of 'Digital Arrest' over WhatsApp or Skype video calls.",
            detailedAdvice = "Police, CBI, and Customs never interrogate people on video calls or demand money transfers into 'RBI verification accounts'. If someone threatens you with arrest on video, disconnect immediately.",
            category = "Impersonation",
            audioScript = "Attention: Indian law does not have any digital arrest. Police will never ask you to transfer money on a video call. Hang up and call 1930."
        ),
        DailySafetyTip(
            id = "tip_screen_sharing",
            title = "Never Install Remote Apps on Call Instructions",
            summary = "Apps like AnyDesk, TeamViewer, and QuickSupport let strangers control your phone.",
            detailedAdvice = "Fraudsters pretend to be bank agents or electricity officers and tell you to download support apps. These apps allow them to see your bank OTPs directly as they arrive on your screen.",
            category = "Device Safety",
            audioScript = "Never install screen sharing apps like AnyDesk on the instruction of anyone calling your phone. It allows them to read your bank OTPs."
        ),
        DailySafetyTip(
            id = "tip_pan_kyc",
            title = "Banks Never Send Threatening SMS Links",
            summary = "Official banks never send links asking you to update PAN or Aadhaar within 2 hours.",
            detailedAdvice = "Threats of account suspension within 24 hours are designed to induce panic. Always update your KYC in person at your home branch or through the official mobile banking app.",
            category = "Banking",
            audioScript = "Banks will never block your account in two hours via an SMS link. Always visit your branch directly for KYC updates."
        ),
        DailySafetyTip(
            id = "tip_1930_golden_hour",
            title = "The Golden Hour: Call 1930 Immediately",
            summary = "Reporting financial fraud within 2 hours gives banks the best chance to freeze stolen funds.",
            detailedAdvice = "If any unauthorized transaction occurs, immediately dial 1930 (National Cyber Crime Helpline) or report on cybercrime.gov.in. Keep your bank account number and transaction ID ready.",
            category = "Emergency",
            audioScript = "If money was deducted by fraud, call helpline 1930 immediately. Reporting within two hours can help freeze your funds before they are withdrawn."
        )
    )

    val verifiedBankDirectory: List<BankEmergencyContact> = listOf(
        BankEmergencyContact(
            bankName = "State Bank of India (SBI)",
            tollFreeNumber = "1800 1234",
            alternateNumber = "1800 2100",
            fraudEmailOrUrl = "report.phishing@sbi.co.in",
            colorHex = 0xFF1A5A96
        ),
        BankEmergencyContact(
            bankName = "Punjab National Bank (PNB)",
            tollFreeNumber = "1800 180 2222",
            alternateNumber = "1800 103 2222",
            fraudEmailOrUrl = "care@pnb.co.in",
            colorHex = 0xFFA01828
        ),
        BankEmergencyContact(
            bankName = "HDFC Bank",
            tollFreeNumber = "1800 1600",
            alternateNumber = "1800 2600",
            fraudEmailOrUrl = "report.fraud@hdfcbank.com",
            colorHex = 0xFF004C8F
        ),
        BankEmergencyContact(
            bankName = "ICICI Bank",
            tollFreeNumber = "1800 1080",
            alternateNumber = "1800 200 3344",
            fraudEmailOrUrl = "antiphishing@icicibank.com",
            colorHex = 0xFFB33010
        ),
        BankEmergencyContact(
            bankName = "Bank of Baroda",
            tollFreeNumber = "1800 5700",
            alternateNumber = "1800 258 4455",
            fraudEmailOrUrl = "cyberfraud@bankofbaroda.com",
            colorHex = 0xFFE05206
        ),
        BankEmergencyContact(
            bankName = "Canara Bank",
            tollFreeNumber = "1800 425 0018",
            alternateNumber = "1800 103 0018",
            fraudEmailOrUrl = "fmu@canarabank.com",
            colorHex = 0xFF0075A9
        ),
        BankEmergencyContact(
            bankName = "Axis Bank",
            tollFreeNumber = "1860 419 5555",
            alternateNumber = "1860 500 5555",
            fraudEmailOrUrl = "fraud.alert@axisbank.com",
            colorHex = 0xFF861F41
        )
    )
}
