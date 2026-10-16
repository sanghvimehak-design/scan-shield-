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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.model.VolunteerData
import com.example.data.model.VolunteerProfile
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VolunteerScreen(
    viewModel: ScanShieldViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookedSessions by viewModel.bookedSessions.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Learner Mode, 1: Volunteer Mode
    var selectedLanguageFilter by remember { mutableStateOf<String?>(null) }
    var selectedVolunteerToBook by remember { mutableStateOf<VolunteerProfile?>(null) }
    var bookingTopic by remember { mutableStateOf("Help with WhatsApp & UPI Safety") }
    var showReportDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val availableLanguages = listOf("All", "English", "Hindi", "Gujarati", "Tamil", "Marathi", "Bengali", "Malayalam")

    val filteredVolunteers = VolunteerData.demoVolunteers.filter { v ->
        if (selectedLanguageFilter == null || selectedLanguageFilter == "All") true
        else v.languages.contains(selectedLanguageFilter)
    }

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
                    .testTag("volunteer_back_button")
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
                    text = "Volunteer Safety Community",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Text(
                    text = "Connecting Pensioners with Youth Mentors",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkInk.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Role Tabs (Learner vs Volunteer)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = DeepPurple,
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("I am a Senior Learner", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("I want to Volunteer", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // LEARNER TAB
            // Community Strict Safety Guarantee
            AccessibleCard(
                backgroundColor = MintSubtle,
                borderColor = MintAccent
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = MintDarkText,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "100% Free & Safe Mentorship Guarantee",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MintDarkText
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Volunteers will NEVER ask for money, OTPs, passwords, or remote screen control. All profiles are simulated demo mentors.",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkInk
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Language Filter Chips
            Text(
                text = "Filter Mentors by Language:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableLanguages) { lang ->
                    val isSelected = (selectedLanguageFilter == null && lang == "All") || selectedLanguageFilter == lang
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) DeepPurple else LavenderSubtle)
                            .clickable {
                                selectedLanguageFilter = if (lang == "All") null else lang
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Color.White else DeepPurple
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Booked Sessions (if any)
            if (bookedSessions.isNotEmpty()) {
                Text(
                    text = "Your Scheduled Sessions (${bookedSessions.size}):",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(6.dp))

                bookedSessions.forEach { session ->
                    AccessibleCard(
                        backgroundColor = LavenderSubtle
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = session.topic,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = DeepPurple
                                )
                                Text(
                                    text = "Mentor: ${session.volunteerName} • ${session.date} (${session.timeSlot})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DarkInk
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MintAccent)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = session.status, fontWeight = FontWeight.Bold, color = MintDarkText, fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Volunteer Profiles List
            Text(
                text = "Available Safety Mentors (${filteredVolunteers.size}):",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepPurple
            )

            Spacer(modifier = Modifier.height(8.dp))

            filteredVolunteers.forEach { volunteer ->
                AccessibleCard(
                    backgroundColor = Color.White
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(LavenderSubtle),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = DeepPurple,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "${volunteer.name}, ${volunteer.age}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = DarkInk
                                )
                                Text(
                                    text = "${volunteer.city}, ${volunteer.state} • ${volunteer.languages.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DarkInk.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Rating badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = ButterYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${volunteer.rating}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = DarkInk
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = volunteer.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkInk.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        volunteer.specialties.forEach { spec ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LavenderSubtle)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(text = spec, style = MaterialTheme.typography.labelSmall, color = DeepPurple)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = volunteer.availableDays,
                            style = MaterialTheme.typography.labelSmall,
                            color = DarkInk.copy(alpha = 0.6f)
                        )

                        AccessibleButton(
                            text = "Schedule Free Session",
                            onClick = { selectedVolunteerToBook = volunteer },
                            modifier = Modifier.width(190.dp),
                            testTag = "book_${volunteer.id}"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            // VOLUNTEER REGISTRATION TAB
            AccessibleCard(
                backgroundColor = Color.White
            ) {
                Text(
                    text = "Join as a Youth Safety Mentor",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Help pensioners in your city spot fake messages, prevent online financial theft, and learn digital banking calmly without frustration.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkInk
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Community Safety Guidelines:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(6.dp))

                VolunteerData.communityGuidelines.forEach { rule ->
                    Text(
                        text = "• $rule",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                AccessibleButton(
                    text = "Register as Volunteer (Demo)",
                    onClick = {
                        Toast.makeText(context, "Thank you! Volunteer orientation materials will be sent to your email.", Toast.LENGTH_LONG).show()
                    },
                    testTag = "volunteer_register_button"
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Report Abuse Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showReportDialog = true }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Report,
                contentDescription = null,
                tint = WarningRose,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Report Suspicious Volunteer Behavior or Abuse",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = WarningRose
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Booking Session Dialog
    if (selectedVolunteerToBook != null) {
        val vol = selectedVolunteerToBook!!
        AlertDialog(
            onDismissRequest = { selectedVolunteerToBook = null },
            title = {
                Text(
                    text = "Schedule Safety Session with ${vol.name}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Languages: ${vol.languages.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Availability: ${vol.availableDays}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "What would you like help with?",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = bookingTopic,
                        onValueChange = { bookingTopic = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.bookSession(vol, bookingTopic, "Upcoming Saturday", "11:00 AM")
                        Toast.makeText(context, "Session booked with ${vol.name}! Details saved in your list.", Toast.LENGTH_LONG).show()
                        selectedVolunteerToBook = null
                    }
                ) {
                    Text(text = "Confirm Booking", fontWeight = FontWeight.Bold, color = DeepPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedVolunteerToBook = null }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )
    }

    // Report Dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = "Report Volunteer Abuse",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = WarningRose
                )
            },
            text = {
                Text(
                    text = "If any volunteer ever asked for your OTP, UPI PIN, money, or remote access, please report immediately. The profile will be blocked and escalated to cyber authorities.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showReportDialog = false
                        Toast.makeText(context, "Report received. Our moderation team has flagged this case.", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Text(text = "Submit Report", fontWeight = FontWeight.Bold, color = WarningRose)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text(text = "Close", color = Color.Gray)
                }
            }
        )
    }
}
