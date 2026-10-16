package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.components.RiskBadge
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseLight

@Composable
fun ScanResultScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code
    val result = viewModel.lastAnalysisResult.collectAsState().value
    val selectedRecord = viewModel.selectedRecordForDetail.collectAsState().value

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Back Row
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
                        .testTag("result_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back",
                        tint = DeepPurple
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Scan Analysis Result",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }

            if (result != null) {
                AudioSpeakerButton(
                    onClick = {
                        val speech = "${result.title}. ${result.summaryExplanation}. Recommendation: ${result.recommendedActions.joinToString(", ")}"
                        viewModel.voiceManager.speak(speech, prefs.language.localeTag)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (result != null) {
            // Prominent Risk Badge Banner
            AccessibleCard(
                backgroundColor = Color.White
            ) {
                RiskBadge(riskLevel = result.riskLevel)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = DarkInk
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Evidence Breakdown
            AccessibleCard(
                backgroundColor = if (result.riskLevel == com.example.data.model.RiskLevel.HIGH_RISK) WarningRoseLight else LavenderSubtle
            ) {
                Text(
                    text = StringsDictionary.getString(langCode, "evidence_found"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (result.riskLevel == com.example.data.model.RiskLevel.HIGH_RISK) WarningRose else DeepPurple
                )
                Spacer(modifier = Modifier.height(8.dp))
                result.evidenceList.forEach { point ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "• ", fontWeight = FontWeight.Bold, color = DarkInk)
                        Text(
                            text = point,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = DarkInk
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Simple Plain-Language Explanation
            AccessibleCard(
                backgroundColor = Color.White
            ) {
                Text(
                    text = StringsDictionary.getString(langCode, "simple_explanation"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = result.summaryExplanation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkInk
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Recommended Actions
            AccessibleCard(
                backgroundColor = MintSubtle,
                borderColor = MintAccent
            ) {
                Text(
                    text = StringsDictionary.getString(langCode, "recommended_steps"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(8.dp))
                result.recommendedActions.forEach { action ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "✔ ", fontWeight = FontWeight.Bold, color = DeepPurple)
                        Text(
                            text = action,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = DarkInk
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Safe Verification Guide
            AccessibleCard(
                backgroundColor = Color.White
            ) {
                Text(
                    text = StringsDictionary.getString(langCode, "safe_verification"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = result.safeVerificationGuide,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkInk
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save / Action Buttons
            if (selectedRecord == null) {
                AccessibleButton(
                    text = StringsDictionary.getString(langCode, "save_scan"),
                    icon = Icons.Default.Bookmark,
                    onClick = {
                        viewModel.saveCurrentScan()
                        Toast.makeText(context, StringsDictionary.getString(langCode, "saved_toast"), Toast.LENGTH_SHORT).show()
                    },
                    testTag = "save_scan_button"
                )
                Spacer(modifier = Modifier.height(10.dp))
            } else {
                AccessibleButton(
                    text = StringsDictionary.getString(langCode, "delete_scan"),
                    icon = Icons.Default.Delete,
                    isDanger = true,
                    onClick = {
                        viewModel.deleteScan(selectedRecord.id)
                        Toast.makeText(context, StringsDictionary.getString(langCode, "deleted_toast"), Toast.LENGTH_SHORT).show()
                        viewModel.goBack()
                    },
                    testTag = "delete_scan_button"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            AccessibleButton(
                text = "Check Another Message",
                icon = Icons.Default.Refresh,
                isSecondary = true,
                onClick = {
                    viewModel.setScanInputText("")
                    viewModel.setSelectedImageUri(null)
                    viewModel.navigate(AppScreen.SCANNER)
                },
                testTag = "check_another_button"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
