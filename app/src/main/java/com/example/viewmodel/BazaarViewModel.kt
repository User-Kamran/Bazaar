package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BazaarRepository
import com.example.model.Listing
import com.example.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class BazaarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BazaarRepository.getInstance(application)

    init {
        // Pre-populate some dummy listings for the prototype
        viewModelScope.launch {
            val listings = repository.listingDao.getLiveListings().firstOrNull()
            if (listings.isNullOrEmpty()) {
                repository.listingDao.insertListing(
                    Listing(
                        sellerId = "mock_seller_1",
                        name = "Handloom Cotton Stole",
                        description = "Hand-woven on a pit loom using natural dyes. Each piece may vary slightly.",
                        generatedDescription = "",
                        price = 500.0,
                        category = "Textiles",
                        status = "live",
                        photoUrl = "product_stole_1788554115195"
                    )
                )
                repository.listingDao.insertListing(
                    Listing(
                        sellerId = "mock_seller_2",
                        name = "Terracotta Clay Pot",
                        description = "Beautiful handcrafted Indian clay pot, intricate traditional design.",
                        generatedDescription = "",
                        price = 850.0,
                        category = "Pottery",
                        status = "live",
                        photoUrl = "product_terracotta_vase_1788553077168"
                    )
                )
                repository.listingDao.insertListing(
                    Listing(
                        sellerId = "mock_seller_3",
                        name = "Noise Buds VS104",
                        description = "Truly Wireless Earbuds. Black color, up to 45 hours of playtime.",
                        generatedDescription = "Immerse yourself in premium audio with these true wireless earbuds. Featuring a sleek black finish, comfortable fit, and robust battery life.",
                        price = 699.0,
                        category = "Electronics",
                        status = "live",
                        photoUrl = "earbuds_product_1788551501842"
                    )
                )
                repository.listingDao.insertListing(
                    Listing(
                        sellerId = "mock_seller_4",
                        name = "Indian Silk Saree",
                        description = "Elegant handwoven Indian silk saree, vibrant red and gold colors.",
                        generatedDescription = "Drape yourself in elegance with this authentic handwoven silk saree, featuring exquisite gold zari work on vibrant red silk.",
                        price = 4500.0,
                        category = "Clothing",
                        status = "live",
                        photoUrl = "product_silk_saree_1788553093904"
                    )
                )
                repository.listingDao.insertListing(
                    Listing(
                        sellerId = "mock_seller_5",
                        name = "Carved Wooden Box",
                        description = "Intricately carved wooden jewelry box, traditional Indian design.",
                        generatedDescription = "Store your precious items in this beautifully carved wooden box, featuring intricate traditional Indian floral motifs.",
                        price = 1200.0,
                        category = "Woodwork",
                        status = "live",
                        photoUrl = "product_wooden_box_1788553108998"
                    )
                )
            }
        }
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()
    
    val allListings = repository.listingDao.getLiveListings()
    
    // For cart
    private val _cartItems = MutableStateFlow<Map<Listing, Int>>(emptyMap())
    val cartItems = _cartItems.asStateFlow()

    fun login(phone: String, isNew: Boolean = false) {
        viewModelScope.launch {
            // Simulated login. In real app, OTP verification.
            val user = repository.userDao.getUserByPhone(phone).firstOrNull()
            if (user == null && isNew) {
                val newUser = User(phone = phone)
                repository.userDao.insertUser(newUser)
                _currentUser.value = newUser
            } else {
                _currentUser.value = user ?: User(phone = phone)
            }
        }
    }
    
    fun setRole(role: String) {
        _currentUser.value?.let { user ->
            val updated = user.copy(role = role)
            viewModelScope.launch {
                repository.userDao.updateUser(updated)
                _currentUser.value = updated
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
    }

    fun addToCart(listing: Listing) {
        val current = _cartItems.value.toMutableMap()
        current[listing] = (current[listing] ?: 0) + 1
        _cartItems.value = current
    }

    fun updateCartQuantity(listing: Listing, quantity: Int) {
        val current = _cartItems.value.toMutableMap()
        if (quantity <= 0) {
            current.remove(listing)
        } else {
            current[listing] = quantity
        }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    fun generateAIContent(prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                if (apiKey.isEmpty()) {
                    withContext(kotlinx.coroutines.Dispatchers.Main) {
                        onResult("AI Description: This is a beautiful artisanal item (API key missing).")
                    }
                    return@launch
                }
                
                val url = java.net.URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                val connection = url.openConnection() as java.net.HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                
                val jsonInputString = """
                    {
                        "contents": [{
                            "parts": [{"text": "Write a compelling product description for: $prompt"}]
                        }]
                    }
                """.trimIndent()
                
                connection.outputStream.use { os ->
                    val input = jsonInputString.toByteArray(Charsets.UTF_8)
                    os.write(input, 0, input.size)
                }
                
                val responseCode = connection.responseCode
                if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = org.json.JSONObject(response)
                    val text = jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                        
                    withContext(kotlinx.coroutines.Dispatchers.Main) {
                        onResult(text.trim())
                    }
                } else {
                    withContext(kotlinx.coroutines.Dispatchers.Main) {
                        onResult("Could not generate description.")
                    }
                }
            } catch (e: Exception) {
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    onResult("Error generating description: ${e.message}")
                }
            }
        }
    }
}
