# Experiment 9 – Data Persistence using SharedPreferences and SQLite

<p align="center">
  <b>Android Application – Experiment 9</b><br>
  Demonstration of local data persistence using SharedPreferences and SQLite
</p>

---

## 📌 Student Details

| Field              | Details                        |
| ------------------ | ------------------------------ |
| **Name**           | Shubham Shivaji Kondikire      |
| **USN**            | 25MCAR0102                     |
| **Program**        | MCA                            |
| **University**     | Jain (Deemed-to-be University) |
| **Experiment No.** | 9                              |
| **Platform**       | Android                        |
| **Language**       | Kotlin                         |
| **UI Framework**   | Jetpack Compose                |

---

## 🎯 Aim

To develop an Android application that demonstrates **data persistence** using:

1. **SharedPreferences** for storing small key-value data.
2. **SQLite** for storing structured data in a local database.

The application demonstrates that data remains available even after the application is closed and opened again.

---

## 📖 Introduction

Data persistence means storing data so that it is available even after an application is closed or restarted.

In Android applications, different storage mechanisms can be used depending on the type and amount of data.

This experiment demonstrates two important local storage techniques:

* **SharedPreferences** – suitable for small values such as names, settings, Boolean flags, and counters.
* **SQLite** – suitable for structured data stored in tables and manipulated using CRUD operations.

The application combines both techniques in one simple **Student Profile & Notes Manager**.

---

## 🧠 Technologies Used

* Kotlin
* Android SDK
* Jetpack Compose
* Material 3
* SharedPreferences
* SQLite
* SQLiteOpenHelper
* Gradle Kotlin DSL
* Android Studio

---

## 💾 Data Persistence Methods

### 1. SharedPreferences

SharedPreferences stores data as **key-value pairs**.

It is useful for small pieces of information such as:

* Student name
* USN
* Dark mode setting
* Remember-me preference
* Application launch count
* Last saved time

The application uses a SharedPreferences file named:

```text
student_prefs.xml
```

The data is stored in the application's private storage.

### Example

```kotlin
prefs.edit()
    .putString("name", name)
    .putString("usn", usn)
    .putBoolean("remember_me", true)
    .apply()
```

---

### 2. SQLite

SQLite is a lightweight relational database available on Android.

This project uses SQLite to store notes.

The database is:

```text
exp9.db
```

The table used by the application is:

```text
notes
```

The table contains:

| Column       | Type    | Description                     |
| ------------ | ------- | ------------------------------- |
| `id`         | INTEGER | Primary key with auto increment |
| `title`      | TEXT    | Note title                      |
| `content`    | TEXT    | Note content                    |
| `created_at` | TEXT    | Date and time of creation       |

### SQLite Table

```sql
CREATE TABLE notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT,
    created_at TEXT NOT NULL
);
```

---

## ⚖️ SharedPreferences vs SQLite

| Feature              | SharedPreferences    | SQLite             |
| -------------------- | -------------------- | ------------------ |
| Storage model        | Key-value            | Relational table   |
| Best for             | Small data/settings  | Structured records |
| Data format          | Key-value pairs      | Rows and columns   |
| Query support        | Key based            | SQL queries        |
| Used in this project | Profile and settings | Notes              |
| CRUD operations      | Basic read/write     | Full CRUD          |

---

## 📱 Application Features

The application contains two main sections.

### 1. Preferences

The Preferences screen demonstrates SharedPreferences.

#### Features

* Enter student name
* Enter USN
* Save student details
* Clear saved profile
* Enable/disable dark mode
* Remember student details
* Count application launches
* Display currently stored values

The saved data remains available after restarting the application when the **Remember my details** option is enabled.

---

### 2. SQLite Notes

The SQLite Notes screen demonstrates database operations.

#### Features

* Add a new note
* Display saved notes
* Edit an existing note
* Delete an individual note
* Delete all notes
* Display note ID
* Display creation date and time
* Preserve notes after application restart

The application performs the following CRUD operations:

```text
Create  → Add Note
Read    → Display Notes
Update  → Edit Note
Delete  → Delete Note
```

---

## 🏗️ Project Structure

```text
Exp9_DataPersistence/
│
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   │
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           │
│           ├── java/
│           │   └── com/
│           │       └── example/
│           │           └── exp9/
│           │               │
│           │               ├── MainActivity.kt
│           │               │
│           │               ├── data/
│           │               │   ├── PrefsManager.kt
│           │               │   └── DatabaseHelper.kt
│           │               │
│           │               └── ui/
│           │                   ├── AppRoot.kt
│           │                   ├── Components.kt
│           │                   ├── PrefsScreen.kt
│           │                   ├── NotesScreen.kt
│           │                   │
│           │                   └── theme/
│           │                       └── Theme.kt
│           │
│           └── res/
│               ├── drawable/
│               │   └── ic_launcher.xml
│               │
│               └── values/
│                   ├── strings.xml
│                   └── themes.xml
│
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── .gitignore
└── README.md
```

