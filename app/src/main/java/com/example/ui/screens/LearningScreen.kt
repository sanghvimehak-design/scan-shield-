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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.QuizRepository
import com.example.data.model.QuizScenario
import com.example.data.model.StringsDictionary
import com.example.data.model.UserBadge
import com.example.ui.AppScreen
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleCard
import com.example.ui.theme.ButterYellow
import com.example.ui.theme.CardBorderLavender
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintDarkText
import com.example.ui.theme.MintSubtle

@Composable
fun LearningScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code
    val completedQuizIds by viewModel.completedQuizIds.collectAsState()

    val totalScenarios = QuizRepository.scenarios.size
    val completedCount = completedQuizIds.size
    val progressPercent = if (totalScenarios > 0) completedCount.toFloat() / totalScenarios.toFloat() else 0f

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = StringsDictionary.getString(langCode, "learn_title"),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = StringsDictionary.getString(langCode, "learn_sub"),
            style = MaterialTheme.typography.bodyMedium,
            color = DarkInk.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Card
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Safety Learning Progress",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepPurple
                    )
                    Text(
                        text = "$completedCount of $totalScenarios Lessons Mastered",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MintSubtle),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = DeepPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = DeepPurple,
                trackColor = LavenderSubtle
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Collectible Badges
        Text(
            text = "Your Safety Badges",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(QuizRepository.defaultBadges) { badge ->
                val isUnlocked = badge.unlocked || completedCount >= 3
                BadgeCard(badge = badge, isUnlocked = isUnlocked)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 12 Scenario Quizzes
        Text(
            text = "12 Real Indian Scam Scenarios",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(10.dp))

        QuizRepository.scenarios.forEachIndexed { index, scenario ->
            val isCompleted = completedQuizIds.contains(scenario.id)
            ScenarioQuizRow(
                index = index + 1,
                scenario = scenario,
                isCompleted = isCompleted,
                onClick = { viewModel.startQuiz(scenario) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Offline Safety Pocket Guide Card
        AccessibleCard(
            backgroundColor = MintSubtle,
            borderColor = MintAccent
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MintDarkText,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Offline Safety Golden Rules",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MintDarkText
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "1. UPI PIN is only to PAY, never to receive.\n2. No such thing as 'Digital Arrest' over video calls.\n3. Never download AnyDesk or APK links sent via SMS.\n4. Call 1930 immediately if money was deducted.\n5. When in doubt, PAUSE and speak to family.",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkInk
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BadgeCard(
    badge: UserBadge,
    isUnlocked: Boolean
) {
    Box(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isUnlocked) LavenderSubtle else Color(0xFFF0F0F0))
            .border(1.5.dp, if (isUnlocked) LavenderPrimary else Color.LightGray, RoundedCornerShape(20.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) ButterYellow else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) DeepPurple else Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = badge.name,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isUnlocked) DeepPurple else Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ScenarioQuizRow(
    index: Int,
    scenario: QuizScenario,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    AccessibleCard(
        backgroundColor = if (isCompleted) MintSubtle else Color.White,
        borderColor = if (isCompleted) MintAccent else CardBorderLavender,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) MintAccent else LavenderSubtle),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = MintDarkText,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "$index",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = DeepPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = scenario.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DarkInk
                    )
                    Text(
                        text = scenario.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.7f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DeepPurple)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Quiz",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
