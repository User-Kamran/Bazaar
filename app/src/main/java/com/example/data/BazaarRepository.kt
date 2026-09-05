package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.model.*

class BazaarRepository(private val db: AppDatabase) {
    val userDao = db.userDao()
    val payoutMethodDao = db.payoutMethodDao()
    val listingDao = db.listingDao()
    val orderDao = db.orderDao()
    val mediaDao = db.mediaDao()

    companion object {
        @Volatile
        private var INSTANCE: BazaarRepository? = null

        fun getInstance(context: Context): BazaarRepository {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bazaar_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                BazaarRepository(database).also { INSTANCE = it }
            }
        }
    }
}
