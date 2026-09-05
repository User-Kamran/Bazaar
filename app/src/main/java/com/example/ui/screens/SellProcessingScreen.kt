package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import kotlinx.coroutines.delay

@Composable
fun SellProcessingScreen(navController: NavController, viewModel: BazaarViewModel) {
    var step by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        // Simulate AI processing steps
        delay(1500)
        step = 1 // 3D Generation
        delay(1500)
        step = 2 // Background Removal
        delay(1500)
        step = 3 // Background Matching
        delay(1500)
        step = 4 // Description Writing
        delay(1500)
        step = 5 // Search Optimization
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AI is building your listing", style = MaterialTheme.typography.titleLarge, color = Text, modifier = Modifier.padding(bottom = 32.dp))
        
        val steps = listOf(
            "Generating 3D model",
            "Removing background",
            "Matching backgrounds across photos",
            "Writing product description",
            "Optimizing for search"
        )
        
        steps.forEachIndexed { index, title ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (index < step) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "Done", tint = Success)
                } else if (index == step) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Primary, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.HourglassEmpty, contentDescription = "Waiting", tint = TextMuted)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, style = MaterialTheme.typography.bodyLarge, color = if (index <= step) Text else TextMuted)
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        PrimaryButton(
            text = "Preview Listing",
            onClick = { navController.navigate("sell_preview") },
            enabled = step == 5
        )
    }
}
