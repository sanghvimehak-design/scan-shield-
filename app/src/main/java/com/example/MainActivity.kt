package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.ui.AppScreen
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.ScanShieldBottomBar
import com.example.ui.components.ScanShieldTopBar
import com.example.ui.screens.EmergencySosScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearningScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PauseBeforeYouPayScreen
import com.example.ui.screens.QuizRunnerScreen
import com.example.ui.screens.ScanResultScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.VoiceAssistantScreen
import com.example.ui.screens.VolunteerScreen
import com.example.ui.theme.ScanShieldTheme
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {

    private val viewModel: ScanShieldViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Firebase App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            val prefs by viewModel.preferences.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            // Global Text Size Scaling for accessibility
            val currentDensity = LocalDensity.current
            val scaledDensity = Density(
                density = currentDensity.density,
                fontScale = currentDensity.fontScale * prefs.textScale.scaleFactor
            )

            CompositionLocalProvider(LocalDensity provides scaledDensity) {
                ScanShieldTheme(
                    darkTheme = prefs.isDarkTheme,
                    isHighContrast = prefs.isHighContrast
                ) {
                    val showBars = currentScreen != AppScreen.SPLASH && currentScreen != AppScreen.ONBOARDING

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (showBars) {
                                ScanShieldTopBar(
                                    currentLanguage = prefs.language,
                                    onLanguageSelected = { viewModel.setLanguage(it) },
                                    onSosClick = { viewModel.navigate(AppScreen.EMERGENCY_SOS) },
                                    onHomeClick = { viewModel.navigate(AppScreen.HOME) }
                                )
                            }
                        },
                        bottomBar = {
                            if (showBars) {
                                ScanShieldBottomBar(
                                    currentScreen = currentScreen,
                                    currentLangCode = prefs.language.code,
                                    onNavigate = { viewModel.navigate(it) }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            // Back navigation handler
                            BackHandler(enabled = currentScreen != AppScreen.HOME && currentScreen != AppScreen.SPLASH) {
                                viewModel.goBack()
                            }

                            Crossfade(targetState = currentScreen, label = "screen_crossfade") { screen ->
                                when (screen) {
                                    AppScreen.SPLASH -> SplashScreen(viewModel = viewModel)
                                    AppScreen.ONBOARDING -> OnboardingScreen(viewModel = viewModel)
                                    AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                                    AppScreen.SCANNER -> ScannerScreen(viewModel = viewModel)
                                    AppScreen.SCAN_RESULT -> ScanResultScreen(viewModel = viewModel)
                                    AppScreen.PAY_CHECK -> PauseBeforeYouPayScreen(viewModel = viewModel)
                                    AppScreen.EMERGENCY_SOS -> EmergencySosScreen(viewModel = viewModel)
                                    AppScreen.VOICE_ASSISTANT -> VoiceAssistantScreen(viewModel = viewModel)
                                    AppScreen.LEARN -> LearningScreen(viewModel = viewModel)
                                    AppScreen.QUIZ_RUNNER -> QuizRunnerScreen(viewModel = viewModel)
                                    AppScreen.VOLUNTEER -> VolunteerScreen(viewModel = viewModel)
                                    AppScreen.HISTORY -> HistoryScreen(viewModel = viewModel)
                                    AppScreen.CONTACTS -> SettingsScreen(viewModel = viewModel)
                                    AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
