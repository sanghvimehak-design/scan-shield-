package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.StringsDictionary
import com.example.data.model.TextScale
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleButton
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
import com.example.ui.theme.WarningRose

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code
    val contacts by viewModel.allContacts.collectAsState()

    var showAddContactDialog by remember { mutableStateOf(false) }
    var newContactName by remember { mutableStateOf("") }
    var newContactRel by remember { mutableStateOf("Son") }
    var newContactPhone by remember { mutableStateOf("") }
    var showClearDataDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.goBack() },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(LavenderSubtle)
                    .testTag("settings_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DeepPurple
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = StringsDictionary.getString(langCode, "settings_title"),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Language Selection (9 Languages)
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = DeepPurple,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = StringsDictionary.getString(langCode, "setting_lang"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }

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
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) DeepPurple else LavenderSubtle)
                            .border(1.5.dp, if (isSelected) DeepPurple else LavenderPrimary, RoundedCornerShape(14.dp))
                            .clickable { viewModel.setLanguage(lang) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("setting_lang_${lang.code}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${lang.displayName} (${lang.nativeName})",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) Color.White else DarkInk
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Global Text Size Scaling
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Text(
                text = StringsDictionary.getString(langCode, "setting_text_size"),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextScale.entries.forEach { scale ->
                    val isSelected = prefs.textScale == scale
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) DeepPurple else LavenderSubtle)
                            .border(1.5.dp, if (isSelected) DeepPurple else LavenderPrimary, RoundedCornerShape(16.dp))
                            .clickable { viewModel.setTextScale(scale) }
                            .padding(vertical = 10.dp),
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
                            Text(
                                text = scale.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else DarkInk
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. High Contrast & Dark Theme Toggles
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = StringsDictionary.getString(langCode, "setting_high_contrast"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DarkInk
                    )
                    Text(
                        text = "Bold borders and highest readability colors",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.7f)
                    )
                }
                Switch(
                    checked = prefs.isHighContrast,
                    onCheckedChange = { viewModel.setHighContrast(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = DeepPurple, checkedTrackColor = LavenderPrimary)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = StringsDictionary.getString(langCode, "setting_dark_theme"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DarkInk
                    )
                    Text(
                        text = "Easy on eyes in dim light",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.7f)
                    )
                }
                Switch(
                    checked = prefs.isDarkTheme,
                    onCheckedChange = { viewModel.setDarkTheme(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = DeepPurple, checkedTrackColor = LavenderPrimary)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Voice Assistant Speech Speed
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = DeepPurple,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = StringsDictionary.getString(langCode, "setting_voice_speed"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            var speedSlider by remember { mutableFloatStateOf(prefs.speechSpeed) }
            Slider(
                value = speedSlider,
                onValueChange = {
                    speedSlider = it
                    viewModel.setSpeechSpeed(it)
                },
                valueRange = 0.75f..1.25f,
                steps = 4,
                colors = SliderDefaults.colors(thumbColor = DeepPurple, activeTrackColor = LavenderPrimary)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Slow (0.75x)", style = MaterialTheme.typography.labelSmall)
                Text("Normal (1.0x)", style = MaterialTheme.typography.labelSmall)
                Text("Fast (1.25x)", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Trusted Family Contacts Management
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trusted Family Contacts (${contacts.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )

                IconButton(
                    onClick = { showAddContactDialog = true },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(LavenderSubtle)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact", tint = DeepPurple)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            contacts.forEach { contact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${contact.name} (${contact.relationship})",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = DarkInk
                        )
                        Text(
                            text = contact.phoneNumber + if (contact.isPrimary) " • Primary SOS" else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (contact.isPrimary) DeepPurple else DarkInk.copy(alpha = 0.7f)
                        )
                    }

                    IconButton(onClick = { viewModel.deleteContact(contact.id) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = WarningRose.copy(alpha = 0.7f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Privacy & Local Storage Guarantee
        AccessibleCard(
            backgroundColor = MintSubtle,
            borderColor = MintAccent
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MintDarkText,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "100% On-Device Privacy",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MintDarkText
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = StringsDictionary.getString(langCode, "privacy_notice"),
                style = MaterialTheme.typography.bodySmall,
                color = DarkInk
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Clear Data Button
        AccessibleButton(
            text = StringsDictionary.getString(langCode, "setting_clear_data"),
            icon = Icons.Default.Delete,
            isDanger = true,
            onClick = { showClearDataDialog = true },
            testTag = "clear_all_data_button"
        )

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Add Contact Dialog
    if (showAddContactDialog) {
        AlertDialog(
            onDismissRequest = { showAddContactDialog = false },
            title = {
                Text("Add Trusted Contact", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newContactName,
                        onValueChange = { newContactName = it },
                        label = { Text("Name (e.g. Ramesh)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newContactRel,
                        onValueChange = { newContactRel = it },
                        label = { Text("Relationship (e.g. Son, Daughter)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newContactPhone,
                        onValueChange = { newContactPhone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newContactName.isNotBlank() && newContactPhone.isNotBlank()) {
                            viewModel.addTrustedContact(
                                name = newContactName,
                                relationship = newContactRel,
                                phone = newContactPhone,
                                isPrimary = contacts.isEmpty()
                            )
                            newContactName = ""
                            newContactPhone = ""
                            showAddContactDialog = false
                            Toast.makeText(context, "Contact added", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, color = DeepPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddContactDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // Clear Data Dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = {
                Text("Reset All Scan Shield Data?", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Text("This will remove all saved scans, contacts, and quiz progress from this phone.", style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearDataDialog = false
                        viewModel.clearAllData()
                        Toast.makeText(context, "All data reset", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Reset Everything", fontWeight = FontWeight.Bold, color = WarningRose)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}
