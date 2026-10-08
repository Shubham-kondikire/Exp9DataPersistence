# Experiment 9 – Data Persistence using SharedPreferences and SQLite

> **Name:** Shubham Shivaji Kondikire  
> **USN:** 25MCAR0102  
> **Program:** MCA, Jain (Deemed-to-be University)  
> **Platform:** Android (Kotlin + Jetpack Compose)

---

## 1. Aim

To demonstrate **data persistence** in an Android application, i.e. keeping data available even after the app is closed or the device is restarted, using two storage mechanisms:

1. **SharedPreferences** – lightweight key-value storage
2. **SQLite** – a local relational database

## 2. Concept / Technology Behind It

By default, everything stored in variables or UI state is lost when the app process is killed. Android provides several options to persist data on the device. This experiment uses the two most common ones.

### 2.1 SharedPreferences
* Stores **small amounts of primitive data** (String, Int, Boolean, Float, Long) as **key-value pairs** in an XML file inside the app's private storage:  
  `/data/data/com.example.exp9/shared_prefs/student_prefs.xml`
* Accessed via `context.getSharedPreferences(name, MODE_PRIVATE)`.
* Writes are done with an `Editor` – `edit().putString(...).apply()` (`apply()` saves asynchronously, `commit()` saves synchronously).
* Best for: user settings, theme choice, login flags, simple counters.

### 2.2 SQLite
* An embedded, serverless **relational database** built into Android. Data is stored in tables with rows and columns and queried with SQL.
* Android gives the `SQLiteOpenHelper` class to create / upgrade the database and open it for reading or writing:  
  `/data/data/com.example.exp9/databases/exp9.db`
* Supports full **CRUD**: `INSERT`, `SELECT`, `UPDATE`, `DELETE`.
* Best for: structured, larger or list-type data (notes, contacts, records).

| Feature | SharedPreferences | SQLite |
|---|---|---|
| Data model | Key-value pairs | Tables / rows / columns |
| Data size | Small | Medium to large |
| Querying | By key only | Full SQL |
| Typical use | Settings, flags | Records, lists |

## 3. Scenario Used for the Demo – *Student Profile & Notes Manager*

The app has two tabs:

**Tab 1 – Preferences (SharedPreferences)**
* Student enters **name** and **USN** and taps **Save**.
* **Dark mode** toggle is saved instantly and re-applied on the next launch.
* **Remember my details** switch decides whether name/USN are stored.
* A **launch counter** is incremented on every fresh start of the app.
* A *"Stored values"* card reads the values back from SharedPreferences so persistence is visible on screen.

**Tab 2 – SQLite Notes (SQLite)**
* Student can **add**, **view**, **edit**, **delete** and **delete-all** notes.
* Each note is a row in the `notes` table (`id`, `title`, `content`, `created_at`).
* Rows are loaded from the database every time the screen opens, so the list is the same after restarting the app.

## 4. Project Folder & File Structure

```
Exp9_DataPersistence/
├── app/
│   ├── build.gradle.kts            # Module config: SDK versions, Compose, dependencies
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml     # App declaration + launcher activity
│       ├── java/com/example/exp9/
│       │   ├── MainActivity.kt     # Entry point; creates PrefsManager & DatabaseHelper, applies saved theme
│       │   ├── data/
│       │   │   ├── PrefsManager.kt     # SharedPreferences wrapper (save / read / clear)
│       │   │   └── DatabaseHelper.kt   # SQLiteOpenHelper + Note model + CRUD methods
│       │   └── ui/
│       │       ├── AppRoot.kt          # Scaffold: top bar, bottom navigation, snackbar
│       │       ├── PrefsScreen.kt      # Tab 1 – SharedPreferences UI
│       │       ├── NotesScreen.kt      # Tab 2 – SQLite CRUD UI
│       │       ├── Components.kt       # Reusable SectionCard, SwitchRow, KeyValueRow
│       │       └── theme/Theme.kt      # Light / dark Material 3 colour schemes
│       └── res/
│           ├── drawable/ic_launcher.xml
│           └── values/ (strings.xml, themes.xml)
├── screenshots/                    # Output + test case screenshots used in this README
├── build.gradle.kts                # Root Gradle file (plugin versions)
├── settings.gradle.kts             # Project name + repositories
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
├── .gitignore
└── README.md
```

| File | Responsibility |
|---|---|
| `PrefsManager.kt` | All SharedPreferences read/write logic in one place |
| `DatabaseHelper.kt` | Creates `exp9.db`, defines `notes` table, `insertNote / getAllNotes / updateNote / deleteNote / deleteAll` |
| `PrefsScreen.kt` | UI for profile form, switches and the live "Stored values" card |
| `NotesScreen.kt` | UI for note form, list of rows, edit/delete actions |
| `MainActivity.kt` | Restores dark-mode preference at launch and counts app launches |

## 5. Key Code Snippets

**Saving to SharedPreferences**
```kotlin
prefs.edit()
    .putString("name", name)
    .putString("usn", usn)
    .putBoolean("remember_me", true)
    .apply()
```

**Creating the SQLite table**
```kotlin
db.execSQL("""
    CREATE TABLE notes (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        content TEXT,
        created_at TEXT NOT NULL
    )
""")
```

**Inserting a row**
```kotlin
val values = ContentValues().apply {
    put("title", title); put("content", content); put("created_at", stamp)
}
writableDatabase.insert("notes", null, values)
```

## 6. How to Run

1. Open **Android Studio** → *File → Open* → select the `Exp9_DataPersistence` folder.
2. Wait for **Gradle Sync** to finish (internet needed the first time).
3. Start an emulator or connect a phone and press **Run ▶**.
4. Min SDK 24 (Android 7.0), Target SDK 35.

## 7. Output Screenshot

![Output](screenshots/output.png)

## 8. Test Cases

### Test Case 1 – SharedPreferences saves Name & USN (with USN and name)
| Item | Details |
|---|---|
| **Input** | Name = `Shubham Shivaji Kondikire`, USN = `25MCAR0102`, Remember = ON → tap **Save** |
| **Steps** | Save → force-close the app → reopen it |
| **Expected** | Greeting shows *Hello, Shubham Shivaji Kondikire*; both fields are pre-filled; launch counter has increased |
| **Result** | ✅ Pass |

![Test Case 1](screenshots/test_case_1.png)

### Test Case 2 – Dark mode preference persists
| Item | Details |
|---|---|
| **Input** | Turn **Dark mode** ON |
| **Steps** | Toggle → force-close the app → reopen it |
| **Expected** | App opens directly in dark theme; `dark_mode = true` in the Stored values card |
| **Result** | ✅ Pass |

![Test Case 2](screenshots/test_case_2.png)

### Test Case 3 – SQLite CRUD persists across restarts
| Item | Details |
|---|---|
| **Input** | Title = `Exp 9 – 25MCAR0102`, Content = `Shubham Shivaji Kondikire – SQLite test` |
| **Steps** | Add the note → edit it → add a second note → delete the second one → force-close and reopen the app |
| **Expected** | Only the edited first note remains in the list after reopening; counter shows `Saved notes (1)` |
| **Result** | ✅ Pass |

![Test Case 3](screenshots/test_case_3.png)

## 9. Conclusion

The experiment shows that **SharedPreferences** is ideal for small settings (profile, theme, flags) while **SQLite** is suited for structured, list-type records that need create / read / update / delete operations. Both keep data safely on the device so it is available again after the app is restarted.
