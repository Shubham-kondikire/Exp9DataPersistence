package com.example.exp9.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Snapshot of everything currently stored in the SharedPreferences file. */
data class StoredPrefs(
    val name: String,
    val usn: String,
    val darkMode: Boolean,
    val rememberMe: Boolean,
    val launchCount: Int,
    val lastSaved: String
)

/**
 * Thin wrapper around SharedPreferences (key-value storage).
 * File on device: /data/data/com.example.exp9/shared_prefs/student_prefs.xml
 */
class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var name: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
        set(value) { prefs.edit().putString(KEY_NAME, value).apply() }

    var usn: String
        get() = prefs.getString(KEY_USN, "") ?: ""
        set(value) { prefs.edit().putString(KEY_USN, value).apply() }

    var darkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) { prefs.edit().putBoolean(KEY_DARK_MODE, value).apply() }

    var rememberMe: Boolean
        get() = prefs.getBoolean(KEY_REMEMBER, true)
        set(value) { prefs.edit().putBoolean(KEY_REMEMBER, value).apply() }

    val launchCount: Int
        get() = prefs.getInt(KEY_LAUNCH_COUNT, 0)

    fun incrementLaunchCount() {
        prefs.edit().putInt(KEY_LAUNCH_COUNT, launchCount + 1).apply()
    }

    /** Saves the profile. If rememberMe is false, name & USN are NOT stored. */
    fun saveProfile(name: String, usn: String, rememberMe: Boolean) {
        val stamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        prefs.edit()
            .putString(KEY_NAME, if (rememberMe) name else "")
            .putString(KEY_USN, if (rememberMe) usn else "")
            .putBoolean(KEY_REMEMBER, rememberMe)
            .putString(KEY_LAST_SAVED, stamp)
            .apply()
    }

    /** Removes the profile keys (dark mode and launch counter are kept). */
    fun clearProfile() {
        prefs.edit()
            .remove(KEY_NAME)
            .remove(KEY_USN)
            .remove(KEY_REMEMBER)
            .remove(KEY_LAST_SAVED)
            .apply()
    }

    fun snapshot() = StoredPrefs(
        name = name,
        usn = usn,
        darkMode = darkMode,
        rememberMe = rememberMe,
        launchCount = launchCount,
        lastSaved = prefs.getString(KEY_LAST_SAVED, "") ?: ""
    )

    companion object {
        const val FILE_NAME = "student_prefs"
        private const val KEY_NAME = "name"
        private const val KEY_USN = "usn"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_REMEMBER = "remember_me"
        private const val KEY_LAUNCH_COUNT = "launch_count"
        private const val KEY_LAST_SAVED = "last_saved"
    }
}
