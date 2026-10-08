package com.example.exp9.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exp9.data.DatabaseHelper
import com.example.exp9.data.Note

@Composable
fun NotesScreen(db: DatabaseHelper, onMessage: (String) -> Unit) {
    // Rows are loaded from the SQLite database every time the screen opens
    var notes by remember { mutableStateOf(db.getAllNotes()) }
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var editingId by rememberSaveable { mutableStateOf<Long?>(null) }
    var showConfirm by remember { mutableStateOf(false) }

    fun refresh() { notes = db.getAllNotes() }
    fun resetForm() { title = ""; content = ""; editingId = null }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Delete all notes?") },
            text = { Text("This removes every row from the \"notes\" table.") },
            confirmButton = {
                TextButton(onClick = {
                    db.deleteAll()
                    resetForm()
                    refresh()
                    showConfirm = false
                    onMessage("All rows deleted")
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ---------- Form ----------
        item {
            SectionCard(
                title = if (editingId == null) "New note" else "Edit note #$editingId",
                subtitle = "Stored in ${DatabaseHelper.DB_NAME} → table \"${DatabaseHelper.TABLE}\""
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (title.isBlank()) {
                                onMessage("Title cannot be empty")
                            } else {
                                val id = editingId
                                if (id == null) {
                                    db.insertNote(title.trim(), content.trim())
                                    onMessage("Row inserted ✔")
                                } else {
                                    db.updateNote(id, title.trim(), content.trim())
                                    onMessage("Row updated ✔")
                                }
                                resetForm()
                                refresh()
                            }
                        }
                    ) { Text(if (editingId == null) "Add note" else "Update") }

                    if (editingId != null) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { resetForm() }
                        ) { Text("Cancel") }
                    }
                }
            }
        }

        // ---------- List header ----------
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Saved notes (${notes.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (notes.isNotEmpty()) {
                    TextButton(onClick = { showConfirm = true }) {
                        Text("Delete all", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        // ---------- Rows ----------
        if (notes.isEmpty()) {
            item {
                Text(
                    "No rows yet. Add your first note above – it will still be here after you close the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(notes, key = { it.id }) { note ->
                NoteCard(
                    note = note,
                    onEdit = {
                        editingId = note.id
                        title = note.title
                        content = note.content
                    },
                    onDelete = {
                        db.deleteNote(note.id)
                        if (editingId == note.id) resetForm()
                        refresh()
                        onMessage("Row deleted")
                    }
                )
            }
        }
    }
}

@Composable
private fun NoteCard(note: Note, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "#${note.id}  ${note.title}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (note.content.isNotBlank()) {
                    Text(note.content, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    note.createdAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
