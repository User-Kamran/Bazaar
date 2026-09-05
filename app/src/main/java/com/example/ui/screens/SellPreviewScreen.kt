package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.model.Listing
import com.example.ui.components.OutlinedButton
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellPreviewScreen(navController: NavController, viewModel: BazaarViewModel) {
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview Listing") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        bottomBar = {
            Surface(color = Surface, shadowElevation = 8.dp) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(
                        text = "Edit Details",
                        onClick = { navController.navigate("sell_details") }, // Return to details
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = "Publish",
                        onClick = {
                            coroutineScope.launch {
                                // Create mock listing
                                val currentUser = viewModel.currentUser.value
                                val mockListing = Listing(
                                    sellerId = currentUser?.id ?: "mock_seller",
                                    name = "Handloom Cotton Stole", // Simulated output
                                    description = "Hand-woven on a pit loom using natural dyes. Price around 500 rupees. Each piece may vary slightly since it is entirely handmade.",
                                    generatedDescription = "Hand-woven on a pit loom using natural dyes. Each piece may vary slightly since it is entirely handmade.",
                                    price = 500.0,
                                    category = "Textiles",
                                    status = "live"
                                )
                                // If database was properly wired, save here. Since it's a prototype we can simulate state or actually save.
                                // It should be done via viewModel. 
                                // Since we haven't implemented addListing in viewModel, we can just navigate.
                                navController.navigate("your_listings") {
                                    popUpTo("home") { inclusive = false }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Border),
                contentAlignment = Alignment.Center
            ) {
                Text("AI Generated 3D Model & Cleaned Photos", color = TextMuted)
            }
            
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Handloom Cotton Stole",
                    style = MaterialTheme.typography.titleLarge,
                    color = Text,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "₹500.0",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryDark,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Divider(color = Border)
                
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    color = Text,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Text(
                    text = "Hand-woven on a pit loom using natural dyes. Each piece may vary slightly since it is entirely handmade.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Surface(color = BgTop, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "This is exactly how buyers will see your listing.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = PrimaryDark,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
