package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(navController: NavController, viewModel: BazaarViewModel) {
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var saveCard by remember { mutableStateOf(false) }
    
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal = cartItems.entries.sumOf { it.key.price * it.value }

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
                Text("Add Card", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Text)
                Spacer(modifier = Modifier.weight(1.5f))
            }
        },
        containerColor = Color(0xFFEAF1FF)
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
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Add Debit Card or Credit Card",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Text,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "All payment details are stored securely. By adding a new card, you won't be charged yet.",
                    fontSize = 14.sp,
                    color = TextMuted,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                Button(
                    onClick = { /* Scan card */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 8.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Surface),
                    border = BorderStroke(1.dp, Border)
                ) {
                    Text("Scan my card", color = Primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Card", fontWeight = FontWeight.Bold, color = Text, modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    placeholder = { Text("0000 0000 0000 0000", color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Expiry", fontWeight = FontWeight.Bold, color = Text, modifier = Modifier.padding(bottom = 8.dp))
                        OutlinedTextField(
                            value = expiry,
                            onValueChange = { expiry = it },
                            placeholder = { Text("MM/YY", color = TextMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CVV", fontWeight = FontWeight.Bold, color = Text, modifier = Modifier.padding(bottom = 8.dp))
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            placeholder = { Text("123", color = TextMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = saveCard,
                        onCheckedChange = { saveCard = it },
                        colors = CheckboxDefaults.colors(checkedColor = Primary)
                    )
                    Text("Set as default card", color = Text, fontWeight = FontWeight.Medium)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = {
                        viewModel.clearCart()
                        navController.navigate("order_confirmation") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    enabled = cardNumber.isNotEmpty() && expiry.isNotEmpty() && cvv.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 24.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (cardNumber.isNotEmpty()) Brush.horizontalGradient(listOf(Color(0xFF4A80FF), Color(0xFF1D4ED8))) else Brush.horizontalGradient(listOf(Color.LightGray, Color.Gray))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Continue   >>>", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
