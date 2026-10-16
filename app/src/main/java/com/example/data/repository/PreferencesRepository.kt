package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppLanguage
import com.example.data.model.TextScale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserPreferences(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val textScale: TextScale = TextScale.NORMAL,
    val isHighContrast: Boolean = false,
    val isDarkTheme: Boolean = false,
    val speechSpeed: Float = 0.95f,
    val hasCompletedOnboarding: Boolean = false
)

class PreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("scan_shield_user_prefs", Context.MODE_PRIVATE)

    private val _preferences = MutableStateFlow(loadPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    private fun loadPreferences(): UserPreferences {
        val langCode = prefs.getString("key_language", "en") ?: "en"
        val scaleStr = prefs.getString("key_text_scale", "NORMAL") ?: "NORMAL"
        val highContrast = prefs.getBoolean("key_high_contrast", false)
        val darkTheme = prefs.getBoolean("key_dark_theme", false)
        val speechSpeed = prefs.getFloat("key_speech_speed", 0.95f)
        val onboarding = prefs.getBoolean("key_onboarding_completed", false)

        return UserPreferences(
            language = AppLanguage.fromCode(langCode),
            textScale = try { TextScale.valueOf(scaleStr) } catch (_: Exception) { TextScale.NORMAL },
            isHighContrast = highContrast,
            isDarkTheme = darkTheme,
            speechSpeed = speechSpeed,
            hasCompletedOnboarding = onboarding
        )
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString("key_language", language.code).apply()
        _preferences.value = _preferences.value.copy(language = language)
    }

    fun setTextScale(scale: TextScale) {
        prefs.edit().putString("key_text_scale", scale.name).apply()
        _preferences.value = _preferences.value.copy(textScale = scale)
    }

    fun setHighContrast(enabled: Boolean) {
        prefs.edit().putBoolean("key_high_contrast", enabled).apply()
        _preferences.value = _preferences.value.copy(isHighContrast = enabled)
    }

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean("key_dark_theme", enabled).apply()
        _preferences.value = _preferences.value.copy(isDarkTheme = enabled)
    }

    fun setSpeechSpeed(speed: Float) {
        prefs.edit().putFloat("key_speech_speed", speed).apply()
        _preferences.value = _preferences.value.copy(speechSpeed = speed)
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("key_onboarding_completed", completed).apply()
        _preferences.value = _preferences.value.copy(hasCompletedOnboarding = completed)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
        _preferences.value = UserPreferences()
    }
}
