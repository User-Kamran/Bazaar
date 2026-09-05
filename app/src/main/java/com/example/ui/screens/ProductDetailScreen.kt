package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import kotlinx.coroutines.flow.firstOrNull
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, viewModel: BazaarViewModel, listingId: String) {
    val listings by viewModel.allListings.collectAsState(initial = emptyList())
    val listing = listings.find { it.id == listingId }
    val context = LocalContext.current
    
    var isFavorite by remember { mutableStateOf(false) }
    
    if (listing == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).background(Surface.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Text)
                }
                Box(
                    modifier = Modifier
                        .background(Surface.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Text, modifier = Modifier.size(16.dp))
                        Text("Search for More Products", fontSize = 12.sp, color = Text, fontWeight = FontWeight.Medium)
                    }
                }
                IconButton(
                    onClick = { /* info */ },
                    modifier = Modifier.size(40.dp).background(Surface.copy(alpha = 0.5f), CircleShape)
                ) {
                    Text("i", color = Primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        },
        bottomBar = {
            Surface(color = Surface, shadowElevation = 16.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.addToCart(listing) },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Border)
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = TextMuted, modifier = Modifier.padding(end = 8.dp))
                        Text("Add to Cart", color = TextMuted, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            viewModel.addToCart(listing)
                            navController.navigate("checkout")
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.horizontalGradient(listOf(Color(0xFF4A80FF), Color(0xFF1D4ED8)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Buy Now", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFEAF1FF) // Main blue gradient backdrop
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Image Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                // Background gradient specific to top area
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(BgTop, Surface))))
                
                // Image
                var hasImage = false
                if (listing.photoUrl.isNotEmpty()) {
                    val resId = context.resources.getIdentifier(listing.photoUrl, "drawable", context.packageName)
                    if (resId != 0) {
                        hasImage = true
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = listing.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 80.dp, bottom = 40.dp)
                        )
                    }
                }
                if (!hasImage) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("3D Viewer / Photos", color = TextMuted)
                    }
                }
                
                // 3D View Button Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Surface(
                        color = Surface.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { /* Simulate 3D View Action */ },
                        shadowElevation = 4.dp
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = "3D View", tint = Primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("View 3D", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Text)
                        }
                    }
                }
                
                // Floating Heart & Dots
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Dots
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(TextMuted, CircleShape))
                            Box(modifier = Modifier.size(6.dp).background(Border, CircleShape))
                            Box(modifier = Modifier.size(6.dp).background(Border, CircleShape))
                            Box(modifier = Modifier.size(6.dp).background(Border, CircleShape))
                            Box(modifier = Modifier.size(6.dp).background(Border, CircleShape))
                        }
                        
                        // Floating Heart
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(8.dp, CircleShape)
                                .background(Surface, CircleShape)
                                .clickable { isFavorite = !isFavorite },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Danger else TextMuted,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
            
            // Bottom White Card Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Surface, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                            Text(
                                text = listing.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Text,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = listing.description.split(".").firstOrNull() ?: "",
                                fontSize = 14.sp,
                                color = TextMuted
                            )
                        }
                        Text(
                            text = "₹${listing.price.toInt()}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Text
                        )
                    }
                    
                    Text(
                        text = "Color",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Text,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 32.dp)) {
                        ColorSelectorBox(Color(0xFFFF3B30), isSelected = true)
                        ColorSelectorBox(Color(0xFF3667E8), isSelected = false)
                        ColorSelectorBox(Color(0xFFE5E5EA), isSelected = false)
                        ColorSelectorBox(Color(0xFF1C1C1E), isSelected = false)
                    }
                    
                    Text(
                        text = "Description",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Text,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = listing.generatedDescription.ifEmpty { listing.description },
                        fontSize = 15.sp,
                        color = TextMuted,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(bottom = 48.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSelectorBox(color: Color, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .width(64.dp)
            .height(36.dp)
            .background(color, RoundedCornerShape(8.dp))
            .border(
                if (isSelected) 2.dp else 0.dp,
                if (isSelected) Primary else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
    )
}
