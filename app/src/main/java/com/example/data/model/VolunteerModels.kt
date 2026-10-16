package com.example.data.model

data class VolunteerProfile(
    val id: String,
    val name: String,
    val age: Int,
    val city: String,
    val state: String,
    val languages: List<String>,
    val bio: String,
    val specialties: List<String>,
    val rating: Float,
    val sessionsCompleted: Int,
    val verifiedBadge: String,
    val availableDays: String
)

data class HelpRequest(
    val id: String,
    val volunteerId: String,
    val volunteerName: String,
    val topic: String,
    val date: String,
    val timeSlot: String,
    val status: String // Pending, Confirmed, Completed
)

object VolunteerData {
    val demoVolunteers: List<VolunteerProfile> = listOf(
        VolunteerProfile(
            id = "vol_aarav",
            name = "Aarav Sharma",
            age = 22,
            city = "Pune",
            state = "Maharashtra",
            languages = listOf("English", "Hindi", "Marathi"),
            bio = "Computer Science student passionate about teaching grandparents how to use smartphones safely and recognize WhatsApp fraud.",
            specialties = listOf("UPI & Payment Safety", "Spotting WhatsApp Scams", "Smartphone Basics"),
            rating = 4.9f,
            sessionsCompleted = 34,
            verifiedBadge = "Senior Safety Mentor",
            availableDays = "Saturday & Sunday (10 AM - 4 PM)"
        ),
        VolunteerProfile(
            id = "vol_sneha",
            name = "Sneha Patel",
            age = 24,
            city = "Ahmedabad",
            state = "Gujarat",
            languages = listOf("English", "Hindi", "Gujarati"),
            bio = "Fintech analyst volunteering to teach pensioners how to check passbooks safely without falling for fake KYC calls.",
            specialties = listOf("Pension Apps & Portals", "Jeevan Pramaan Guidance", "Banking Alerts"),
            rating = 5.0f,
            sessionsCompleted = 52,
            verifiedBadge = "Gold Volunteer",
            availableDays = "Tue, Thu, Sat (5 PM - 7 PM)"
        ),
        VolunteerProfile(
            id = "vol_ananya",
            name = "Ananya Iyer",
            age = 21,
            city = "Chennai",
            state = "Tamil Nadu",
            languages = listOf("English", "Tamil"),
            bio = "Engineering graduate dedicated to helping retired elders navigate digital life certificates and online utility bill payments securely.",
            specialties = listOf("Jeevan Pramaan Face App", "Utility Bills Safety", "Tamil Voice Support"),
            rating = 4.8f,
            sessionsCompleted = 28,
            verifiedBadge = "Community Star",
            availableDays = "Monday to Friday (4 PM - 6 PM)"
        ),
        VolunteerProfile(
            id = "vol_rohan",
            name = "Rohan Das",
            age = 25,
            city = "Kolkata",
            state = "West Bengal",
            languages = listOf("English", "Hindi", "Bengali"),
            bio = "Cybersecurity enthusiast conducting interactive digital literacy circles for retirees in community centers and online.",
            specialties = listOf("Detecting Phishing Links", "Password Security", "SIM Swap Protection"),
            rating = 4.9f,
            sessionsCompleted = 41,
            verifiedBadge = "Cyber Shield Trainer",
            availableDays = "Daily (6 PM - 8 PM)"
        ),
        VolunteerProfile(
            id = "vol_meera",
            name = "Meera Nair",
            age = 23,
            city = "Kochi",
            state = "Kerala",
            languages = listOf("English", "Malayalam", "Tamil"),
            bio = "Software designer who loves creating stress-free, slow-paced learning sessions for seniors on WhatsApp privacy settings.",
            specialties = listOf("WhatsApp Privacy Settings", "Avoiding Remote Apps", "Family Group Safety"),
            rating = 5.0f,
            sessionsCompleted = 39,
            verifiedBadge = "Senior Care Advocate",
            availableDays = "Wednesday & Sunday (11 AM - 3 PM)"
        )
    )

    val communityGuidelines: List<String> = listOf(
        "Volunteers will NEVER ask for money, fees, or gifts.",
        "Volunteers will NEVER ask for your OTP, UPI PIN, bank password, or card CVV.",
        "Volunteers will NEVER ask you to install remote access apps (like AnyDesk or TeamViewer).",
        "Sessions are purely educational. Volunteers cannot perform financial transactions on your behalf.",
        "Report any suspicious behavior immediately via the in-app Report button."
    )
}
