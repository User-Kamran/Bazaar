package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: BazaarViewModel) {
    val user by viewModel.currentUser.collectAsState()
    var isSeller = user?.role == "seller" || user?.role == "both"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        bottomBar = { BottomNavBar(navController, "profile", viewModel) },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = user?.name?.ifEmpty { user?.phone } ?: "Guest User",
                    style = MaterialTheme.typography.titleLarge,
                    color = Text
                )
                Text(
                    text = user?.phone ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Role Toggle
                Surface(
                    color = Surface,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Selling on Bazaar", style = MaterialTheme.typography.titleMedium, color = Text)
                            Text("Turn this on to sell your products", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                        }
                        Switch(
                            checked = isSeller,
                            onCheckedChange = { 
                                val newRole = if (it) "both" else "buyer"
                                viewModel.setRole(newRole)
                                if (it) navController.navigate("payout_setup")
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = Primary.copy(alpha = 0.5f))
                        )
                    }
                }

                ProfileMenuItem(icon = Icons.Filled.Payment, title = "Payment & Payout Methods")
                ProfileMenuItem(icon = Icons.Filled.ShoppingBag, title = "Your Orders")
                ProfileMenuItem(icon = Icons.Filled.LocationOn, title = "Addresses")
                ProfileMenuItem(icon = Icons.Filled.Language, title = "Language")
                ProfileMenuItem(icon = Icons.Filled.Help, title = "Help & Support")
                ProfileMenuItem(icon = Icons.Filled.PrivacyTip, title = "Privacy Policy")
                ProfileMenuItem(icon = Icons.Filled.Description, title = "Terms & Conditions")

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.titleMedium,
                    color = Danger,
                    modifier = Modifier
                        .clickable { 
                            viewModel.logout()
                            navController.navigate("login") { popUpTo("home") { inclusive = true } }
                        }
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = Text, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Go", tint = TextMuted, modifier = Modifier.size(16.dp))
    }
}
