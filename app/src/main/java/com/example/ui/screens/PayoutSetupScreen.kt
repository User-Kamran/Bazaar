package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.CustomTextField
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

@Composable
fun PayoutSetupScreen(navController: NavController, viewModel: BazaarViewModel) {
    var selectedMethod by remember { mutableStateOf("upi") }
    var upiId by remember { mutableStateOf("") }
    var bankAccount by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var readBackConfirmed by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "How would you like to get paid?",
            style = MaterialTheme.typography.titleLarge,
            color = Text,
            modifier = Modifier.padding(bottom = 24.dp, top = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterChip(
                selected = selectedMethod == "upi",
                onClick = { selectedMethod = "upi"; readBackConfirmed = false },
                label = { Text("UPI") }
            )
            FilterChip(
                selected = selectedMethod == "bank",
                onClick = { selectedMethod = "bank"; readBackConfirmed = false },
                label = { Text("Bank Account") }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        if (selectedMethod == "upi") {
            CustomTextField(
                value = upiId,
                onValueChange = { upiId = it; readBackConfirmed = false },
                label = "UPI ID (e.g. name@bank)"
            )
        } else if (selectedMethod == "bank") {
            CustomTextField(
                value = bankAccount,
                onValueChange = { bankAccount = it; readBackConfirmed = false },
                label = "Account Number"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = ifscCode,
                onValueChange = { ifscCode = it; readBackConfirmed = false },
                label = "IFSC Code"
            )
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Danger,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!readBackConfirmed && ((selectedMethod == "upi" && upiId.isNotEmpty()) || (selectedMethod == "bank" && bankAccount.isNotEmpty()))) {
            Surface(
                color = BgBottom,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Voice Assistant read-back:",
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary
                    )
                    Text(
                        text = if (selectedMethod == "upi") "I heard $upiId. Is that right?" else "I heard account $bankAccount. Is that right?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Text
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(text = "Yes, that's correct", onClick = { readBackConfirmed = true })
                }
            }
        } else {
            PrimaryButton(
                text = "Save and continue",
                onClick = {
                    val trimmedUpi = upiId.trim()
                    val trimmedBank = bankAccount.trim()
                    val trimmedIfsc = ifscCode.trim()
                    
                    if (selectedMethod == "upi") {
                        val upiRegex = Regex("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$")
                        if (!upiRegex.matches(trimmedUpi)) {
                            error = "Please enter a valid UPI ID format (e.g., name@bank)"
                            return@PrimaryButton
                        }
                    } else if (selectedMethod == "bank") {
                        val ifscRegex = Regex("^[A-Z]{4}0[A-Z0-9]{6}$")
                        if (trimmedBank.isEmpty()) {
                            error = "Account number cannot be empty"
                            return@PrimaryButton
                        }
                        if (!ifscRegex.matches(trimmedIfsc)) {
                            error = "Please enter a valid IFSC code (e.g., SBIN0123456)"
                            return@PrimaryButton
                        }
                    }
                    navController.navigate("home") {
                        popUpTo("payout_setup") { inclusive = true }
                    }
                },
                enabled = readBackConfirmed
            )
        }
    }
}
