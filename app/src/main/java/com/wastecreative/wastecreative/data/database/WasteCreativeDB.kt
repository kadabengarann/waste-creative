package com.wastecreative.wastecreative.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CraftEntity::class, CraftRemoteKeys::class, MarketplaceEntity::class, MarketplaceRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class WasteCreativeDB : RoomDatabase() {

    abstract fun remoteKeysDao(): CraftRemoteKeysDao
    abstract fun craftDao(): CraftDao
    abstract fun marketRemoteKeysDao(): MarketplaceRemoteKeysDao
    abstract fun marketDao(): MarketplaceDao

    companion object {
        @Volatile
        private var INSTANCE: WasteCreativeDB? = null

        @JvmStatic
        fun getDatabase(context: Context): WasteCreativeDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WasteCreativeDB::class.java, "wastecreative_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}