package com.example.data.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val localeTag: String
) {
    ENGLISH("en", "English", "English", "en-IN"),
    HINDI("hi", "Hindi", "हिंदी", "hi-IN"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી", "gu-IN"),
    TAMIL("ta", "Tamil", "தமிழ்", "ta-IN"),
    BENGALI("bn", "Bengali", "বাংলা", "bn-IN"),
    MARATHI("mr", "Marathi", "मराठी", "mr-IN"),
    TELUGU("te", "Telugu", "తెలుగు", "te-IN"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ", "kn-IN"),
    MALAYALAM("ml", "Malayalam", "മലയാളം", "ml-IN");

    companion object {
        fun fromCode(code: String): AppLanguage =
            entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
    }
}

enum class TextScale(val label: String, val scaleFactor: Float) {
    NORMAL("Normal", 1.0f),
    LARGE("Large", 1.2f),
    EXTRA_LARGE("Extra Large", 1.4f)
}
