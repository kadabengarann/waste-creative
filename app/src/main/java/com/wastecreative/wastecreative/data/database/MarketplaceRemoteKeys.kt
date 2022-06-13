package com.wastecreative.wastecreative.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marketplace_remote_keys")
data class MarketplaceRemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)