---

## 📂 Important Files

| File                   | Purpose                                              |
| ---------------------- | ---------------------------------------------------- |
| `MainActivity.kt`      | Application entry point and initialization           |
| `PrefsManager.kt`      | Handles SharedPreferences operations                 |
| `DatabaseHelper.kt`    | Creates SQLite database and performs CRUD operations |
| `AppRoot.kt`           | Main application layout and bottom navigation        |
| `PrefsScreen.kt`       | UI for SharedPreferences demonstration               |
| `NotesScreen.kt`       | UI for SQLite notes and CRUD operations              |
| `Components.kt`        | Reusable Compose UI components                       |
| `Theme.kt`             | Light and dark Material 3 themes                     |
| `AndroidManifest.xml`  | Android application configuration                    |
| `app/build.gradle.kts` | App SDK configuration and dependencies               |

---

## 🔄 Application Flow

```text
                    ┌─────────────────────┐
                    │     MainActivity    │
                    └──────────┬──────────┘
                               │
                ┌──────────────┴──────────────┐
                │                             │
        ┌───────▼────────┐            ┌───────▼────────┐
        │   Preferences  │            │  SQLite Notes  │
        └───────┬────────┘            └───────┬────────┘
                │                             │
        ┌───────▼────────┐            ┌───────▼────────┐
        │ SharedPrefs    │            │ SQLite Database│
        │ student_prefs  │            │    exp9.db     │
        └────────────────┘            └───────┬────────┘
                                             │
                                    ┌────────┴────────┐
                                    │       CRUD      │
                                    ├─────────────────┤
                                    │ Create          │
                                    │ Read            │
                                    │ Update          │
                                    │ Delete          │
                                    └─────────────────┘
```

---

## 🔑 SharedPreferences Implementation

The project contains a dedicated `PrefsManager` class that keeps all SharedPreferences operations in one place.

### Stored Values

```text
name
usn
dark_mode
remember_me
launch_count
last_saved
```

### Saving Profile

```kotlin
fun saveProfile(name: String, usn: String, rememberMe: Boolean) {
    val stamp = SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    ).format(Date())

    prefs.edit()
        .putString(KEY_NAME, if (rememberMe) name else "")
        .putString(KEY_USN, if (rememberMe) usn else "")
        .putBoolean(KEY_REMEMBER, rememberMe)
        .putString(KEY_LAST_SAVED, stamp)
        .apply()
}
```

### Clearing Profile

```kotlin
fun clearProfile() {
    prefs.edit()
        .remove(KEY_NAME)
        .remove(KEY_USN)
        .remove(KEY_REMEMBER)
        .remove(KEY_LAST_SAVED)
        .apply()
}
```

---

## 🗄️ SQLite Implementation

The application uses `SQLiteOpenHelper` through the `DatabaseHelper` class.

### Database

```text
exp9.db
```

### Table

```text
notes
```

### Insert

```kotlin
fun insertNote(title: String, content: String): Long {
    val values = ContentValues().apply {
        put(COL_TITLE, title)
        put(COL_CONTENT, content)
        put(COL_CREATED, stamp)
    }

    return writableDatabase.insert(TABLE, null, values)
}
```

### Read

```kotlin
fun getAllNotes(): List<Note> {
    // Reads all rows from the notes table
}
```

### Update

```kotlin
fun updateNote(id: Long, title: String, content: String): Int {
    return writableDatabase.update(
        TABLE,
        values,
        "$COL_ID = ?",
        arrayOf(id.toString())
    )
}
```

### Delete

```kotlin
fun deleteNote(id: Long): Int =
    writableDatabase.delete(
        TABLE,
        "$COL_ID = ?",
        arrayOf(id.toString())
    )
```

---

## 🧪 Test Cases

### Test Case 1 – Save Student Details

| Test            | Details                               |
| --------------- | ------------------------------------- |
| Input           | Student name and USN                  |
| Action          | Enable Remember Me and press **Save** |
| Expected Result | Name and USN are stored               |
| Verification    | Close and reopen the application      |
| Result          | Pass                                  |

---

### Test Case 2 – Dark Mode Persistence

| Test            | Details                           |
| --------------- | --------------------------------- |
| Action          | Enable Dark Mode                  |
| Expected Result | Application changes to dark theme |
| Verification    | Restart the application           |
| Result          | Dark mode remains enabled         |

---

### Test Case 3 – SQLite Insert

| Test            | Details                      |
| --------------- | ---------------------------- |
| Input           | Note title and content       |
| Action          | Press **Add Note**           |
| Expected Result | New note appears in the list |
| Result          | Pass                         |

---

### Test Case 4 – SQLite Update

| Test            | Details                  |
| --------------- | ------------------------ |
| Action          | Select the Edit button   |
| Input           | Change title/content     |
| Expected Result | Existing note is updated |
| Result          | Pass                     |

---

