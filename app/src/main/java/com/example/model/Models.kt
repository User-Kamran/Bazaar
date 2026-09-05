package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val phone: String,
    val name: String = "",
    val role: String = "buyer", // buyer, seller, both
    val preferredLanguage: String = "English",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "payout_methods")
data class PayoutMethod(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val type: String, // upi, card, bank
    val tokenizedReference: String,
    val verified: Boolean = false
)

@Entity(tableName = "listings")
data class Listing(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val sellerId: String,
    val name: String,
    val description: String,
    val generatedDescription: String = "",
    val price: Double,
    val category: String = "",
    val status: String = "processing", // processing, live, paused
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "media")
data class Media(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val listingId: String,
    val type: String, // photo, video, model_3d
    val storageUrl: String,
    val processingStatus: String = "pending"
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val buyerId: String,
    val listingId: String,
    val quantity: Int,
    val total: Double,
    val status: String = "placed", // placed, packed, shipped, delivered, cancelled
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val orderId: String,
    val buyerId: String,
    val listingId: String,
    val rating: Int,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val label: String,
    val fullAddress: String,
    val isDefault: Boolean = false
)
