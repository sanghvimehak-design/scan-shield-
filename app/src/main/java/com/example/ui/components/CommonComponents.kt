package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.RiskLevel
import com.example.data.model.StringsDictionary
import com.example.ui.AppScreen
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
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseLight

@Composable
fun AccessibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isDanger: Boolean = false,
    isSecondary: Boolean = false,
    enabled: Boolean = true,
    testTag: String = "accessible_button"
) {
    val containerColor = when {
        isDanger -> WarningRose
        isSecondary -> LavenderSubtle
        else -> DeepPurple
    }
    val contentColor = when {
        isDanger -> Color.White
        isSecondary -> DeepPurple
        else -> Color.White
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color.LightGray
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AccessibleCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = CardSurfaceLight,
    borderColor: Color = CardBorderLavender,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    } else {
        modifier.fillMaxWidth()
    }

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}

@Composable
fun RiskBadge(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon, label) = when (riskLevel) {
        RiskLevel.HIGH_RISK -> Quadruple(WarningRoseLight, WarningRose, Icons.Default.Warning, "HIGH RISK SCAM 🛑")
        RiskLevel.SUSPICIOUS -> Quadruple(ButterYellow, ButterYellowDarkText, Icons.Default.Info, "SUSPICIOUS WARNING ⚠️")
        RiskLevel.NO_OBVIOUS_WARNING -> Quadruple(MintAccent, MintDarkText, Icons.Default.CheckCircle, "NO OBVIOUS WARNING SIGNS ✅")
        RiskLevel.UNABLE_TO_VERIFY -> Quadruple(LavenderSubtle, DeepPurple, Icons.Default.HelpOutline, "UNABLE TO VERIFY ❓")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun AudioSpeakerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Read aloud"
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(LavenderSubtle)
            .border(1.5.dp, LavenderPrimary, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = contentDescription,
            tint = DeepPurple,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun ScanShieldTopBar(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onSosClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLangMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mascot + App Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onHomeClick)
                .testTag("app_brand_logo")
        ) {
            MascotBadge(size = 46.dp, animated = false)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "SCAN SHIELD",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = DeepPurple,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Pause. Check. Stay Safe. 💜",
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                    color = DarkInk.copy(alpha = 0.7f)
                )
            }
        }

        // Language & SOS Action buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Language Picker Chip
            Box {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(LavenderSubtle)
                        .border(1.dp, LavenderPrimary, RoundedCornerShape(14.dp))
                        .clickable { showLangMenu = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("language_picker_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Change Language",
                            tint = DeepPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentLanguage.nativeName,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = DeepPurple
                        )
                    }
                }

                DropdownMenu(
                    expanded = showLangMenu,
                    onDismissRequest = { showLangMenu = false }
                ) {
                    AppLanguage.entries.forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${lang.displayName} (${lang.nativeName})",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            onClick = {
                                onLanguageSelected(lang)
                                showLangMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // SOS Quick Launcher
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(WarningRose)
                    .clickable(onClick = onSosClick)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
                    .testTag("sos_emergency_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Emergency SOS",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SOS 1930",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ScanShieldBottomBar(
    currentScreen: AppScreen,
    currentLangCode: String,
    onNavigate: (AppScreen) -> Unit
) {
    val items = listOf(
        NavigationItem(
            screen = AppScreen.HOME,
            label = StringsDictionary.getString(currentLangCode, "nav_home"),
            icon = Icons.Default.Security,
            testTag = "nav_home"
        ),
        NavigationItem(
            screen = AppScreen.SCANNER,
            label = StringsDictionary.getString(currentLangCode, "nav_scan"),
            icon = Icons.Default.Info,
            testTag = "nav_scan"
        ),
        NavigationItem(
            screen = AppScreen.PAY_CHECK,
            label = StringsDictionary.getString(currentLangCode, "nav_pay_check"),
            icon = Icons.Default.Warning,
            testTag = "nav_pay_check"
        ),
        NavigationItem(
            screen = AppScreen.LEARN,
            label = StringsDictionary.getString(currentLangCode, "nav_learn"),
            icon = Icons.Default.CheckCircle,
            testTag = "nav_learn"
        ),
        NavigationItem(
            screen = AppScreen.SETTINGS,
            label = StringsDictionary.getString(currentLangCode, "nav_settings"),
            icon = Icons.Default.Language,
            testTag = "nav_settings"
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DeepPurple,
                    selectedTextColor = DeepPurple,
                    indicatorColor = LavenderSubtle,
                    unselectedIconColor = DarkInk.copy(alpha = 0.6f),
                    unselectedTextColor = DarkInk.copy(alpha = 0.6f)
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

private data class NavigationItem(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)
