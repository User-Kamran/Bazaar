package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.model.Listing
import com.example.ui.components.ProductCard
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel

import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: BazaarViewModel) {
    val listings by viewModel.allListings.collectAsState(initial = emptyList())
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Textiles", "Pottery", "Woodwork", "Jewelry")
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    
    val filteredListings = listings.filter { 
        (selectedCategory == "All" || it.category == selectedCategory) &&
        (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
    }
    
    Scaffold(
        bottomBar = { BottomNavBar(navController, "home", viewModel) },
        floatingActionButton = { 
            com.example.ui.components.VoiceNavigatorFab { query -> 
                val lowerQuery = query.lowercase()
                if (lowerQuery.contains("order") || lowerQuery.contains("profile") || lowerQuery.contains("listing") || lowerQuery.contains("account")) {
                    navController.navigate("profile")
                } else if (lowerQuery.contains("sell") || lowerQuery.contains("add")) {
                    navController.navigate("sell_details")
                } else if (lowerQuery.contains("cart") || lowerQuery.contains("checkout")) {
                    navController.navigate("cart")
                } else {
                    searchQuery = query
                }
            }
        },
        containerColor = androidx.compose.ui.graphics.Color(0xFFEAF1FF)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(BgTop, Surface)))
        ) {
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Top Bar: Location & Profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Deliver to", style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.5.sp, fontWeight = FontWeight.Medium), color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Jaipur, Rajasthan", style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold), color = Text)
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                    }
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                        .background(Surface)
                        .border(1.dp, Border, androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                        .clickable { navController.navigate("profile") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = "Profile", tint = TextMuted)
                }
            }
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                    .height(52.dp),
                placeholder = { Text("Search artisan crafts...", color = TextMuted, fontSize = 14.5.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextMuted) },
                trailingIcon = { Icon(Icons.Filled.Mic, contentDescription = "Voice Search", tint = Primary) },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Border,
                    focusedBorderColor = Primary,
                    unfocusedContainerColor = Surface,
                    focusedContainerColor = Surface
                ),
                singleLine = true
            )
            
            // Categories
            LazyRow(
                modifier = Modifier.padding(bottom = 16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                            .background(if (isSelected) Primary else Surface)
                            .border(if (isSelected) 0.dp else 1.dp, if (isSelected) androidx.compose.ui.graphics.Color.Transparent else Border, androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Surface else TextMuted,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    }
                }
            }
            
            // Product Grid
            if (filteredListings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (listings.isEmpty()) {
                        // Skeleton loading simulation if actually fetching
                        Text("No products available", color = TextMuted)
                    } else {
                        Text("No products found in this category", color = TextMuted)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredListings) { listing ->
                        ProductCard(modifier = Modifier.clickable { navController.navigate("product_detail/${listing.id}") }) {
                            // Image Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(128.dp)
                                    .background(androidx.compose.ui.graphics.Color(0xFFF8FAFC)),
                                contentAlignment = Alignment.Center
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
                            Column(modifier = Modifier.padding(12.dp).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                                Text(listing.name, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                Spacer(modifier = Modifier.height(4.dp))
                                Column {
                                    Text("₹${listing.price.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = PrimaryDark)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (listing.status == "live") {
                                        Text("LIVE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Surface, modifier = Modifier.background(Success, androidx.compose.foundation.shape.RoundedCornerShape(2.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                                    } else {
                                        Text("No reviews yet", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = TextMuted)
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
}
