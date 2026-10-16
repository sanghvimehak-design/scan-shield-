package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleButton
import com.example.ui.components.MascotBadge
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.MintAccent
import com.example.ui.theme.WarmCream
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showContent = true
        delay(2200)
        val hasOnboarded = viewModel.preferences.value.hasCompletedOnboarding
        if (hasOnboarded) {
            viewModel.navigate(AppScreen.HOME)
        } else {
            viewModel.navigate(AppScreen.ONBOARDING)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(WarmCream, LavenderSubtle, MintAccent.copy(alpha = 0.2f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn() + slideInVertically { it / 3 }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                MascotBadge(size = 140.dp)

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "SCAN SHIELD",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp,
                        letterSpacing = 1.sp
                    ),
                    color = DeepPurple,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Pause. Check. Stay Safe. 💜",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkInk
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "AI Digital Safety Guardian for Pensioners",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = DarkInk.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Fast bypass button for accessibility or quick start
                AccessibleButton(
                    text = "Enter Scan Shield →",
                    isSecondary = true,
                    onClick = {
                        val hasOnboarded = viewModel.preferences.value.hasCompletedOnboarding
                        if (hasOnboarded) {
                            viewModel.navigate(AppScreen.HOME)
                        } else {
                            viewModel.navigate(AppScreen.ONBOARDING)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 32.dp),
                    testTag = "splash_continue_button"
                )
            }
        }
    }
}
