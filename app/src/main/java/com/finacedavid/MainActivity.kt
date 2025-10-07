package com.finacedavid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.finacedavid.ui.FinaceDavidRoot
import com.finacedavid.ui.theme.FinaceDavidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as FinaceDavidApp
        setContent {
            val themePreference by app.themeRepository.theme.collectAsState(initial = true)
            FinaceDavidTheme(darkTheme = themePreference) {
                val navController = rememberNavController()
                FinaceDavidRoot(
                    navController = navController,
                    app = app
                )
            }
        }
    }
}
