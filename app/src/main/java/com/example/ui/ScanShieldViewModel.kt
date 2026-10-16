package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.DailySafetyTip
import com.example.data.model.HelpRequest
import com.example.data.model.QuizRepository
import com.example.data.model.QuizScenario
import com.example.data.model.RiskLevel
import com.example.data.model.SafetyData
import com.example.data.model.ScanRecord
import com.example.data.model.TextScale
import com.example.data.model.ThreatPreset
import com.example.data.model.TrustedContact
import com.example.data.model.VolunteerData
import com.example.data.model.VolunteerProfile
import com.example.data.repository.ContactRepository
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.ScanRepository
import com.example.data.repository.UserPreferences
import com.example.domain.analyzer.AnalysisResult
import com.example.domain.analyzer.GeminiScanAnalyzer
import com.example.domain.analyzer.HeuristicScamDetector
import com.example.domain.voice.VoiceAssistantManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    SPLASH,
    ONBOARDING,
    HOME,
    SCANNER,
    SCAN_RESULT,
    PAY_CHECK,
    EMERGENCY_SOS,
    VOICE_ASSISTANT,
    LEARN,
    QUIZ_RUNNER,
    VOLUNTEER,
    HISTORY,
    CONTACTS,
    SETTINGS
}

class ScanShieldViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val scanRepo = ScanRepository(db.scanDao())
    private val contactRepo = ContactRepository(db.contactDao())
    private val prefsRepo = PreferencesRepository(application)
    private val geminiAnalyzer = GeminiScanAnalyzer()

    val voiceManager = VoiceAssistantManager(application)

    val preferences: StateFlow<UserPreferences> = prefsRepo.preferences

    val allScans: StateFlow<List<ScanRecord>> = scanRepo.allScans.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalScansCount: StateFlow<Int> = scanRepo.totalScansCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalThreatsCount: StateFlow<Int> = scanRepo.totalThreatsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val allContacts: StateFlow<List<TrustedContact>> = contactRepo.allContacts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val primaryContact: StateFlow<TrustedContact?> = contactRepo.primaryContact.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Navigation state
    private val screenStack = mutableListOf<AppScreen>()
    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Daily Safety Tip
    private val _dailyTipIndex = MutableStateFlow(0)
    val currentDailyTip: DailySafetyTip
        get() = SafetyData.dailyTips[_dailyTipIndex.value % SafetyData.dailyTips.size]

    // Scanner state
    private val _scanInputText = MutableStateFlow("")
    val scanInputText: StateFlow<String> = _scanInputText.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _lastAnalysisResult = MutableStateFlow<AnalysisResult?>(null)
    val lastAnalysisResult: StateFlow<AnalysisResult?> = _lastAnalysisResult.asStateFlow()

    private val _selectedRecordForDetail = MutableStateFlow<ScanRecord?>(null)
    val selectedRecordForDetail: StateFlow<ScanRecord?> = _selectedRecordForDetail.asStateFlow()

    // Pause Before You Pay State
    private val _payCheckAnswers = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val payCheckAnswers: StateFlow<Map<Int, Boolean>> = _payCheckAnswers.asStateFlow()

    // Quiz Runner State
    private val _activeQuizScenario = MutableStateFlow<QuizScenario?>(null)
    val activeQuizScenario: StateFlow<QuizScenario?> = _activeQuizScenario.asStateFlow()

    private val _selectedQuizOption = MutableStateFlow<Int?>(null)
    val selectedQuizOption: StateFlow<Int?> = _selectedQuizOption.asStateFlow()

    private val _quizSubmitted = MutableStateFlow(false)
    val quizSubmitted: StateFlow<Boolean> = _quizSubmitted.asStateFlow()

    private val _completedQuizIds = MutableStateFlow<Set<String>>(emptySet())
    val completedQuizIds: StateFlow<Set<String>> = _completedQuizIds.asStateFlow()

    // Volunteer help sessions booked
    private val _bookedSessions = MutableStateFlow<List<HelpRequest>>(emptyList())
    val bookedSessions: StateFlow<List<HelpRequest>> = _bookedSessions.asStateFlow()

    init {
        // Sync initial speech speed
        voiceManager.setSpeechSpeed(preferences.value.speechSpeed)
    }

    // Navigation methods
    fun navigate(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun goBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            val prev = screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = prev
            return true
        }
        return false
    }

    // Settings actions
    fun setLanguage(lang: AppLanguage) {
        prefsRepo.setLanguage(lang)
    }

    fun setTextScale(scale: TextScale) {
        prefsRepo.setTextScale(scale)
    }

    fun setHighContrast(enabled: Boolean) {
        prefsRepo.setHighContrast(enabled)
    }

    fun setDarkTheme(enabled: Boolean) {
        prefsRepo.setDarkTheme(enabled)
    }

    fun setSpeechSpeed(speed: Float) {
        prefsRepo.setSpeechSpeed(speed)
        voiceManager.setSpeechSpeed(speed)
    }

    fun completeOnboarding() {
        prefsRepo.setOnboardingCompleted(true)
        navigate(AppScreen.HOME)
    }

    fun nextTip() {
        _dailyTipIndex.value = (_dailyTipIndex.value + 1) % SafetyData.dailyTips.size
    }

    fun readCurrentTipAloud() {
        val tip = currentDailyTip
        val lang = preferences.value.language.localeTag
        voiceManager.speak("${tip.title}. ${tip.detailedAdvice}", lang)
    }

    // Scanner actions
    fun setScanInputText(text: String) {
        _scanInputText.value = text
    }

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun loadPreset(preset: ThreatPreset) {
        _scanInputText.value = preset.rawContent
        _selectedImageUri.value = null
        val localResult = HeuristicScamDetector.analyze(preset.rawContent, preset.senderOrSource)
        _lastAnalysisResult.value = localResult
        navigate(AppScreen.SCAN_RESULT)
    }

    fun runAnalysis(bitmap: Bitmap? = null) {
        val text = _scanInputText.value
        if (text.isBlank() && bitmap == null) return

        viewModelScope.launch {
            _isScanning.value = true
            try {
                val result = geminiAnalyzer.analyzeScreenshotOrText(
                    text = text,
                    bitmap = bitmap,
                    languageName = preferences.value.language.displayName
                )
                _lastAnalysisResult.value = result
                navigate(AppScreen.SCAN_RESULT)
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun saveCurrentScan() {
        val result = _lastAnalysisResult.value ?: return
        val text = _scanInputText.value
        viewModelScope.launch {
            scanRepo.saveScan(
                ScanRecord(
                    sourceType = if (_selectedImageUri.value != null) "SCREENSHOT" else "MESSAGE",
                    title = result.title,
                    inputText = text.ifBlank { "Screenshot Analyzed" },
                    imageUri = _selectedImageUri.value?.toString(),
                    riskLevel = result.riskLevel,
                    confidenceScore = result.confidenceScore,
                    summaryExplanation = result.summaryExplanation,
                    evidenceList = result.evidenceList.joinToString("|"),
                    recommendedActions = result.recommendedActions.joinToString("|"),
                    safeVerificationGuide = result.safeVerificationGuide,
                    languageCode = preferences.value.language.code
                )
            )
        }
    }

    fun deleteScan(id: Long) {
        viewModelScope.launch {
            scanRepo.deleteScan(id)
            if (_selectedRecordForDetail.value?.id == id) {
                _selectedRecordForDetail.value = null
            }
        }
    }

    fun viewScanDetail(record: ScanRecord) {
        _selectedRecordForDetail.value = record
        _lastAnalysisResult.value = AnalysisResult(
            riskLevel = record.riskLevel,
            confidenceScore = record.confidenceScore,
            title = record.title,
            summaryExplanation = record.summaryExplanation,
            evidenceList = record.evidenceList.split("|").filter { it.isNotBlank() },
            recommendedActions = record.recommendedActions.split("|").filter { it.isNotBlank() },
            safeVerificationGuide = record.safeVerificationGuide
        )
        navigate(AppScreen.SCAN_RESULT)
    }

    // Pay Check actions
    fun setPayCheckAnswer(questionIndex: Int, answer: Boolean) {
        val updated = _payCheckAnswers.value.toMutableMap()
        updated[questionIndex] = answer
        _payCheckAnswers.value = updated
    }

    fun resetPayCheck() {
        _payCheckAnswers.value = emptyMap()
    }

    // Contacts actions
    fun addTrustedContact(name: String, relationship: String, phone: String, isPrimary: Boolean) {
        viewModelScope.launch {
            contactRepo.addContact(
                TrustedContact(
                    name = name,
                    relationship = relationship,
                    phoneNumber = phone,
                    isPrimary = isPrimary
                )
            )
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            contactRepo.deleteContact(id)
        }
    }

    // Quiz actions
    fun startQuiz(scenario: QuizScenario) {
        _activeQuizScenario.value = scenario
        _selectedQuizOption.value = null
        _quizSubmitted.value = false
        navigate(AppScreen.QUIZ_RUNNER)
    }

    fun selectQuizOption(index: Int) {
        if (!_quizSubmitted.value) {
            _selectedQuizOption.value = index
        }
    }

    fun submitQuizAnswer() {
        _quizSubmitted.value = true
        val scenario = _activeQuizScenario.value ?: return
        if (_selectedQuizOption.value == scenario.correctIndex) {
            _completedQuizIds.value = _completedQuizIds.value + scenario.id
        }
    }

    // Volunteer actions
    fun bookSession(volunteer: VolunteerProfile, topic: String, date: String, slot: String) {
        val newSession = HelpRequest(
            id = UUID.randomUUID().toString(),
            volunteerId = volunteer.id,
            volunteerName = volunteer.name,
            topic = topic,
            date = date,
            timeSlot = slot,
            status = "Confirmed"
        )
        _bookedSessions.value = _bookedSessions.value + newSession
    }

    fun clearAllData() {
        viewModelScope.launch {
            scanRepo.clearHistory()
            contactRepo.clearAll()
            prefsRepo.clearAll()
            _bookedSessions.value = emptyList()
            _completedQuizIds.value = emptySet()
            navigate(AppScreen.HOME)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.destroy()
    }
}
