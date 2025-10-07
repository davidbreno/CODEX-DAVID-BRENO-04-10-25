package com.finacedavid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.finacedavid.features.login.LoginUiState

@Composable
fun LoginScreen(
    state: LoginUiState,
    onRegister: (String, Boolean) -> Unit,
    onAuthenticate: (String) -> Unit,
    onSwitchMode: () -> Unit
) {
    val secret = remember { mutableStateOf("") }
    val isPinMode = when (state) {
        is LoginUiState.Login -> state.isPin
        else -> true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FINACE DAVID",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = secret.value,
            onValueChange = { value ->
                if (!isPinMode || value.length <= 6) secret.value = value.filter { it.isDigit() || !isPinMode }
                else secret.value = value
            },
            label = { Text(if (isPinMode) "PIN" else "Senha") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = if (isPinMode) KeyboardType.Number else KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        when (state) {
            is LoginUiState.Setup -> {
                Button(
                    onClick = { onRegister(secret.value, true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Criar PIN")
                }
                Button(
                    onClick = { onRegister(secret.value, false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Criar Senha")
                }
            }
            is LoginUiState.Login -> {
                Button(
                    onClick = { onAuthenticate(secret.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Entrar")
                }
                TextButton(onClick = onSwitchMode) {
                    Text(if (isPinMode) "Usar senha" else "Usar PIN")
                }
                state.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            else -> Unit
        }
    }
}
