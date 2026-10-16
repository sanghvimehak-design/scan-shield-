package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.StringsDictionary
import com.example.data.model.TextScale
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccessibleCard
import com.example.ui.components.MascotBadge
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintSubtle
import com.example.ui.theme.WarmCream

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val prefs by viewModel.preferences.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        MascotBadge(size = 96.dp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to Scan Shield 💜",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Let's make sure the text and language are comfortable for your eyes before we start.",
            style = MaterialTheme.typography.bodyLarge,
            color = DarkInk,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1. Language Selection Card
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Text(
                text = "1. Choose Your Preferred Language",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = prefs.language == lang
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) DeepPurple else LavenderSubtle)
                            .border(
                                1.5.dp,
                                if (isSelected) DeepPurple else LavenderPrimary,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.setLanguage(lang) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("onboarding_lang_${lang.code}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = "${lang.displayName} (${lang.nativeName})",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) Color.White else DarkInk
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Text Size Selection Card
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Text(
                text = "2. Select Comfortable Text Size",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextScale.entries.forEach { scale ->
                    val isSelected = prefs.textScale == scale
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) DeepPurple else LavenderSubtle)
                            .border(
                                1.5.dp,
                                if (isSelected) DeepPurple else LavenderPrimary,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { viewModel.setTextScale(scale) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Aa",
                                style = when (scale) {
                                    TextScale.NORMAL -> MaterialTheme.typography.titleMedium
                                    TextScale.LARGE -> MaterialTheme.typography.titleLarge
                                    TextScale.EXTRA_LARGE -> MaterialTheme.typography.headlineMedium
                                },
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) Color.White else DeepPurple
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scale.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Color.White else DarkInk
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Three Promises Card
        AccessibleCard(
            backgroundColor = MintSubtle,
            borderColor = MintAccent
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = DeepPurple,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Our Safety Promises to You",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• We never ask for your bank password or UPI PIN.\n• All screenshot scans stay 100% private on your phone.\n• When in doubt, you can always dial 1930 with one tap.",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkInk
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        AccessibleButton(
            text = "Start Using Scan Shield 💜",
            onClick = { viewModel.completeOnboarding() },
            testTag = "onboarding_start_button"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
