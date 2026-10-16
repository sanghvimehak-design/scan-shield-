package com.example.data.model

data class ThreatPreset(
    val id: String,
    val title: String,
    val category: String,
    val rawContent: String,
    val senderOrSource: String,
    val isScam: Boolean,
    val expectedRisk: RiskLevel,
    val warningSigns: List<String>,
    val explanation: String,
    val safeStep: String
)

object DemoThreatPresets {
    val scamPresets: List<ThreatPreset> = listOf(
        ThreatPreset(
            id = "scam_epfo_arrears",
            title = "Fake EPFO Pension Arrears Link",
            category = "Pension Arrears Scam",
            rawContent = "Dear Pensioner, Your 8th Pay Commission arrears of Rs 48,250 are pending release due to incomplete bank KYC. Click http://epfo-claim-pension.xyz to approve payout instantly before deadline.",
            senderOrSource = "+91 94012 88471 (Unknown mobile)",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Unofficial fake domain (.xyz instead of official .gov.in)",
                "Urgent panic message with artificial deadline",
                "EPFO never asks to approve payouts via external SMS links"
            ),
            explanation = "Fraudsters create fake websites imitating EPFO to steal your bank credentials and passwords.",
            safeStep = "Check your official pension passbook exclusively at unifiedportal-mem.epfindia.gov.in or via the UMANG app."
        ),
        ThreatPreset(
            id = "scam_jeevan_pramaan_apk",
            title = "Jeevan Pramaan Fake APK Notice",
            category = "Fake App / Malware",
            rawContent = "Urgent Alert: Your Digital Life Certificate (Jeevan Pramaan) failed biometric verification. Download and install JeevanPramaan_v4.apk immediately to avoid pension stoppage: http://dlc-pension-update.site/jp.apk",
            senderOrSource = "WhatsApp Forward from unknown number",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Direct .APK file download link outside Google Play Store",
                "Threatening stoppage of monthly pension",
                "Third-party untrusted domain (.site)"
            ),
            explanation = "Installing APK files from SMS or WhatsApp installs malicious spyware that steals your banking OTPs and passwords.",
            safeStep = "Download official apps only from Google Play Store or visit your nearest post office / bank branch for Jeevan Pramaan submission."
        ),
        ThreatPreset(
            id = "scam_electricity_power_cut",
            title = "Electricity Disconnection Threat",
            category = "Utility Bill Scam",
            rawContent = "Dear Consumer, Your electricity supply will be disconnected at 9:30 PM tonight because your previous month bill was not updated. Immediately call Electricity Officer Verma at 9876543210.",
            senderOrSource = "SMS from personal 10-digit number",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Personal 10-digit mobile number instead of official utility sender ID (e.g. BSES/TATA/MSEDCL)",
                "Creating immediate panic with a same-day 9:30 PM power cutoff",
                "Asking you to call a personal number to resolve billing"
            ),
            explanation = "The scammer on the call will ask you to download AnyDesk or pay Rs 10 to update bills, draining your account.",
            safeStep = "Always pay electricity bills only via your official DISCOM app, bank app, or authorized physical payment center."
        ),
        ThreatPreset(
            id = "scam_upi_qr_receive",
            title = "Scan QR & Enter PIN to Receive Grant",
            category = "UPI PIN Reversal Scam",
            rawContent = "Congratulations! You have received Central Government Senior Citizen Welfare Grant of Rs 5,000. Scan the attached QR code and enter your 6-digit UPI PIN to credit funds to your bank account.",
            senderOrSource = "WhatsApp Image & Message",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Asking for UPI PIN to RECEIVE money",
                "Unsolicited government monetary grant claim",
                "Deceptive QR code payment request"
            ),
            explanation = "CRITICAL: UPI PIN is ONLY needed to SEND money. Entering your UPI PIN will instantly deduct money from your account!",
            safeStep = "Never scan a QR code or enter your UPI PIN when receiving money. Money is credited directly without any PIN."
        ),
        ThreatPreset(
            id = "scam_sbi_pan_freeze",
            title = "Bank Account Suspension Threat",
            category = "Banking KYC Scam",
            rawContent = "SBI Alert: Your Savings A/C XXXX4012 will be blocked within 2 hours due to unlinked PAN card. Update your PAN details at http://sbi-pan-kyc-update.online immediately.",
            senderOrSource = "+91 89123 99872",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "2-hour panic deadline to freeze account",
                "Fake lookalike banking domain (.online instead of sbi.co.in)",
                "Banks never send link via personal phone numbers"
            ),
            explanation = "The fake website looks identical to SBI's login portal and captures your username, password, and OTP.",
            safeStep = "Visit your home branch directly or log in only at onlinesbi.sbi or using the official YONO SBI app."
        ),
        ThreatPreset(
            id = "scam_kbc_lottery",
            title = "KBC / WhatsApp Lottery Lucky Draw",
            category = "Lottery Fraud",
            rawContent = "Dear Friend, Your SIM number has won Rs 25,00,000 in KBC All India Lucky Draw! To claim your cheque, send processing fee & GST tax of Rs 3,500 to SBI Manager Rana via PhonePe: 9988776655.",
            senderOrSource = "+92 301 2345678 (Foreign country code)",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Foreign country code (+92) pretending to be Indian TV show",
                "Demanding 'processing fees' or 'tax' in advance to release fake winnings",
                "You cannot win a lottery you never entered"
            ),
            explanation = "Any lottery or prize asking for advance fees is 100% fraudulent. Once you send the money, the scammers disappear.",
            safeStep = "Block the sender immediately and never transfer money to unknown numbers for claimed prizes."
        ),
        ThreatPreset(
            id = "scam_remote_anydesk",
            title = "AnyDesk / QuickSupport Trapping",
            category = "Remote Screen Access",
            rawContent = "Dear Customer, Your Aadhaar biometric was blocked. To unblock without branch visit, install RustDesk QuickSupport app from https://help-aadhaar.org/agent and share the 9-digit code with our technician.",
            senderOrSource = "Call & SMS follow-up",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Asking to install screen-sharing software (AnyDesk, TeamViewer, RustDesk)",
                "Requesting 9-digit remote access permission code",
                "UIDAI / Aadhaar never provides remote screen support"
            ),
            explanation = "Sharing the remote access code allows fraudsters to view your phone screen, read OTPs, and control your device.",
            safeStep = "Never install AnyDesk or screen-sharing tools on instructions from an unknown caller."
        ),
        ThreatPreset(
            id = "scam_police_digital_arrest",
            title = "Fake Police / Digital Arrest Threat",
            category = "Extortion & Impersonation",
            rawContent = "FedEx Customs Notification: A parcel containing illegal medicines and duplicate passports sent under your Aadhaar has been intercepted by CBI. A digital arrest warrant is issued. Connect on Skype/WhatsApp immediately or police will arrive.",
            senderOrSource = "Automated IVR call followed by WhatsApp video call",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Concept of 'Digital Arrest' is completely fictitious and illegal in India",
                "Police and CBI never interrogate suspects via WhatsApp or Skype video calls",
                "Demanding money or bank transfers to clear your name"
            ),
            explanation = "Scammers dress in fake police uniforms to terrorize seniors into liquidating fixed deposits and savings.",
            safeStep = "Disconnect the call immediately. Call 1930 or visit your local police station. 'Digital Arrest' does not exist."
        ),
        ThreatPreset(
            id = "scam_high_interest_scheme",
            title = "Guaranteed 24% Senior Pension Scheme",
            category = "Investment Fraud",
            rawContent = "Special PM Senior Citizen Safe Gold Scheme: Guaranteed 24% annual returns backed by RBI. Deposit minimum Rs 50,000 today to earn monthly interest of Rs 1,000 directly in your account. WhatsApp 9123456789.",
            senderOrSource = "Telegram & WhatsApp message",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Unrealistically high guaranteed returns (24% is impossible in legitimate schemes)",
                "Misusing Prime Minister and RBI names for fake schemes",
                "Unregistered private individuals soliciting money"
            ),
            explanation = "This is a Ponzi fraud designed to steal retirees' life savings and gratuity payouts.",
            safeStep = "Invest only in RBI/SEBI regulated schemes like Senior Citizen Savings Scheme (SCSS) through banks or post offices."
        ),
        ThreatPreset(
            id = "scam_nephew_accident",
            title = "Impersonating Relative in Emergency",
            category = "Family Impersonation",
            rawContent = "Hello Uncle, this is your nephew Rahul. I met with a bike accident in Bangalore and my phone broke. I am using a doctor's phone. Please send Rs 15,000 immediately to hospital UPI clinicpay@upi for urgent treatment. Please don't tell mom.",
            senderOrSource = "+91 78901 23456 (Unknown number)",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Unknown phone number claiming to be a close relative",
                "High emotional panic and medical emergency urgency",
                "Plea to keep it secret from other family members"
            ),
            explanation = "Scammers use AI voice clips or social media data to impersonate grandchildren and nephews in fake crises.",
            safeStep = "Pause and call your nephew on his known number or call his parents directly to verify before transferring any money."
        ),
        ThreatPreset(
            id = "scam_credit_card_points",
            title = "Credit Card Reward Points Expiring",
            category = "Phishing Scam",
            rawContent = "Dear Cardholder, 8,450 ICICI Reward points worth Rs 4,225 are expiring tonight. Redeem points directly into your bank account as cash by visiting http://icici-points-cash.cc/redeem now.",
            senderOrSource = "+91 96543 21098",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Personal sender number instead of ICICI Bank sender header",
                "Third-party fake domain (.cc instead of icicibank.com)",
                "Fake 'expiring tonight' pressure tactic"
            ),
            explanation = "The site requests full card number, CVV, expiry date, and the OTP sent to your phone to siphon money.",
            safeStep = "Redeem bank points only through your official bank mobile app or internet banking portal."
        ),
        ThreatPreset(
            id = "scam_part_time_rating",
            title = "Work From Home Pension Booster",
            category = "Task / Job Scam",
            rawContent = "Retirement Special: Earn Rs 3,000 to Rs 8,000 daily from home by liking YouTube videos and rating hotels on Google Maps. No skills required. Immediate daily payout. Click t.me/pensioner_part_time to join.",
            senderOrSource = "SMS from unknown number",
            isScam = true,
            expectedRisk = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                "Too good to be true: huge daily payouts for simple video likes",
                "Telegram link solicitation",
                "Leads to 'prepaid task' fraud where victims are forced to pay money to withdraw"
            ),
            explanation = "Victims are lured into paying small amounts for 'upgraded tasks' and end up losing lakhs of rupees.",
            safeStep = "Never join unsolicited Telegram earning groups. Genuine companies never ask you to pay money to get work."
        )
    )

    val benignPresets: List<ThreatPreset> = listOf(
        ThreatPreset(
            id = "benign_sbi_pension_credit",
            title = "Genuine SBI Pension Credit SMS",
            category = "Official Bank Alert",
            rawContent = "Your A/C ending with 4012 is credited with Rs 28,450.00 on 30-Sep-24 by Pension Disbursement EPFO. Avail Bal: Rs 42,100.00 - State Bank of India.",
            senderOrSource = "VK-SBIINB (Official 6-char Alpha Header)",
            isScam = false,
            expectedRisk = RiskLevel.NO_OBVIOUS_WARNING,
            warningSigns = listOf(
                "Official bank alpha sender header (VK-SBIINB)",
                "No suspicious external links or phone numbers requested",
                "Standard informative transaction alert"
            ),
            explanation = "This is a legitimate automated bank credit alert informing you of pension deposit.",
            safeStep = "Always cross-check your account balance inside your official YONO SBI app or passbook."
        ),
        ThreatPreset(
            id = "benign_epfo_monthly",
            title = "Official EPFO Monthly Passbook Notice",
            category = "Government Notification",
            rawContent = "Dear Member, monthly contribution of Rs 1,800 is credited in your EPF account for the month of Aug-2024. For more details visit unifiedportal-mem.epfindia.gov.in - EPFO",
            senderOrSource = "BP-EPFOHO (Official Government Header)",
            isScam = false,
            expectedRisk = RiskLevel.NO_OBVIOUS_WARNING,
            warningSigns = listOf(
                "Official government sender header (BP-EPFOHO)",
                "Contains official gov.in domain (epfindia.gov.in)",
                "No urgent panic, no requests for OTP or payment"
            ),
            explanation = "This is an official administrative SMS sent by the Employees' Provident Fund Organisation.",
            safeStep = "Access your passbook only at official portals ending in .gov.in."
        ),
        ThreatPreset(
            id = "benign_bank_otp",
            title = "Genuine Bank OTP with Warning",
            category = "Authentication OTP",
            rawContent = "582914 is your Secret OTP for NetBanking login at HDFC Bank. Valid for 5 mins. NEVER share OTP with anyone including bank staff or callers. HDFC Bank.",
            senderOrSource = "VM-HDFCBK (Official Bank Header)",
            isScam = false,
            expectedRisk = RiskLevel.NO_OBVIOUS_WARNING,
            warningSigns = listOf(
                "Official bank sender header",
                "Prominently warns: 'NEVER share OTP with anyone'",
                "No links or external call-back requests"
            ),
            explanation = "This is a standard security OTP generated when logging into your account.",
            safeStep = "Only enter this OTP yourself in your official app. Never speak or send this code to anyone on a phone call."
        ),
        ThreatPreset(
            id = "benign_jeevan_pramaan_ack",
            title = "Official Jeevan Pramaan Submission",
            category = "Government Acknowledgment",
            rawContent = "Your Digital Life Certificate (DLC) with Pramaan ID 98234125 has been successfully generated and submitted to your Pension Disbursing Agency. Visit jeevanpramaan.gov.in - NIC",
            senderOrSource = "TD-NICSMS (National Informatics Centre)",
            isScam = false,
            expectedRisk = RiskLevel.NO_OBVIOUS_WARNING,
            warningSigns = listOf(
                "Official National Informatics Centre header (TD-NICSMS)",
                "Contains official .gov.in domain",
                "Informational receipt of life certificate"
            ),
            explanation = "Legitimate confirmation that your annual life certificate was recorded successfully.",
            safeStep = "Download your official certificate copy from jeevanpramaan.gov.in using your Pramaan ID."
        )
    )
}
