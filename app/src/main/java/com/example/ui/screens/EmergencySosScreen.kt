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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BankEmergencyContact
import com.example.data.model.SafetyData
import com.example.data.model.StringsDictionary
import com.example.ui.ScanShieldViewModel
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccessibleCard
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderSubtle
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseLight

@Composable
fun EmergencySosScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs by viewModel.preferences.collectAsState()
    val langCode = prefs.language.code
    val primaryContact by viewModel.primaryContact.collectAsState()

    val scrollState = rememberScrollState()

    var confirmCallDialogNumber by remember { mutableStateOf<Pair<String, String>?>(null) }
    var confirmSmsDialogContact by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Bar
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
                    .testTag("emergency_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DeepPurple
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = StringsDictionary.getString(langCode, "emergency_title"),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = WarningRose
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SOS Golden Hour Notice
        AccessibleCard(
            backgroundColor = WarningRoseLight,
            borderColor = WarningRose
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningRose,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "THE GOLDEN HOUR RULE ⏱️",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        color = WarningRose
                    )
                    Text(
                        text = "If an unauthorized transaction took place, report within 2 hours to freeze the recipient's bank account before money is withdrawn.",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. National Cybercrime Helpline 1930
        AccessibleCard(
            backgroundColor = Color.White,
            borderColor = WarningRose
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "National Cyber Helpline: 1930",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = WarningRose
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ministry of Home Affairs & Indian Cyber Crime Coordination Centre (I4C). Direct assistance to freeze funds.",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(WarningRose)
                        .clickable {
                            confirmCallDialogNumber = Pair("National Cyber Crime Helpline", "1930")
                        }
                        .padding(14.dp)
                        .testTag("dial_1930_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Dial 1930",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Official Portal Link
        AccessibleCard(
            backgroundColor = Color.White,
            onClick = {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in"))
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
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
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = null,
                        tint = DeepPurple,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Official Portal: cybercrime.gov.in",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = DeepPurple
                        )
                        Text(
                            text = "Register complaint online with transaction IDs and screenshots",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkInk.copy(alpha = 0.7f)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = DeepPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Primary Trusted Family Member
        if (primaryContact != null) {
            AccessibleCard(
                backgroundColor = Color.White
            ) {
                Text(
                    text = "Trusted Family Emergency Contact",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${primaryContact?.name} (${primaryContact?.relationship}) - ${primaryContact?.phoneNumber}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkInk
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AccessibleButton(
                        text = "Call Contact",
                        icon = Icons.Default.Call,
                        onClick = {
                            primaryContact?.phoneNumber?.let { num ->
                                confirmCallDialogNumber = Pair(primaryContact?.name ?: "Family Contact", num)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "emergency_call_family_btn"
                    )

                    AccessibleButton(
                        text = "Send SOS SMS",
                        icon = Icons.Default.Email,
                        isSecondary = true,
                        onClick = {
                            primaryContact?.phoneNumber?.let { num ->
                                confirmSmsDialogContact = num
                            }
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "emergency_sms_family_btn"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4. Five Steps to Freeze Account Immediately
        AccessibleCard(
            backgroundColor = Color.White
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = null,
                    tint = DeepPurple,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = StringsDictionary.getString(langCode, "freeze_steps"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val steps = listOf(
                "1. Immediately call your bank's toll-free number below to block NetBanking, UPI, and debit cards.",
                "2. Dial 1930 within the Golden Hour to place a hold on the beneficiary's bank account.",
                "3. Take screenshots of fraudulent SMS messages, payment receipts, and call logs as evidence.",
                "4. File a complaint at cybercrime.gov.in with your transaction UTR numbers.",
                "5. Visit your bank branch with the cybercrime acknowledgment slip to file a chargeback."
            )

            steps.forEach { step ->
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkInk,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Verified Indian Bank Fraud Helplines Directory
        Text(
            text = StringsDictionary.getString(langCode, "bank_helplines"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepPurple
        )

        Spacer(modifier = Modifier.height(8.dp))

        SafetyData.verifiedBankDirectory.forEach { bank ->
            BankRow(
                bank = bank,
                onCall = { number ->
                    confirmCallDialogNumber = Pair(bank.bankName, number)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Call Confirmation Dialog
    if (confirmCallDialogNumber != null) {
        val (label, phone) = confirmCallDialogNumber!!
        AlertDialog(
            onDismissRequest = { confirmCallDialogNumber = null },
            title = {
                Text(
                    text = "Confirm Call: $label",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "You are about to dial verified number $phone. Proceed to system dialler?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmCallDialogNumber = null
                        try {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phone")
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                ) {
                    Text(text = "Open Dialler", fontWeight = FontWeight.Bold, color = DeepPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmCallDialogNumber = null }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )
    }

    // SOS SMS Confirmation Dialog
    if (confirmSmsDialogContact != null) {
        val phone = confirmSmsDialogContact!!
        AlertDialog(
            onDismissRequest = { confirmSmsDialogContact = null },
            title = {
                Text(
                    text = "Send Emergency SOS Alert?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "This will open your SMS app with an emergency alert template for $phone:\n\n'EMERGENCY: I suspect an online financial scam or unauthorized debit. Please call me right away! (Sent via Scan Shield)'",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmSmsDialogContact = null
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("smsto:$phone")
                                putExtra(
                                    "sms_body",
                                    "EMERGENCY: I suspect an online financial scam or unauthorized debit. Please call me right away! (Sent via Scan Shield)"
                                )
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                ) {
                    Text(text = "Open SMS", fontWeight = FontWeight.Bold, color = WarningRose)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmSmsDialogContact = null }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
private fun BankRow(
    bank: BankEmergencyContact,
    onCall: (String) -> Unit
) {
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
                    text = bank.bankName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DarkInk
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Toll Free: ${bank.tollFreeNumber} • ${bank.alternateNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkInk.copy(alpha = 0.75f)
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LavenderSubtle)
                    .clickable { onCall(bank.tollFreeNumber) }
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call ${bank.bankName}",
                    tint = DeepPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
