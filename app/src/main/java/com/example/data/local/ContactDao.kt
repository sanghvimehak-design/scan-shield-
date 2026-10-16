package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TrustedContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM trusted_contacts ORDER BY isPrimary DESC, name ASC")
    fun getAllContacts(): Flow<List<TrustedContact>>

    @Query("SELECT * FROM trusted_contacts WHERE isPrimary = 1 LIMIT 1")
    fun getPrimaryContact(): Flow<TrustedContact?>

    @Query("SELECT COUNT(*) FROM trusted_contacts")
    fun getContactCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: TrustedContact): Long

    @Update
    suspend fun updateContact(contact: TrustedContact)

    @Query("DELETE FROM trusted_contacts WHERE id = :id")
    suspend fun deleteContactById(id: Long)

    @Query("UPDATE trusted_contacts SET isPrimary = 0 WHERE id != :primaryId")
    suspend fun clearOtherPrimary(primaryId: Long)

    @Query("DELETE FROM trusted_contacts")
    suspend fun clearAll()
}
