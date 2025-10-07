package com.finacedavid

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.finacedavid.features.login.LoginUiState
import com.finacedavid.ui.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsSetupActions() {
        composeRule.setContent {
            LoginScreen(
                state = LoginUiState.Setup(),
                onRegister = { _, _ -> },
                onAuthenticate = {},
                onSwitchMode = {}
            )
        }

        composeRule.onNodeWithText("Criar PIN").assertIsDisplayed()
        composeRule.onNodeWithText("Criar Senha").assertIsDisplayed()
    }
}
