package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, viewModel: BazaarViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal = cartItems.entries.sumOf { it.key.price * it.value }
    var selectedMethod by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Text)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("Choose the Payment Method", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Text)
                Spacer(modifier = Modifier.weight(1.5f))
            }
        },
        containerColor = Color(0xFFEAF1FF) // Main blue gradient backdrop
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(BgTop, Surface)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Stepper
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepItem("Cart", isActive = false)
                    Box(modifier = Modifier.weight(1f).height(2.dp).background(Color.White))
                    StepItem("Payment", isActive = true)
                    Box(modifier = Modifier.weight(1f).height(2.dp).background(Color.White))
                    StepItem("Delivered", isActive = false)
                }
                
                // Credit Card Illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF333333), Color(0xFF111111))))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Bazaar Card", color = Color.White, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                                Box(modifier = Modifier.size(32.dp).background(Color.Red.copy(alpha = 0.8f), CircleShape))
                                Box(modifier = Modifier.size(32.dp).background(Color(0xFFFF9800).copy(alpha = 0.8f), CircleShape))
                            }
                        }
                        
                        Text("1234  1234  1234  1234", color = Color.White, fontSize = 22.sp, letterSpacing = 2.sp)
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("11/25", color = Color.White)
                            Text("₹${subtotal.toInt()}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
                
                // Bottom White Card Area for Payment Options
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Surface, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Divider(modifier = Modifier.weight(1f), color = Border)
                                Text("or", color = TextMuted, modifier = Modifier.padding(horizontal = 16.dp))
                                Divider(modifier = Modifier.weight(1f), color = Border)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            PaymentMethodItem("Google Pay", "gpay", selectedMethod) { selectedMethod = "gpay" }
                            PaymentMethodItem("Add New Card", "card", selectedMethod) { selectedMethod = "card" }
                            PaymentMethodItem("Cash on Delivery", "cod", selectedMethod) { selectedMethod = "cod" }
                        }
                        
                        Button(
                            onClick = {
                                if (selectedMethod == "card") {
                                    navController.navigate("add_card")
                                } else {
                                    viewModel.clearCart()
                                    navController.navigate("order_confirmation") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                }
                            },
                            enabled = selectedMethod.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 8.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (selectedMethod.isNotEmpty()) Brush.horizontalGradient(listOf(Color(0xFF4A80FF), Color(0xFF1D4ED8))) else Brush.horizontalGradient(listOf(Color.LightGray, Color.Gray))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Continue   >>>", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepItem(label: String, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (isActive) Color(0xFF4A80FF) else Surface, CircleShape)
                .border(2.dp, if (isActive) Color(0xFF4A80FF) else Border, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text(label.first().toString(), color = TextMuted, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium, color = if (isActive) Text else TextMuted)
    }
}

@Composable
fun PaymentMethodItem(label: String, id: String, selectedId: String, onClick: () -> Unit) {
    val isSelected = id == selectedId
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) BgTop else Surface)
            .border(1.dp, if (isSelected) Primary else Border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (isSelected) Primary else Surface, CircleShape)
                    .border(1.dp, if (isSelected) Primary else TextMuted, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontWeight = FontWeight.SemiBold, color = Text)
        }
    }
}
