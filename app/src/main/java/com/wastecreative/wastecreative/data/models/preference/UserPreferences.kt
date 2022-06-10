package com.wastecreative.wastecreative.data.models.preference


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences private constructor( private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(

                preferences[NAMEKEY] ?:"",
                preferences[EMAILKEY] ?:"" ,
                preferences[LOGINKEY] ?: false
            )
        }
    }
    suspend fun loginPref(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAMEKEY] = userModel.name
            preferences[LOGINKEY] = true
            preferences[EMAILKEY] = userModel.email
        }
    }
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[LOGINKEY ] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val IDKEY= stringPreferencesKey("id")
        private val NAMEKEY= stringPreferencesKey("name")
        private val LOGINKEY = booleanPreferencesKey("logins")
        private val EMAILKEY = stringPreferencesKey("email")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }


}
