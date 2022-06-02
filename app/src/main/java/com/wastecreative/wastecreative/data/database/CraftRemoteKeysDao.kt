package com.wastecreative.wastecreative.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CraftRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CraftRemoteKeys>)

    @Query("SELECT * FROM craft_remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): CraftRemoteKeys?

    @Query("DELETE FROM craft_remote_keys")
    suspend fun deleteRemoteKeys()
}