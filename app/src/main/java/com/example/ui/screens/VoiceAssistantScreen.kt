package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.AppScreen
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleCard
import com.example.ui.components.MascotBadge
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.MintAccent
import com.example.ui.theme.WarningRose

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceAssistantScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val voice = viewModel.voiceManager
    val isListening by voice.isListening.collectAsState()
    val isSpeaking by voice.isSpeaking.collectAsState()
    val spokenText by voice.spokenText.collectAsState()
    val replyText by voice.assistantReply.collectAsState()
    val prefs by viewModel.preferences.collectAsState()

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) {
            voice.startListening { target ->
                handleVoiceNavigation(target, viewModel)
            }
        }
    }

    val pulseTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isListening) 1.25f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val scrollState = rememberScrollState()

    val quickCommands = listOf(
        "Is this message fake?",
        "Scan this screenshot",
        "Help me verify a payment",
        "Call my trusted contact",
        "How can I report cyber fraud?",
        "Explain Jeevan Pramaan rules"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.goBack() },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LavenderSubtle)
                        .testTag("voice_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepPurple
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Scan Shield Voice Guide",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }

            MascotBadge(size = 46.dp, animated = isSpeaking)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Mic Button with animated pulse
        Box(
            modifier = Modifier
                .size(140.dp)
                .scale(if (isListening) pulseScale else 1.0f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(if (isListening) WarningRose.copy(alpha = 0.25f) else LavenderSubtle)
            )

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(if (isListening) WarningRose else DeepPurple)
                    .border(3.dp, if (isListening) WarningRose else LavenderPrimary, CircleShape)
                    .clickable {
                        if (isListening) {
                            voice.stopListening()
                        } else {
                            if (hasAudioPermission) {
                                voice.startListening { target ->
                                    handleVoiceNavigation(target, viewModel)
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    }
                    .testTag("large_voice_mic_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = if (isListening) "Stop Listening" else "Start Speaking",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (isListening) "Listening... speak clearly" else "Tap the microphone to speak",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (isListening) WarningRose else DeepPurple
        )

        if (spokenText.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You asked: \"$spokenText\"",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = DarkInk.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Spoken Response Card with Audio Controls
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scan Shield's Spoken Answer:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )

                Row {
                    // Repeat audio button
                    IconButton(
                        onClick = { voice.speak(replyText, prefs.language.localeTag) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LavenderSubtle)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Repeat",
                            tint = DeepPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Stop audio button
                    IconButton(
                        onClick = { voice.stopSpeaking() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LavenderSubtle)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = WarningRose,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = replyText,
                style = MaterialTheme.typography.bodyLarge,
                color = DarkInk
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Command Suggestions
        Text(
            text = "Or Tap a Common Question:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickCommands.forEach { command ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(LavenderSubtle)
                        .border(1.dp, LavenderPrimary, RoundedCornerShape(16.dp))
                        .clickable {
                            voice.handleVoiceCommand(command) { target ->
                                handleVoiceNavigation(target, viewModel)
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💬 $command",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = DeepPurple
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

private fun handleVoiceNavigation(target: String, viewModel: ScanShieldViewModel) {
    when (target) {
        "SCAN" -> viewModel.navigate(AppScreen.SCANNER)
        "PAY_CHECK" -> viewModel.navigate(AppScreen.PAY_CHECK)
        "EMERGENCY" -> viewModel.navigate(AppScreen.EMERGENCY_SOS)
        "LEARN" -> viewModel.navigate(AppScreen.LEARN)
    }
}
