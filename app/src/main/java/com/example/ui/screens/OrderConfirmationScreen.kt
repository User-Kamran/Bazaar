package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

@Composable
fun OrderConfirmationScreen(navController: NavController, viewModel: BazaarViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = "Success",
            tint = Success,
            modifier = Modifier.size(80.dp).padding(bottom = 24.dp)
        )
        Text(
            text = "Order Confirmed!",
            style = MaterialTheme.typography.titleLarge,
            color = Text,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Order #BZR-${(10000..99999).random()}",
            style = MaterialTheme.typography.titleMedium,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(
            text = "Expect delivery within 3-5 business days.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Text,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        PrimaryButton(
            text = "Continue Shopping",
            onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
        )
    }
}
