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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
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
import com.example.data.model.StringsDictionary
import com.example.ui.AppScreen
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
import com.example.ui.theme.PeachSecondary
import com.example.ui.theme.PeachSubtle
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseLight
import com.example.ui.theme.YellowSubtle

@Composable
fun PauseBeforeYouPayScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code
    val answers by viewModel.payCheckAnswers.collectAsState()

    val scrollState = rememberScrollState()

    // 5 core questions
    val questions = listOf(
        Pair(StringsDictionary.getString(langCode, "q1"), false), // No -> high risk
        Pair(StringsDictionary.getString(langCode, "q2"), true), // Is asking for unusual reason?
        Pair(StringsDictionary.getString(langCode, "q3"), true), // Yes -> high risk
        Pair(StringsDictionary.getString(langCode, "q4"), true), // Yes -> critical stop!
        Pair(StringsDictionary.getString(langCode, "q5"), false) // No -> warning
    )

    // Calculate risk
    val answeredCount = answers.size
    val hasOtpOrPinRequest = answers[3] == true
    val hasUrgencyOrThreat = answers[2] == true
    val unknownRecipient = answers[0] == false
    val notVerifiedKnown = answers[4] == false

    val isHighRisk = hasOtpOrPinRequest || (hasUrgencyOrThreat && unknownRecipient)
    val isCaution = !isHighRisk && (unknownRecipient || notVerifiedKnown || hasUrgencyOrThreat)
    val isSafe = answeredCount == 5 && !isHighRisk && !isCaution

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
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
                        .testTag("pay_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepPurple
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Payment Safety Check",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }

            AudioSpeakerButton(
                onClick = {
                    val speech = "Wait! Let's check before you pay. A UPI PIN is only needed to send money. Never enter a UPI PIN to receive money."
                    viewModel.voiceManager.speak(speech, prefs.language.localeTag)
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Title Card
        AccessibleCard(
            backgroundColor = PeachSubtle,
            borderColor = PeachSecondary
        ) {
            Text(
                text = StringsDictionary.getString(langCode, "pay_guard_title"),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                color = DarkInk
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = StringsDictionary.getString(langCode, "pay_guard_intro"),
                style = MaterialTheme.typography.bodyMedium,
                color = DarkInk.copy(alpha = 0.85f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // CRITICAL Golden Rule Banner
        AccessibleCard(
            backgroundColor = YellowSubtle,
            borderColor = ButterYellow
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = ButterYellowDarkText,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = StringsDictionary.getString(langCode, "upi_golden_rule"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ButterYellowDarkText
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 5 Questions Checklist
        questions.forEachIndexed { index, (questionText, _) ->
            val currentAnswer = answers[index]

            AccessibleCard(
                backgroundColor = Color.White
            ) {
                Text(
                    text = questionText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DarkInk
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // YES Button
                    AnswerOptionButton(
                        text = StringsDictionary.getString(langCode, "ans_yes"),
                        isSelected = currentAnswer == true,
                        onClick = { viewModel.setPayCheckAnswer(index, true) },
                        modifier = Modifier.weight(1f),
                        testTag = "pay_q_${index}_yes"
                    )

                    // NO Button
                    AnswerOptionButton(
                        text = StringsDictionary.getString(langCode, "ans_no"),
                        isSelected = currentAnswer == false,
                        onClick = { viewModel.setPayCheckAnswer(index, false) },
                        modifier = Modifier.weight(1f),
                        testTag = "pay_q_${index}_no"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Actionable Recommendation Card
        if (answeredCount > 0) {
            AccessibleCard(
                backgroundColor = when {
                    isHighRisk -> WarningRoseLight
                    isCaution -> YellowSubtle
                    else -> MintSubtle
                },
                borderColor = when {
                    isHighRisk -> WarningRose
                    isCaution -> ButterYellow
                    else -> MintAccent
                }
            ) {
                val (verdictTitle, verdictDesc, icon, titleColor) = when {
                    isHighRisk -> Quad(
                        StringsDictionary.getString(langCode, "verdict_stop"),
                        "Critical danger detected! You are likely being scammed. Do NOT enter your UPI PIN, do NOT scan any QR code, and do NOT download AnyDesk.",
                        Icons.Default.Warning,
                        WarningRose
                    )
                    isCaution -> Quad(
                        StringsDictionary.getString(langCode, "verdict_caution"),
                        "Warning: You do not have verified confirmation. Call the person on their known phone number or speak with a family member first.",
                        Icons.Default.Info,
                        ButterYellowDarkText
                    )
                    else -> Quad(
                        StringsDictionary.getString(langCode, "verdict_safe"),
                        "All safety checks passed. Proceed safely, but double check the recipient's name on your bank screen before typing your PIN.",
                        Icons.Default.CheckCircle,
                        MintDarkText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = titleColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = verdictTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = titleColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = verdictDesc,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = DarkInk
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AccessibleButton(
                text = "Reset Questionnaire",
                icon = Icons.Default.Refresh,
                isSecondary = true,
                onClick = { viewModel.resetPayCheck() },
                testTag = "pay_reset_button"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun AnswerOptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) DeepPurple else LavenderSubtle)
            .border(1.5.dp, if (isSelected) DeepPurple else LavenderPrimary, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (isSelected) Color.White else DeepPurple
        )
    }
}
