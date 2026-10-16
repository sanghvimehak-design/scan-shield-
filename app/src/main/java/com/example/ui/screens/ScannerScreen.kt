package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.DemoThreatPresets
import com.example.data.model.StringsDictionary
import com.example.data.model.ThreatPreset
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
import com.example.ui.theme.WarningRoseLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ScannerScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code

    val inputText by viewModel.scanInputText.collectAsState()
    val selectedUri by viewModel.selectedImageUri.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setSelectedImageUri(uri)
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = StringsDictionary.getString(langCode, "scanner_title"),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = StringsDictionary.getString(langCode, "scanner_prompt"),
            style = MaterialTheme.typography.bodyMedium,
            color = DarkInk.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Screenshot Picker / Preview Card
        AccessibleCard(
            backgroundColor = if (selectedUri != null) LavenderSubtle else Color.White
        ) {
            if (selectedUri != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = selectedUri),
                            contentDescription = "Selected Screenshot",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )

                        IconButton(
                            onClick = { viewModel.setSelectedImageUri(null) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Remove photo",
                                tint = WarningRose
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Screenshot attached for AI multimodal analysis",
                        style = MaterialTheme.typography.labelMedium,
                        color = DeepPurple,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .padding(vertical = 12.dp)
                        .testTag("scanner_pick_photo_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(LavenderSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = DeepPurple,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = StringsDictionary.getString(langCode, "btn_select_screenshot"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = DeepPurple
                        )
                        Text(
                            text = "WhatsApp messages, SMS notices, or payment alerts",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkInk.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Manual Text / Phone / Link Input
        Text(
            text = "Or Paste Message / Phone Number:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DarkInk
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { viewModel.setScanInputText(it) },
            placeholder = {
                Text(
                    text = StringsDictionary.getString(langCode, "enter_text_placeholder"),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("scanner_input_field"),
            shape = RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = DeepPurple,
                unfocusedIndicatorColor = CardBorderLavender
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Analyze Button / Loading Indicator
        if (isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(LavenderSubtle)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = DeepPurple, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = StringsDictionary.getString(langCode, "analyzing"),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepPurple
                    )
                }
            }
        } else {
            AccessibleButton(
                text = StringsDictionary.getString(langCode, "btn_scan_now"),
                icon = Icons.Default.Search,
                enabled = inputText.isNotBlank() || selectedUri != null,
                onClick = {
                    scope.launch {
                        var bitmap: Bitmap? = null
                        if (selectedUri != null) {
                            try {
                                bitmap = withContext(Dispatchers.IO) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        val source = ImageDecoder.createSource(context.contentResolver, selectedUri!!)
                                        ImageDecoder.decodeBitmap(source)
                                    } else {
                                        @Suppress("DEPRECATION")
                                        MediaStore.Images.Media.getBitmap(context.contentResolver, selectedUri)
                                    }
                                }
                            } catch (_: Exception) {}
                        }
                        viewModel.runAnalysis(bitmap)
                    }
                },
                testTag = "scanner_analyze_button"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Demo Test Presets (12 Scams + 4 Benign)
        Text(
            text = StringsDictionary.getString(langCode, "test_scam_samples"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tap any realistic Indian scenario below to test instantly:",
            style = MaterialTheme.typography.bodySmall,
            color = DarkInk.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Scam Scenarios (12 unique)
        Text(
            text = "Scam Threats (12 Scenarios):",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = WarningRose
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DemoThreatPresets.scamPresets) { preset ->
                PresetChip(
                    preset = preset,
                    isDanger = true,
                    onClick = { viewModel.loadPreset(preset) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Benign Examples (4 unique)
        Text(
            text = "Legitimate Official Notices (4 Samples):",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MintDarkText
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DemoThreatPresets.benignPresets) { preset ->
                PresetChip(
                    preset = preset,
                    isDanger = false,
                    onClick = { viewModel.loadPreset(preset) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PresetChip(
    preset: ThreatPreset,
    isDanger: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isDanger) WarningRoseLight else MintSubtle
    val borderColor = if (isDanger) WarningRose else MintAccent
    val textColor = if (isDanger) WarningRose else MintDarkText

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("preset_${preset.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = preset.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
            Text(
                text = preset.category,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = DarkInk.copy(alpha = 0.7f)
            )
        }
    }
}