### Test Case 5 – SQLite Delete

| Test            | Details                  |
| --------------- | ------------------------ |
| Action          | Select Delete            |
| Expected Result | Selected note is removed |
| Result          | Pass                     |

---

### Test Case 6 – Data Persistence After Restart

| Test            | Details                            |
| --------------- | ---------------------------------- |
| Action          | Add notes and save profile         |
| Next Step       | Close and reopen the application   |
| Expected Result | Previously saved data is displayed |
| Result          | Pass                               |

---

## ▶️ How to Run the Project

### Step 1 – Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

### Step 2 – Open in Android Studio

Open the cloned project folder:

```text
Exp9_DataPersistence
```

### Step 3 – Gradle Sync

Allow Android Studio to complete the Gradle synchronization.

An internet connection may be required the first time dependencies are downloaded.

### Step 4 – Run the Application

Connect an Android device or start an Android Emulator.

Then click:

```text
Run ▶
```

---

## ⚙️ Project Configuration

| Configuration     | Value                        |
| ----------------- | ---------------------------- |
| Namespace         | `com.example.exp9`           |
| Application ID    | `com.example.exp9`           |
| Minimum SDK       | 24                           |
| Target SDK        | 35                           |
| Compile SDK       | 35                           |
| Java Version      | 17                           |
| Kotlin JVM Target | 17                           |
| Compose           | Enabled                      |
| UI                | Jetpack Compose + Material 3 |

---

## 📦 Main Dependencies

The project uses AndroidX and Jetpack Compose libraries, including:

```text
androidx.core:core-ktx
androidx.lifecycle:lifecycle-runtime-ktx
androidx.activity:activity-compose
androidx.compose.ui
androidx.compose.material3
```

The project uses the Kotlin DSL (`.kts`) for Gradle configuration.

---

## 📸 Screenshots

Screenshots can be added to the repository in the following folder:


<img width="732" height="1600" alt="exp9 (1)" src="https://github.com/user-attachments/assets/1f6aa5ad-4e19-4fa1-833f-82875f9dc5fe" />
<img width="732" height="1600" alt="exp9 (2)" src="https://github.com/user-attachments/assets/1a237e55-35e0-4e20-b2b7-8890a38d92d3" />
<img width="732" height="1600" alt="exp9 (3)" src="https://github.com/user-attachments/assets/52a8cbbf-f59e-4390-8d74-048fc3709fa7" />
<img width="732" height="1600" alt="exp9 (4)" src="https://github.com/user-attachments/assets/93b0406e-6954-4eb0-86c1-7506c094e2ae" />
<img width="732" height="1600" alt="exp9 (5)" src="https://github.com/user-attachments/assets/87b3743b-06eb-4948-9f4e-31c5f8253a58" />

## 🔐 Data Storage Location

The application stores data inside its private application storage.

### SharedPreferences

```text
/data/data/com.example.exp9/shared_prefs/student_prefs.xml
```

### SQLite

```text
/data/data/com.example.exp9/databases/exp9.db
```

These locations are managed by Android and are not normally accessible to other applications.

---

## 🧹 Clearing Data

If the application data is cleared from Android settings, both SharedPreferences and the SQLite database are removed.

For example:

```text
Settings
   ↓
Apps
   ↓
Exp9 Data Persistence
   ↓
Storage
   ↓
Clear Data
```

This resets the application to its initial state.

---

## 🎓 Learning Outcomes

After completing this experiment, the following concepts are demonstrated:

* Understanding data persistence in Android
* Using SharedPreferences
* Saving and retrieving key-value data
* Persisting application settings
* Using SQLite databases
* Creating database tables
* Performing CRUD operations
* Using `SQLiteOpenHelper`
* Using `ContentValues`
* Reading database records using a Cursor
* Building Android UI using Jetpack Compose
* Managing application state
* Implementing light and dark themes

---

## ✅ Conclusion

This experiment successfully demonstrates **data persistence in Android** using **SharedPreferences and SQLite**.

SharedPreferences is suitable for storing small values such as student information, application settings, Boolean preferences, and counters. SQLite is more suitable for structured information such as notes and records because it supports tables and CRUD operations.

The application demonstrates that stored information can be retrieved after the application is closed and reopened, providing a practical understanding of local data storage in Android.

---

## 👨‍💻 Author

**Shubham Shivaji Kondikire**

**USN:** 25MCAR0102

**MCA – Jain (Deemed-to-be University)**

---

## ⭐ Experiment Summary

```text
Experiment 9
     │
     ├── SharedPreferences
     │      ├── Name
     │      ├── USN
     │      ├── Dark Mode
     │      ├── Remember Me
     │      └── Launch Count
     │
     └── SQLite
            ├── Insert
            ├── Read
            ├── Update
            ├── Delete
            └── Delete All
```

---

> **Note:** This README is based on the Experiment 9 project implementation. Add your actual application screenshots to the `screenshots/` folder before publishing the repository.
