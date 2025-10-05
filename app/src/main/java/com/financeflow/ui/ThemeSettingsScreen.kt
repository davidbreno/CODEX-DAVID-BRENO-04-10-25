package com.financeflow.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financeflow.ui.theme.FinanceFlowGradient
import com.financeflow.ui.theme.FinanceFlowPalettes

@Composable
fun ThemeSettingsScreen(
    selectedTheme: String,
    onSelect: (FinanceFlowGradient) -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Select Theme", style = MaterialTheme.typography.headlineSmall)
        FinanceFlowPalettes.all.forEach { palette ->
            ThemeCard(
                palette = palette,
                isSelected = palette.name == selectedTheme,
                onSelect = onSelect
            )
        }
    }
}

@Composable
private fun ThemeCard(palette: FinanceFlowGradient, isSelected: Boolean, onSelect: (FinanceFlowGradient) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(palette) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = palette.name, style = MaterialTheme.typography.titleMedium)
            Canvas(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(40.dp)
                    .fillMaxWidth(0.3f)
            ) {
                drawRect(brush = palette.gradient)
            }
        }
    }
}
