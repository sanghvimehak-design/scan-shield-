package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccessibleCard
import com.example.ui.components.AudioSpeakerButton
import com.example.ui.theme.ButterYellow
import com.example.ui.theme.ButterYellowDarkText
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintDarkText
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseLight
import com.example.ui.theme.YellowSubtle

@Composable
fun QuizRunnerScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val scenario by viewModel.activeQuizScenario.collectAsState()
    val selectedOption by viewModel.selectedQuizOption.collectAsState()
    val isSubmitted by viewModel.quizSubmitted.collectAsState()
    val prefs by viewModel.preferences.collectAsState()

    val scrollState = rememberScrollState()

    if (scenario == null) {
        viewModel.goBack()
        return
    }

    val s = scenario!!
    val isCorrect = selectedOption == s.correctIndex

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Row
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
                        .testTag("quiz_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepPurple
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = s.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepPurple
                    )
                    Text(
                        text = s.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkInk.copy(alpha = 0.7f)
                    )
                }
            }

            AudioSpeakerButton(
                onClick = {
                    val speech = "${s.story}. ${s.question}"
                    viewModel.voiceManager.speak(speech, prefs.language.localeTag)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scenario Story Card
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Text(
                text = "The Situation:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = s.story,
                style = MaterialTheme.typography.bodyLarge,
                color = DarkInk
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Question
        AccessibleCard(
            backgroundColor = LavenderSubtle,
            borderColor = LavenderPrimary
        ) {
            Text(
                text = s.question,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options List
        s.options.forEachIndexed { index, optionText ->
            val isSelected = selectedOption == index
            val (optionBg, optionBorder, optionTextColor) = when {
                !isSubmitted && isSelected -> Triple(LavenderSubtle, DeepPurple, DeepPurple)
                isSubmitted && index == s.correctIndex -> Triple(MintSubtle, MintAccent, MintDarkText)
                isSubmitted && isSelected && !isCorrect -> Triple(WarningRoseLight, WarningRose, WarningRose)
                else -> Triple(Color.White, Color(0xFFE0E0E0), DarkInk)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(optionBg)
                    .border(1.5.dp, optionBorder, RoundedCornerShape(20.dp))
                    .clickable(enabled = !isSubmitted) {
                        viewModel.selectQuizOption(index)
                    }
                    .padding(16.dp)
                    .testTag("quiz_option_$index")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected || (isSubmitted && index == s.correctIndex)) DeepPurple else LavenderSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A'.code + index).toChar().toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected || (isSubmitted && index == s.correctIndex)) Color.White else DeepPurple
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (isSelected || (isSubmitted && index == s.correctIndex)) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = optionTextColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Submit or Explanation
        if (!isSubmitted) {
            AccessibleButton(
                text = "Submit Answer",
                enabled = selectedOption != null,
                onClick = { viewModel.submitQuizAnswer() },
                testTag = "quiz_submit_button"
            )
        } else {
            // Outcome Explanation Card
            AccessibleCard(
                backgroundColor = if (isCorrect) MintSubtle else WarningRoseLight,
                borderColor = if (isCorrect) MintAccent else WarningRose
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCorrect) MintDarkText else WarningRose,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isCorrect) "Excellent! Correct Answer 🎉" else "Not quite, but let's learn! 💡",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isCorrect) MintDarkText else WarningRose
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = s.explanation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkInk
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Practical safety tip
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = ButterYellowDarkText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Safety Tip: ${s.safetyTip}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = DarkInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AccessibleButton(
                text = "Return to Learning Centre",
                onClick = { viewModel.goBack() },
                testTag = "quiz_finish_button"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
