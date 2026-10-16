package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.StringsDictionary
import com.example.ui.AppScreen
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleCard
import com.example.ui.components.AudioSpeakerButton
import com.example.ui.components.MascotBadge
import com.example.ui.components.RiskBadge
import com.example.ui.theme.ButterYellow
import com.example.ui.theme.ButterYellowDarkText
import com.example.ui.theme.CardBorderLavender
import com.example.ui.theme.CardSurfaceLight
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
fun HomeScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code

    val scans by viewModel.allScans.collectAsState()
    val scanCount by viewModel.totalScansCount.collectAsState()
    val threatCount by viewModel.totalThreatsCount.collectAsState()
    val primaryContact by viewModel.primaryContact.collectAsState()
    val contacts by viewModel.allContacts.collectAsState()

    val currentTip = viewModel.currentDailyTip
    val scrollState = rememberScrollState()

    var showCallConfirmDialog by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Welcoming Greeting
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MascotBadge(size = 64.dp, animated = true)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = StringsDictionary.getString(langCode, "greeting"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Text(
                    text = StringsDictionary.getString(langCode, "subtitle"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkInk.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Safety Tip Card
        AccessibleCard(
            backgroundColor = YellowSubtle,
            borderColor = ButterYellow
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ButterYellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💡", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = StringsDictionary.getString(langCode, "daily_tip_badge"),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = ButterYellowDarkText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AudioSpeakerButton(
                        onClick = { viewModel.readCurrentTipAloud() },
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Next Tip ↻",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepPurple,
                        modifier = Modifier
                            .clickable { viewModel.nextTip() }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = currentTip.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DarkInk
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentTip.detailedAdvice,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkInk.copy(alpha = 0.9f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Big Action Cards Grid
        Text(
            text = "Digital Safety Actions",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 1. Scan a Screenshot
        ActionCard(
            title = StringsDictionary.getString(langCode, "card_scan_screenshot"),
            subtitle = StringsDictionary.getString(langCode, "card_scan_screenshot_sub"),
            icon = Icons.Default.CameraAlt,
            backgroundColor = LavenderSubtle,
            borderColor = LavenderPrimary,
            iconTint = DeepPurple,
            onClick = { viewModel.navigate(AppScreen.SCANNER) },
            testTag = "home_card_scan_screenshot"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Check Suspicious Message
        ActionCard(
            title = StringsDictionary.getString(langCode, "card_check_message"),
            subtitle = StringsDictionary.getString(langCode, "card_check_message_sub"),
            icon = Icons.Default.Info,
            backgroundColor = MintSubtle,
            borderColor = MintAccent,
            iconTint = MintDarkText,
            onClick = { viewModel.navigate(AppScreen.SCANNER) },
            testTag = "home_card_check_message"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Pause Before You Pay
        ActionCard(
            title = StringsDictionary.getString(langCode, "card_pause_pay"),
            subtitle = StringsDictionary.getString(langCode, "card_pause_pay_sub"),
            icon = Icons.Default.PauseCircle,
            backgroundColor = PeachSubtle,
            borderColor = PeachSecondary,
            iconTint = DeepPurple,
            onClick = { viewModel.navigate(AppScreen.PAY_CHECK) },
            testTag = "home_card_pause_pay"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Talk to Scan Shield
        ActionCard(
            title = StringsDictionary.getString(langCode, "card_voice_assist"),
            subtitle = StringsDictionary.getString(langCode, "card_voice_assist_sub"),
            icon = Icons.Default.Mic,
            backgroundColor = LavenderSubtle,
            borderColor = LavenderPrimary,
            iconTint = DeepPurple,
            onClick = { viewModel.navigate(AppScreen.VOICE_ASSISTANT) },
            testTag = "home_card_voice_assist"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Primary Trusted Contact Card
        if (primaryContact != null) {
            AccessibleCard(
                backgroundColor = Color.White,
                borderColor = CardBorderLavender
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(LavenderSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContactPhone,
                                contentDescription = null,
                                tint = DeepPurple
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Trusted Family Contact",
                                style = MaterialTheme.typography.labelMedium,
                                color = DarkInk.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${primaryContact?.name} (${primaryContact?.relationship})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = DarkInk
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MintAccent)
                            .clickable {
                                primaryContact?.phoneNumber?.let { num ->
                                    showCallConfirmDialog = num
                                }
                            }
                            .padding(12.dp)
                            .testTag("home_call_family_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Contact",
                            tint = MintDarkText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Safety Summary (based on real app data)
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Text(
                text = "Your Safety Summary",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryMetric(
                    count = scanCount.toString(),
                    label = StringsDictionary.getString(langCode, "summary_scans_done"),
                    color = DeepPurple
                )
                SummaryMetric(
                    count = threatCount.toString(),
                    label = StringsDictionary.getString(langCode, "summary_threats_blocked"),
                    color = WarningRose
                )
                SummaryMetric(
                    count = contacts.size.toString(),
                    label = StringsDictionary.getString(langCode, "summary_contacts"),
                    color = MintDarkText
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Recent Scans Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = StringsDictionary.getString(langCode, "recent_scans"),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )

            Text(
                text = StringsDictionary.getString(langCode, "view_all"),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple,
                modifier = Modifier
                    .clickable { viewModel.navigate(AppScreen.HISTORY) }
                    .padding(4.dp)
                    .testTag("home_view_all_scans")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (scans.isEmpty()) {
            AccessibleCard(
                backgroundColor = Color.White
            ) {
                Text(
                    text = StringsDictionary.getString(langCode, "no_scans_yet"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkInk.copy(alpha = 0.8f)
                )
            }
        } else {
            scans.take(3).forEach { record ->
                AccessibleCard(
                    backgroundColor = Color.White,
                    onClick = { viewModel.viewScanDetail(record) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = record.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = DarkInk
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = record.inputText.take(60) + if (record.inputText.length > 60) "..." else "",
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkInk.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        RiskBadge(riskLevel = record.riskLevel)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Volunteer Community Banner
        AccessibleCard(
            backgroundColor = LavenderSubtle,
            borderColor = LavenderPrimary,
            onClick = { viewModel.navigate(AppScreen.VOLUNTEER) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = DeepPurple,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = StringsDictionary.getString(langCode, "community_title"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = DeepPurple
                        )
                        Text(
                            text = StringsDictionary.getString(langCode, "community_sub"),
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkInk.copy(alpha = 0.8f)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = DeepPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Call Confirmation Dialog
    if (showCallConfirmDialog != null) {
        val phoneToCall = showCallConfirmDialog ?: ""
        AlertDialog(
            onDismissRequest = { showCallConfirmDialog = null },
            title = {
                Text(
                    text = "Confirm Phone Call",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to dial $phoneToCall now?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCallConfirmDialog = null
                        try {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phoneToCall")
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                ) {
                    Text(text = "Call Now", fontWeight = FontWeight.Bold, color = DeepPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCallConfirmDialog = null }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    borderColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DarkInk
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkInk.copy(alpha = 0.75f)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = iconTint
            )
        }
    }
}

@Composable
private fun SummaryMetric(
    count: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = DarkInk.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}
