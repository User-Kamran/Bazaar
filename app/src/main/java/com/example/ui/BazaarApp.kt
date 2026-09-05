package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BazaarApp(viewModel: BazaarViewModel = viewModel()) {
    val navController = rememberNavController()
    
    // Background gradient per Section 3.1
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom))),
        color = androidx.compose.ui.graphics.Color.Transparent
    ) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") { LoginScreen(navController, viewModel) }
            composable("otp/{phone}") { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                OtpScreen(navController, viewModel, phone)
            }
            composable("role_prompt") { VoiceRolePromptScreen(navController, viewModel) }
            composable("payout_setup") { PayoutSetupScreen(navController, viewModel) }
            composable("home") { HomeScreen(navController, viewModel) }
            composable("product_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                ProductDetailScreen(navController, viewModel, id)
            }
            composable("cart") { CartScreen(navController, viewModel) }
            composable("checkout") { CheckoutScreen(navController, viewModel) }
            composable("add_card") { AddCardScreen(navController, viewModel) }
            composable("order_confirmation") { OrderConfirmationScreen(navController, viewModel) }
            composable("sell_details") { SellDetailsScreen(navController, viewModel) }
            composable("sell_media") { SellMediaScreen(navController, viewModel) }
            composable("sell_processing") { SellProcessingScreen(navController, viewModel) }
            composable("sell_preview") { SellPreviewScreen(navController, viewModel) }
            composable("your_listings") { YourListingsScreen(navController, viewModel) }
            composable("profile") { ProfileScreen(navController, viewModel) }
        }
    }
}
