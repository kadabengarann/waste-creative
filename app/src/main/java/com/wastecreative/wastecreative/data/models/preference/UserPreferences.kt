package com.wastecreative.wastecreative.data.models.preference


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wastecreative.wastecreative.data.models.ResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences private constructor( private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<ResponseItem> {
        return dataStore.data.map { preferences ->
            ResponseItem(
                "",
                preferences[PASSWORD] ?:"",
                preferences[IMGKEY] ?:"",
                preferences[IDKEY] ?:"",
                preferences[EMAILKEY] ?:"" ,
                preferences[NAMEKEY] ?:"",
                preferences[LOGINKEY]?: false

            )
        }
    }

    suspend fun loginPrefs(responseItem: ResponseItem) {
        dataStore.edit { preferences ->
            preferences[IDKEY] = responseItem.id
            preferences[LOGINKEY] = true
            preferences[NAMEKEY] = responseItem.username
            preferences[IMGKEY] = responseItem.avatar
            preferences[PASSWORD] = responseItem.password
            preferences[EMAILKEY] = responseItem.email
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
        private val NAMEKEY= stringPreferencesKey("username")
        private val LOGINKEY = booleanPreferencesKey("login")
        private val PASSWORD= stringPreferencesKey("password")
        private val IMGKEY = stringPreferencesKey("avatar")
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
