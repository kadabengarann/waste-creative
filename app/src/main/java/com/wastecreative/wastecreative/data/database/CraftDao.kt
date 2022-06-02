package com.wastecreative.wastecreative.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrafts(crafts: List<CraftEntity>)

    @Query("SELECT * FROM craft")
    fun getAllCrafts(): PagingSource<Int, CraftEntity>

    @Query("DELETE FROM craft")
    suspend fun deleteAll()
}