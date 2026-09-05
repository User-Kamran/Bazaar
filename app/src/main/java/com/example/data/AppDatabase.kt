package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.*

@Database(
    entities = [
        User::class, 
        PayoutMethod::class, 
        Listing::class, 
        Media::class, 
        Order::class, 
        Review::class, 
        Address::class
    ], 
    version = 1, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun payoutMethodDao(): PayoutMethodDao
    abstract fun listingDao(): ListingDao
    abstract fun orderDao(): OrderDao
    abstract fun mediaDao(): MediaDao
}
