package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.CustomTextField
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(navController: NavController, viewModel: BazaarViewModel, phone: String) {
    var otp by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var timer by remember { mutableIntStateOf(30) }
    var attempts by remember { mutableIntStateOf(0) }
    var isLocked by remember { mutableStateOf(false) }
    
    LaunchedEffect(timer) {
        if (timer > 0) {
            delay(1000)
            timer--
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Verify your number",
                style = MaterialTheme.typography.titleLarge,
                color = Text,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "We sent a 6-digit code to $phone",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            CustomTextField(
                value = otp,
                onValueChange = { 
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        otp = it 
                        error = ""
                    }
                },
                label = "6-digit code",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = Danger,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (isLocked) {
                Surface(
                    color = WarnBg,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Too many attempts. Please wait 5 minutes before trying again.",
                        color = WarnText,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                PrimaryButton(
                    text = "Verify",
                    onClick = {
                        if (otp == "123456") {
                            // Dummy success. In real app, check if new user or existing
                            viewModel.login(phone, isNew = true)
                            // If new user -> role_prompt, if existing -> home. Simulating new user here:
                            navController.navigate("role_prompt") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            attempts++
                            if (attempts >= 3) {
                                isLocked = true
                                error = ""
                            } else {
                                error = "Incorrect code. Please check and try again."
                            }
                        }
                    },
                    enabled = otp.length == 6
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (timer > 0) "Resend code in ${timer}s" else "Resend code",
                    color = if (timer > 0) TextMuted else Primary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.clickable(enabled = timer == 0 && !isLocked) {
                        timer = 30
                        error = ""
                        // API call to resend OTP
                    }
                )
            }
        }
    }
}
