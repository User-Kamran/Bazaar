package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    fun getUserByPhone(phone: String): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}

@Dao
interface PayoutMethodDao {
    @Query("SELECT * FROM payout_methods WHERE userId = :userId")
    fun getPayoutMethods(userId: String): Flow<List<PayoutMethod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayoutMethod(method: PayoutMethod)
}

@Dao
interface ListingDao {
    @Query("SELECT * FROM listings WHERE status = 'live' ORDER BY createdAt DESC")
    fun getLiveListings(): Flow<List<Listing>>

    @Query("SELECT * FROM listings WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getListingsBySeller(sellerId: String): Flow<List<Listing>>

    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    fun getListingById(id: String): Flow<Listing?>
    
    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    suspend fun getListingByIdNow(id: String): Listing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: Listing)

    @Update
    suspend fun updateListing(listing: Listing)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getOrdersForBuyer(buyerId: String): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE listingId IN (SELECT id FROM listings WHERE sellerId = :sellerId) ORDER BY createdAt DESC")
    fun getOrdersForSeller(sellerId: String): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)
}

@Dao
interface MediaDao {
    @Query("SELECT * FROM media WHERE listingId = :listingId")
    fun getMediaForListing(listingId: String): Flow<List<Media>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: Media)
}
