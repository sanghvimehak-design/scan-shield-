package com.example.data.model

data class QuizScenario(
    val id: String,
    val title: String,
    val category: String,
    val story: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val safetyTip: String
)

data class UserBadge(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String,
    val unlocked: Boolean
)

object QuizRepository {
    val scenarios: List<QuizScenario> = listOf(
        QuizScenario(
            id = "q1_pension_arrears",
            title = "The 8th Pay Commission Message",
            category = "Pension Fraud",
            story = "You receive an SMS saying your pension arrears of Rs 35,000 are ready to be deposited, but you need to click a link 'pension-claim.net' to submit your Aadhaar number before 5 PM.",
            question = "What should you do?",
            options = listOf(
                "Click the link immediately so your arrears aren't cancelled",
                "Do NOT click the link; check your official pension passbook or bank directly",
                "Forward the link to your pensioner friends group",
                "Reply to the SMS asking for more information"
            ),
            correctIndex = 1,
            explanation = "Official pension authorities like EPFO never disburse arrears through random links or give same-day deadlines.",
            safetyTip = "Check pension status only on official portals ending in .gov.in or in person at your bank branch."
        ),
        QuizScenario(
            id = "q2_upi_pin_receive",
            title = "The Friendly Buyer on OLX",
            category = "UPI Safety",
            story = "A buyer wants to buy your old sofa and sends a QR code on WhatsApp, saying: 'Sir, scan this code and enter your UPI PIN, the Rs 4,000 will be credited directly to your bank.'",
            question = "Is this safe to do?",
            options = listOf(
                "Yes, scanning is needed to confirm your identity",
                "No! You NEVER enter a UPI PIN to receive money",
                "Yes, as long as the amount is under Rs 5,000",
                "Only if the buyer sends their Aadhaar card first"
            ),
            correctIndex = 1,
            explanation = "A UPI PIN is your digital signature to SEND money out of your account. You NEVER need to enter a UPI PIN to receive money.",
            safetyTip = "Remember the golden rule: PIN is for Paying, never for receiving! 🛑"
        ),
        QuizScenario(
            id = "q3_power_cut_threat",
            title = "The 9:30 PM Electricity Power Cut",
            category = "Utility Scam",
            story = "At 7:00 PM, an SMS from a 10-digit mobile number arrives: 'Your power will be disconnected at 9:30 PM due to unpaid bill. Call Electricity Officer Sharma immediately.'",
            question = "How should you respond?",
            options = listOf(
                "Call the number in panic and pay whatever small fee they ask",
                "Ignore the SMS and pay through your regular official DISCOM app or check physical bill",
                "Share your credit card details to prevent immediate power cutoff",
                "Download the app the officer sends via WhatsApp"
            ),
            correctIndex = 1,
            explanation = "Power utility companies send notices weeks in advance via registered bills, never via personal 10-digit phone numbers with 2-hour threats.",
            safetyTip = "Never call phone numbers listed inside alarming text messages."
        ),
        QuizScenario(
            id = "q4_bank_manager_call",
            title = "The Helpful Bank Manager",
            category = "KYC Scam",
            story = "A caller claims to be the Head Manager of your bank. He knows your name and says your ATM debit card has expired. He asks for the 16-digit card number and the 3-digit CVV on the back.",
            question = "What is the proper action?",
            options = listOf(
                "Tell him the CVV since he already knows your name",
                "Ask him to send an SMS and reply there",
                "Refuse and disconnect immediately; genuine banks NEVER ask for your CVV or PIN",
                "Give half the numbers and tell him to guess the rest"
            ),
            correctIndex = 2,
            explanation = "Bank staff already have access to internal systems and are strictly prohibited by the Reserve Bank of India from asking for card CVVs or passwords.",
            safetyTip = "Your 3-digit CVV and OTPs are secret keys to your money—never speak them aloud."
        ),
        QuizScenario(
            id = "q5_anydesk_remote",
            title = "The Quick Mobile Support Tool",
            category = "Remote Access Trap",
            story = "While trying to book a train ticket on IRCTC, an error occurs. You search customer care on Google and call a number. The person tells you to install 'AnyDesk' or 'RustDesk' so they can assist you.",
            question = "Why is this dangerous?",
            options = listOf(
                "It uses too much internet data",
                "It allows the stranger to view your screen, intercept OTPs, and take over your phone",
                "It will slow down your train ticket booking",
                "It is completely safe if the agent is polite"
            ),
            correctIndex = 1,
            explanation = "Screen-sharing tools give scammers complete visibility of your phone screen, enabling them to read incoming banking OTPs and steal funds in seconds.",
            safetyTip = "Never install remote control applications at the request of any customer support representative."
        ),
        QuizScenario(
            id = "q6_digital_arrest",
            title = "The CBI Video Interrogation",
            category = "Digital Arrest Scam",
            story = "A caller dressed in a police uniform on WhatsApp video calls you, claiming your Aadhaar was used in money laundering and that you are under 'Digital Arrest' until you transfer your savings to a verification account.",
            question = "What is the truth about 'Digital Arrest'?",
            options = listOf(
                "It is a legal police procedure introduced for online crimes",
                "It is a complete fraud. Indian law has NO provision for 'Digital Arrest' over video calls",
                "You should immediately transfer money to prove your innocence",
                "You must stay on the video call for 24 hours"
            ),
            correctIndex = 1,
            explanation = "The Supreme Court and Ministry of Home Affairs have affirmed: 'Digital Arrest' is 100% fraudulent. Police never demand financial transfers to clear charges.",
            safetyTip = "Immediately disconnect such calls and report them to 1930 and https://cybercrime.gov.in."
        ),
        QuizScenario(
            id = "q7_kbc_lottery_fee",
            title = "The Rs 25 Lakh WhatsApp Lottery",
            category = "Lottery Fraud",
            story = "You receive an image of a lottery cheque for Rs 25,00,000 with your phone number on it. A voice note tells you to send Rs 5,000 for government tax before the cheque can be couriered.",
            question = "What should you recognize?",
            options = listOf(
                "Pay the Rs 5,000 because 25 Lakhs is much larger",
                "Ask for a discount on the tax amount",
                "You cannot win a lottery you never bought; asking for advance fees is always fraud",
                "Visit Mumbai to collect the cheque in person"
            ),
            correctIndex = 2,
            explanation = "Advance fee fraud relies on excitement. Genuine prizes never require advance payment of tax through personal UPI.",
            safetyTip = "Block and report any number offering sudden lottery prizes or lucky draws."
        ),
        QuizScenario(
            id = "q8_fake_nephew_accident",
            title = "The Urgent Nephew in Hospital",
            category = "Impersonation",
            story = "An unknown number messages you on WhatsApp: 'Uncle, it's Rahul. I had an accident and need Rs 20,000 for blood units. Don't tell Mom, please UPI quickly to hospital@upi.'",
            question = "What is your best first step?",
            options = listOf(
                "Transfer Rs 20,000 immediately out of affection",
                "Pause, call Rahul on his regular saved phone number, or call his mother to verify",
                "Send Rs 10,000 first just in case",
                "Delete WhatsApp from your phone"
            ),
            correctIndex = 1,
            explanation = "Scammers exploit love and urgency to prevent victims from verifying with family members.",
            safetyTip = "Always verify urgent family requests through an independent, pre-saved phone number."
        ),
        QuizScenario(
            id = "q9_jeevan_pramaan_whatsapp",
            title = "The Life Certificate WhatsApp Agent",
            category = "Pensioners Safety",
            story = "A person contacts you claiming to be a door-to-door pension officer who can update your Jeevan Pramaan online if you send photos of both sides of your Aadhaar card and your bank passbook.",
            question = "Is this how Jeevan Pramaan is processed?",
            options = listOf(
                "Yes, it is very convenient for senior citizens",
                "No! Official Jeevan Pramaan requires secure biometric/face authentication on official app or authorized centers",
                "Send only the bank passbook but not the Aadhaar",
                "Pay the agent Rs 500 advance fee"
            ),
            correctIndex = 1,
            explanation = "Sharing your Aadhaar and bank passbook unvetted on WhatsApp exposes you to identity theft and SIM swap attacks.",
            safetyTip = "Submit Jeevan Pramaan via the official Jeevan Pramaan Face App, post office, or your pension disbursing bank branch."
        ),
        QuizScenario(
            id = "q10_reward_points_sms",
            title = "The Expiring Credit Card Points",
            category = "Phishing Link",
            story = "An SMS states: 'Your 5,000 bank points worth Rs 2,500 will expire tonight. Redeem cash directly to your account by entering your card details at http://sbi-points.cc'.",
            question = "Why should you NOT click this link?",
            options = listOf(
                "Because points never give cash",
                "The link '.cc' is a fake phishing website created to steal full card details and CVV",
                "Because reward points cannot expire",
                "It is better to redeem in the morning"
            ),
            correctIndex = 1,
            explanation = "Phishing websites replicate bank portals to steal your card number, expiry, CVV, and OTP.",
            safetyTip = "Only access bank rewards through your official mobile banking app."
        ),
        QuizScenario(
            id = "q11_golden_scheme_24",
            title = "The 24% Guaranteed Pension Return",
            category = "Investment Fraud",
            story = "An agent on a WhatsApp group offers a 'Government-approved Senior Gold Investment' promising 24% annual interest paid monthly. He shows fabricated stamp papers with government logos.",
            question = "How can you protect your retirement savings?",
            options = listOf(
                "Invest a small amount of Rs 10,000 to test if returns come",
                "Understand that 24% guaranteed return is unrealistic and illegal; legitimate schemes are regulated by RBI/SEBI",
                "Invite your pension club members to invest together",
                "Ask for the agent's Aadhaar card and then transfer money"
            ),
            correctIndex = 1,
            explanation = "Scammers often pay the first 1 or 2 small installments from your own principal before vanishing with your entire savings.",
            safetyTip = "Stick to verified government schemes like Senior Citizen Savings Scheme (SCSS), PMVVY, or RBI Floating Rate Bonds."
        ),
        QuizScenario(
            id = "q12_reporting_fraud_1930",
            title = "What to Do If Money Was Deducted",
            category = "Emergency Action",
            story = "Unfortunately, Rs 15,000 was debited from your account due to a deceptive payment link 15 minutes ago. You are worried and anxious.",
            question = "What is the most effective immediate step?",
            options = listOf(
                "Wait 24 hours to see if the money returns automatically",
                "Call 1930 immediately to freeze the money in the fraudster's account and notify your bank",
                "Change your phone wallpaper",
                "Post about it on Facebook"
            ),
            correctIndex = 1,
            explanation = "The National Cybercrime Helpline 1930 works in real-time with Indian banks to intercept transactions if reported in the 'Golden Hours'.",
            safetyTip = "Dial 1930 immediately or log in to https://cybercrime.gov.in within the first two hours of any financial fraud."
        )
    )

    val defaultBadges: List<UserBadge> = listOf(
        UserBadge("badge_scam_shield", "Raksha Novice", "Completed your first digital safety lesson", "ic_shield", true),
        UserBadge("badge_pin_guardian", "UPI Guardian", "Mastered the UPI PIN safety rule", "ic_pin", false),
        UserBadge("badge_kyc_defender", "KYC Defender", "Learned how to spot fake banking calls", "ic_lock", false),
        UserBadge("badge_pension_protector", "Pension Protector", "Secured your retirement from fake arrears", "ic_star", false),
        UserBadge("badge_sos_champion", "1930 Hero", "Knows the exact steps during a cyber emergency", "ic_sos", false)
    )
}
