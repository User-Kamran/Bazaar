package com.example.ui.screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.CustomTextField
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellDetailsScreen(navController: NavController, viewModel: BazaarViewModel) {
    var productName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    
    var aiPrompt by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    var speechRecognizer: SpeechRecognizer? by remember { mutableStateOf(null) }
    var isListeningToVoice by remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
            }
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { isListeningToVoice = true }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { isListeningToVoice = false }
                override fun onError(error: Int) { isListeningToVoice = false }
                override fun onResults(results: Bundle?) {
                    isListeningToVoice = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        aiPrompt = matches[0]
                        isGenerating = true
                        viewModel.generateAIContent(aiPrompt) { generatedText ->
                            description = generatedText
                            if (productName.isEmpty()) {
                                productName = aiPrompt.take(30) + "..."
                            }
                            isGenerating = false
                        }
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        aiPrompt = matches[0]
                    }
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
            speechRecognizer?.destroy()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        },
        bottomBar = {
            Surface(color = Surface, shadowElevation = 8.dp) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding()) {
                    PrimaryButton(
                        text = "Next: Add Photos",
                        onClick = { navController.navigate("sell_media") },
                        enabled = productName.isNotEmpty() && price.isNotEmpty()
                    )
                }
            }
        },
        containerColor = Color(0xFFEAF1FF)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            
            // AI Generation Section
            Surface(
                color = Surface,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = "AI", tint = Primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isListeningToVoice) "Listening..." else "Describe your product", fontWeight = FontWeight.Bold, color = Text)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                tts?.speak("Please describe your product.", TextToSpeech.QUEUE_FLUSH, null, null)
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                                    }
                                    speechRecognizer?.startListening(intent)
                                }, 1500)
                            }
                        ) {
                            Icon(Icons.Filled.Mic, contentDescription = "Speak", tint = if (isListeningToVoice) Danger else Primary)
                        }
                    }
                    
                    OutlinedTextField(
                        value = aiPrompt,
                        onValueChange = { aiPrompt = it },
                        placeholder = { Text("e.g., A vintage leather jacket...", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Border
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            isGenerating = true
                            viewModel.generateAIContent(aiPrompt) { generatedText ->
                                description = generatedText
                                if (productName.isEmpty()) {
                                    productName = aiPrompt.take(30) + "..."
                                }
                                isGenerating = false
                            }
                        },
                        enabled = aiPrompt.isNotEmpty() && !isGenerating,
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Generate Details", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Text("Manual Details", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
            
            CustomTextField(
                value = productName,
                onValueChange = { productName = it },
                label = "Product Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = price,
                onValueChange = { price = it },
                label = "Price (₹)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = TextMuted) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Border,
                    focusedBorderColor = Primary,
                    unfocusedContainerColor = Surface,
                    focusedContainerColor = Surface
                )
            )
        }
    }
}
