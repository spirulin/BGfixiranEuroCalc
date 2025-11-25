package com.example.bgfixiraneurocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    dynamicDarkColorScheme(this) // Използва цветовете на Android системата
                } else {
                    darkColorScheme() // Тъмна тема по подразбиране
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConverterScreen()
                }
            }
        }
    }
}

@Composable
fun ConverterScreen() {
    // Фиксираният курс
    val fixedRate = 1.95583

    // Състояния за текстовите полета
    var bgnText by remember { mutableStateOf("") }
    var eurText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заглавие
        Text(
            text = "Конвертор",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // --- ПОЛЕ ЗА БЪЛГАРСКИ ЛЕВ (BGN) ---
        OutlinedTextField(
            value = bgnText,
            onValueChange = { newBgn ->
                // Позволяваме само цифри и точки
                if (newBgn.all { it.isDigit() || it == '.' }) {
                    bgnText = newBgn
                    // Логика за конвертиране към Евро
                    val bgnValue = newBgn.toDoubleOrNull()
                    if (bgnValue != null) {
                        val convertedEur = bgnValue / fixedRate
                        // Използваме Locale.US, за да сме сигурни, че разделителят е точка
                        eurText = String.format(Locale.US, "%.2f", convertedEur)
                    } else {
                        if (newBgn.isEmpty()) eurText = ""
                    }
                }
            },
            label = { Text("Български лев (BGN)") },
            textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Икона за размяна (визуална)
        Text(
            text = "⇅",
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- ПОЛЕ ЗА ЕВРО (EUR) ---
        OutlinedTextField(
            value = eurText,
            onValueChange = { newEur ->
                if (newEur.all { it.isDigit() || it == '.' }) {
                    eurText = newEur
                    // Логика за конвертиране към Лева
                    val eurValue = newEur.toDoubleOrNull()
                    if (eurValue != null) {
                        val convertedBgn = eurValue * fixedRate
                        bgnText = String.format(Locale.US, "%.2f", convertedBgn)
                    } else {
                        if (newEur.isEmpty()) bgnText = ""
                    }
                }
            },
            label = { Text("Евро (EUR)") },
            textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Бутон за изчистване
        Button(
            onClick = {
                bgnText = ""
                eurText = ""
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Изчисти", fontSize = 18.sp)
        }
    }
}
