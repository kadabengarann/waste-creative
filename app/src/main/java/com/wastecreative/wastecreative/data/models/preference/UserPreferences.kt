package com.wastecreative.wastecreative.data.models.preference


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences private constructor( private val context: Context) {

    fun getUser(): Flow<UserModel> {
        return context.dataStores.data.map { preferences ->
            UserModel(
                preferences[IDKEY] ?: -1,
                preferences[NAMEKEY] ?:"",
                preferences[EMAILKEY] ?:"" ,
                preferences[LOGINKEY] ?: false,
                preferences[AVATARKEY] ?:"" ,
            )
        }
    }
    suspend fun loginPref(userModel: UserModel) {
        context.dataStores.edit { preferences ->
            preferences[IDKEY] = userModel.id
            preferences[NAMEKEY] = userModel.name
            preferences[LOGINKEY] = true
            preferences[EMAILKEY] = userModel.email
            preferences[AVATARKEY]  = userModel.avatar
        }
    }
    suspend fun logout() {
        context.dataStores.edit { preferences ->
            preferences[LOGINKEY ] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val IDKEY= intPreferencesKey("id")
        private val NAMEKEY= stringPreferencesKey("name")
        private val LOGINKEY = booleanPreferencesKey("logins")
        private val EMAILKEY = stringPreferencesKey("email")
        private val AVATARKEY = stringPreferencesKey("avatar")
        private val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = "settings")

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }


}
