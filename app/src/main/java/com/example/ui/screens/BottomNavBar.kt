package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String, viewModel: BazaarViewModel) {
    val user = viewModel.currentUser.collectAsState().value
    val isSeller = user?.role == "seller" || user?.role == "both"
    val cartItems = viewModel.cartItems.collectAsState().value
    val cartCount = cartItems.values.sum()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
    ) {
        // Background Row
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Surface)
                .border(1.dp, Border) // border-t
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 32.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Home
            NavBarItem(
                icon = Icons.Filled.Home,
                label = "Home",
                isSelected = currentRoute == "home",
                onClick = { if (currentRoute != "home") navController.navigate("home") }
            )
            
            // Listings
            NavBarItem(
                icon = Icons.Filled.List,
                label = "Listings",
                isSelected = currentRoute == "your_listings",
                onClick = { if (currentRoute != "your_listings") navController.navigate("your_listings") }
            )
            
            Spacer(modifier = Modifier.width(68.dp)) // Space for center FAB
            
            // Cart
            Box(
                modifier = Modifier.clickable { if (currentRoute != "cart") navController.navigate("cart") },
                contentAlignment = Alignment.Center
            ) {
                NavBarItem(
                    icon = Icons.Filled.ShoppingCart,
                    label = "Cart",
                    isSelected = currentRoute == "cart",
                    onClick = { if (currentRoute != "cart") navController.navigate("cart") }
                )
                if (cartCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                            .size(16.dp)
                            .background(Primary, CircleShape)
                            .border(2.dp, Surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cartCount.toString(),
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Profile
            NavBarItem(
                icon = Icons.Filled.Person,
                label = "Profile",
                isSelected = currentRoute == "profile",
                onClick = { if (currentRoute != "profile") navController.navigate("profile") }
            )
        }
        
        // Voice Button
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = PrimaryDark)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF3667E8), Color(0xFF0F2E86))))
                    .clickable { navController.navigate("sell_details") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Mic, contentDescription = "Voice Navigator", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Voice", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
        }
    }
}

@Composable
fun NavBarItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Primary else TextMuted,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Primary else TextMuted
        )
    }
}
