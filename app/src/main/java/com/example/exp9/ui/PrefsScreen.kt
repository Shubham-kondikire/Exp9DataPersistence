package com.example.exp9.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.exp9.data.PrefsManager

@Composable
fun PrefsScreen(
    prefs: PrefsManager,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onMessage: (String) -> Unit
) {
    // Form fields are pre-filled from SharedPreferences (this is the "persistence" part)
    var name by rememberSaveable { mutableStateOf(prefs.name) }
    var usn by rememberSaveable { mutableStateOf(prefs.usn) }
    var rememberMe by rememberSaveable { mutableStateOf(prefs.rememberMe) }
    var stored by remember { mutableStateOf(prefs.snapshot()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ---------- Hero ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "Welcome back",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Hello, ${stored.name.ifBlank { "Guest" }}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.22f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "App opened ${stored.launchCount} time(s)",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }
        }

        // ---------- Form ----------
        SectionCard(
            title = "Student Profile",
            subtitle = "Saved with SharedPreferences – survives app restart"
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = usn,
                onValueChange = { usn = it },
                label = { Text("USN") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth()
            )
            SwitchRow(
                title = "Dark mode",
                subtitle = "Applied instantly and remembered",
                checked = darkMode,
                onCheckedChange = {
                    onDarkModeChange(it)
                    stored = prefs.snapshot()
                }
            )
            SwitchRow(
                title = "Remember my details",
                subtitle = "If off, name & USN are not stored",
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (name.isBlank() || usn.isBlank()) {
                            onMessage("Please enter both name and USN")
                        } else {
                            prefs.saveProfile(name.trim(), usn.trim(), rememberMe)
                            stored = prefs.snapshot()
                            onMessage(
                                if (rememberMe) "Saved to SharedPreferences ✔"
                                else "Remember is off – details not stored"
                            )
                        }
                    }
                ) { Text("Save") }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        prefs.clearProfile()
                        name = ""
                        usn = ""
                        rememberMe = true
                        stored = prefs.snapshot()
                        onMessage("Profile cleared")
                    }
                ) { Text("Clear") }
            }
        }

        // ---------- Live view of the XML file ----------
        SectionCard(
            title = "Stored values",
            subtitle = "${PrefsManager.FILE_NAME}.xml (read back from SharedPreferences)"
        ) {
            KeyValueRow("name", stored.name)
            KeyValueRow("usn", stored.usn)
            KeyValueRow("dark_mode", stored.darkMode.toString())
            KeyValueRow("remember_me", stored.rememberMe.toString())
            KeyValueRow("launch_count", stored.launchCount.toString())
            KeyValueRow("last_saved", stored.lastSaved)
        }
    }
}
