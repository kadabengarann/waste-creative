package com.wastecreative.wastecreative.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarketplaceRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<MarketplaceRemoteKeys>)

    @Query("SELECT * FROM marketplace_remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): MarketplaceRemoteKeys?

    @Query("DELETE FROM marketplace_remote_keys")
    suspend fun deleteRemoteKeys()
}