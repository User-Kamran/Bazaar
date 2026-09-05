package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.model.Listing
import com.example.ui.components.OutlinedButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourListingsScreen(navController: NavController, viewModel: BazaarViewModel) {
    val listings by viewModel.allListings.collectAsState(initial = emptyList())
    val context = LocalContext.current
    // In a real app we'd fetch listings specifically for the current seller
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Listings") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        bottomBar = { BottomNavBar(navController, "your_listings", viewModel) },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { innerPadding ->
        if (listings.isEmpty()) {
            // Because we mock listings or start empty
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No listings yet", style = MaterialTheme.typography.titleLarge, color = Text)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Tap the microphone icon to start your first listing with the Voice Navigator.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listings) { listing ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(64.dp).background(Border)) {
                                        if (listing.photoUrl.isNotEmpty()) {
                                            val resId = context.resources.getIdentifier(listing.photoUrl, "drawable", context.packageName)
                                            if (resId != 0) {
                                                Image(
                                                    painter = painterResource(id = resId),
                                                    contentDescription = listing.name,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(listing.name, style = MaterialTheme.typography.titleMedium)
                                        Text("₹${listing.price}", style = MaterialTheme.typography.titleMedium, color = PrimaryDark)
                                    }
                                }
                                Surface(
                                    color = if (listing.status == "live") Success.copy(alpha = 0.1f) else WarnBg,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = listing.status.uppercase(),
                                        color = if (listing.status == "live") Success else WarnText,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            // Simple analytics
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Units Sold", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                                    Text("0", style = MaterialTheme.typography.titleMedium, color = Text)
                                }
                                Column {
                                    Text("Revenue", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                                    Text("₹0", style = MaterialTheme.typography.titleMedium, color = Text)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                OutlinedButton(
                                    text = "Edit",
                                    onClick = { /* navigate to edit */ },
                                    modifier = Modifier.weight(1f).height(40.dp)
                                )
                                OutlinedButton(
                                    text = if (listing.status == "paused") "Resume" else "Pause",
                                    onClick = { /* toggle pause */ },
                                    modifier = Modifier.weight(1f).height(40.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
