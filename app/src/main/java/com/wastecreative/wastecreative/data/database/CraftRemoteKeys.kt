package com.wastecreative.wastecreative.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "craft_remote_keys")
data class CraftRemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)