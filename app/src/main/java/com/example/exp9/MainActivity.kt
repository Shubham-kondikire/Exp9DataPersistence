package com.example.exp9

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.exp9.data.DatabaseHelper
import com.example.exp9.data.PrefsManager
import com.example.exp9.ui.AppRoot
import com.example.exp9.ui.theme.Exp9Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PrefsManager(applicationContext)
        val db = DatabaseHelper(applicationContext)

        // Count a "launch" only on a fresh start (not on rotation)
        if (savedInstanceState == null) prefs.incrementLaunchCount()

        setContent {
            // Theme is restored from SharedPreferences every time the app starts
            var darkMode by remember { mutableStateOf(prefs.darkMode) }

            DisposableEffect(darkMode) {
                val barStyle = if (darkMode) {
                    SystemBarStyle.dark(AndroidColor.TRANSPARENT)
                } else {
                    SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
                }
                this@MainActivity.enableEdgeToEdge(
                    statusBarStyle = barStyle,
                    navigationBarStyle = barStyle
                )
                onDispose { }
            }

            Exp9Theme(darkTheme = darkMode) {
                AppRoot(
                    prefs = prefs,
                    db = db,
                    darkMode = darkMode,
                    onDarkModeChange = {
                        prefs.darkMode = it   // persist immediately
                        darkMode = it
                    }
                )
            }
        }
    }
}
