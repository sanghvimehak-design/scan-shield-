package com.example.domain.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

private const val TAG = "VoiceAssistant"

class VoiceAssistantManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isTtsReady = false

    private var speechRecognizer: SpeechRecognizer? = null
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _spokenText = MutableStateFlow("")
    val spokenText: StateFlow<String> = _spokenText.asStateFlow()

    private val _assistantReply = MutableStateFlow("Namaste! Tap the microphone to ask me anything about safety.")
    val assistantReply: StateFlow<String> = _assistantReply.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private var currentSpeed: Float = 0.95f

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                val result = engine.setLanguage(Locale("en", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.language = Locale.ENGLISH
                }
                engine.setSpeechRate(currentSpeed)
                isTtsReady = true
                Log.d(TAG, "TextToSpeech initialized successfully.")
            }
        } else {
            Log.e(TAG, "TextToSpeech initialization failed.")
        }
    }

    fun setSpeechSpeed(speed: Float) {
        currentSpeed = speed
        if (isTtsReady) {
            tts?.setSpeechRate(speed)
        }
    }

    fun speak(text: String, languageTag: String = "en-IN") {
        if (!isTtsReady) return
        try {
            val locale = Locale.forLanguageTag(languageTag)
            tts?.let { engine ->
                val langResult = engine.setLanguage(locale)
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.language = Locale.ENGLISH
                }
                engine.setSpeechRate(currentSpeed)
                _isSpeaking.value = true
                engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SCAN_SHIELD_TTS_${System.currentTimeMillis()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "TTS speak failed", e)
        }
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun startListening(onResult: (String) -> Unit) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _assistantReply.value = "Speech recognition is not available on this device. You can type or use the quick buttons below."
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _isListening.value = true
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _isListening.value = false
                    }

                    override fun onError(error: Int) {
                        _isListening.value = false
                        Log.w(TAG, "Speech recognition error code: $error")
                    }

                    override fun onResults(results: Bundle?) {
                        _isListening.value = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotBlank()) {
                            _spokenText.value = text
                            handleVoiceCommand(text, onResult)
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Scan Shield...")
            }
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start speech recognition", e)
            _isListening.value = false
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
    }

    fun handleVoiceCommand(query: String, onNavigate: (String) -> Unit) {
        val q = query.lowercase(Locale.ROOT)
        val reply = when {
            q.contains("fake") || q.contains("scam") || q.contains("check") -> {
                onNavigate("SCAN")
                "Opening the AI Scam Scanner for you. You can upload a screenshot or paste the message."
            }
            q.contains("payment") || q.contains("pay") || q.contains("send money") || q.contains("upi") -> {
                onNavigate("PAY_CHECK")
                "Opening the 'Pause Before You Pay' guardian. Remember: A UPI PIN is only used to send money, never to receive."
            }
            q.contains("contact") || q.contains("call family") || q.contains("emergency") || q.contains("sos") -> {
                onNavigate("EMERGENCY")
                "Opening the Scam Emergency Centre. You can call your trusted family contact or dial cyber helpline 1930."
            }
            q.contains("report") || q.contains("1930") || q.contains("cybercrime") -> {
                onNavigate("EMERGENCY")
                "To report cyber financial fraud, dial national helpline 1930 or visit cybercrime.gov.in."
            }
            q.contains("quiz") || q.contains("learn") || q.contains("tip") -> {
                onNavigate("LEARN")
                "Opening the Learning Centre. You can practice with 12 interactive safety quizzes."
            }
            else -> {
                "I heard: '$query'. I am your digital safety guardian. I can help you scan screenshots, verify payments, or connect to helpline 1930."
            }
        }
        _assistantReply.value = reply
        speak(reply)
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}
