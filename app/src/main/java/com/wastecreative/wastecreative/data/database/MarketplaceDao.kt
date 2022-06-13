package com.wastecreative.wastecreative.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarketplaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketplace(crafts: List<MarketplaceEntity>)

    @Query("SELECT * FROM marketplace")
    fun getAllMarketplace(): PagingSource<Int, MarketplaceEntity>

    @Query("DELETE FROM marketplace")
    suspend fun deleteAll()
}