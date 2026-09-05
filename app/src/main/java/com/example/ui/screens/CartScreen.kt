package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: BazaarViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val context = LocalContext.current
    
    val subtotal = cartItems.entries.sumOf { it.key.price * it.value }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(color = Surface, shadowElevation = 8.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.titleMedium, color = Text)
                            Text("₹$subtotal", style = MaterialTheme.typography.titleLarge, color = PrimaryDark)
                        }
                        PrimaryButton(
                            text = "Proceed to checkout",
                            onClick = { navController.navigate("checkout") }
                        )
                    }
                }
            }
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Your cart is empty", style = MaterialTheme.typography.titleLarge, color = Text)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Looks like you haven't added any products to your cart yet. Explore the marketplace to find unique items.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                PrimaryButton(
                    text = "Start Shopping",
                    onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems.entries.toList()) { (listing, quantity) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(Border)
                            ) {
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(listing.name, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                                Text("₹${listing.price}", style = MaterialTheme.typography.titleMedium, color = PrimaryDark)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(listing, quantity - 1) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(if (quantity == 1) Icons.Filled.Delete else Icons.Filled.Remove, contentDescription = "Decrease", tint = Danger)
                                    }
                                    Text("$quantity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(listing, quantity + 1) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = "Increase", tint = Primary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
