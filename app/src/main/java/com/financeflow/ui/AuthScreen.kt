package com.financeflow.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    title: String,
    submitLabel: String,
    onSubmit: (String, String) -> Unit = { _, _ -> },
    onSubmitWithConfirm: (String, String, String) -> Unit = { _, _, _ -> },
    onSwitch: () -> Unit,
    isRegister: Boolean = false,
    isLoading: Boolean = false
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmState = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary)
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        if (isRegister) {
            OutlinedTextField(
                value = confirmState.value,
                onValueChange = { confirmState.value = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
        Button(
            onClick = {
                if (isRegister) {
                    onSubmitWithConfirm(emailState.value, passwordState.value, confirmState.value)
                } else {
                    onSubmit(emailState.value, passwordState.value)
                }
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp))
            } else {
                Text(submitLabel)
            }
        }
        TextButtonSecondary(onClick = onSwitch, text = if (isRegister) "Have an account? Login" else "Need an account? Register")
    }
}

@Composable
private fun TextButtonSecondary(onClick: () -> Unit, text: String) {
    androidx.compose.material3.TextButton(onClick = onClick, modifier = Modifier.padding(top = 16.dp)) {
        Text(text)
    }
}
