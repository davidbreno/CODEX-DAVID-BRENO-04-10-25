package com.financeflow.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.financeflow.R
import com.financeflow.ui.components.GradientBackground

@Composable
fun AuthScreen(
    title: String,
    subtitle: String? = null,
    submitLabel: String,
    onSubmit: (String, String) -> Unit = { _, _ -> },
    onSubmitWithConfirm: (String, String, String) -> Unit = { _, _, _ -> },
    onSwitch: () -> Unit,
    switchText: String,
    isRegister: Boolean = false,
    isLoading: Boolean = false
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmState = remember { mutableStateOf("") }
    val rememberMeState = remember { mutableStateOf(false) }
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color(0xFF6E8BFF),
        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
        cursorColor = Color.White,
        containerColor = Color(0x331E1A5B),
        textColor = Color.White,
        focusedTrailingIconColor = Color.White,
        unfocusedTrailingIconColor = Color.White.copy(alpha = 0.7f)
    )

    GradientBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            AuthWaveDecoration(modifier = Modifier.align(Alignment.TopCenter))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x401E1A5B)),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF6E8BFF), Color(0xFFB583F0))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "FD", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                        OutlinedTextField(
                            value = emailState.value,
                            onValueChange = { emailState.value = it },
                            label = { Text(text = stringResource(id = R.string.email)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = passwordState.value,
                            onValueChange = { passwordState.value = it },
                            label = { Text(text = stringResource(id = R.string.password)) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors
                        )
                        if (isRegister) {
                            OutlinedTextField(
                                value = confirmState.value,
                                onValueChange = { confirmState.value = it },
                                label = { Text(text = stringResource(id = R.string.confirm_password)) },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = rememberMeState.value,
                                    onCheckedChange = { rememberMeState.value = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF6E8BFF),
                                        uncheckedColor = Color.White.copy(alpha = 0.6f)
                                    )
                                )
                                Text(text = stringResource(id = R.string.remember_me))
                            }
                            TextButton(onClick = { }) {
                                Text(text = stringResource(id = R.string.forgot_password))
                            }
                        }
                        Button(
                            onClick = {
                                if (isRegister) {
                                    onSubmitWithConfirm(emailState.value, passwordState.value, confirmState.value)
                                } else {
                                    onSubmit(emailState.value, passwordState.value)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6E8BFF))
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(text = submitLabel, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(onClick = onSwitch) {
                            Text(text = switchText)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AuthWaveDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(220.dp)) {
        val width = size.width
        val height = size.height
        val wavePath = Path().apply {
            moveTo(0f, height * 0.6f)
            cubicTo(width * 0.25f, height * 0.4f, width * 0.35f, height * 0.9f, width * 0.6f, height * 0.7f)
            cubicTo(width * 0.8f, height * 0.5f, width * 0.9f, height * 0.9f, width, height * 0.65f)
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }
        drawPath(
            path = wavePath,
            brush = Brush.verticalGradient(
                colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent)
            )
        )
    }
}
