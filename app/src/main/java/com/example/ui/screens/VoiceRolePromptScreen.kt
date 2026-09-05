package com.example.ui.screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.OutlinedButton
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import com.example.viewmodel.BazaarViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun VoiceRolePromptScreen(navController: NavController, viewModel: BazaarViewModel) {
    var transcript by remember { mutableStateOf("Initializing Voice Assistant...") }
    val context = LocalContext.current
    
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var speechRecognizer: SpeechRecognizer? by remember { mutableStateOf(null) }
    var isListening by remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                    transcript = "Listening..."
                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { isListening = false }
                override fun onError(error: Int) {
                    isListening = false
                    transcript = "Didn't catch that. Tap below or say 'sell' or 'buy'."
                }
                override fun onResults(results: Bundle?) {
                    isListening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val result = matches[0].lowercase()
                        if (result.contains("sell") || result.contains("yes") || result.contains("haan")) {
                            viewModel.setRole("seller")
                            navController.navigate("payout_setup") { popUpTo("role_prompt") { inclusive = true } }
                        } else {
                            viewModel.setRole("buyer")
                            navController.navigate("home") { popUpTo("role_prompt") { inclusive = true } }
                        }
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
        
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                val prompt = "Welcome to Bazaar. Would you like to buy, sell, or both?"
                transcript = prompt
                tts?.speak(prompt, TextToSpeech.QUEUE_FLUSH, null, null)
                
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    }
                    speechRecognizer?.startListening(intent)
                }, 3500)
            }
        }
        
        onDispose {
            tts?.stop()
            tts?.shutdown()
            speechRecognizer?.destroy()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = if (isListening) Danger else Primary,
                strokeWidth = if (isListening) 5.dp else 3.dp,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Text(
            text = transcript,
            style = MaterialTheme.typography.titleLarge,
            color = Text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        
        PrimaryButton(
            text = "Yes, I want to sell",
            onClick = {
                viewModel.setRole("seller")
                navController.navigate("payout_setup") {
                    popUpTo("role_prompt") { inclusive = true }
                }
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedButton(
            text = "No, I am a buyer",
            onClick = {
                viewModel.setRole("buyer")
                navController.navigate("home") {
                    popUpTo("role_prompt") { inclusive = true }
                }
            }
        )
    }
}
