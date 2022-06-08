package com.wastecreative.wastecreative.data.models.model.preference


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
                preferences[IDKEY] ?:"",
                preferences[NAMEKEY] ?:"",
                preferences[EMAILKEY] ?:"" ,
                preferences[LOGINKEY] ?: false
            )
        }
    }
    suspend fun loginPref(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[IDKEY] =  userModel.userId
            preferences[NAMEKEY] = userModel.name
            preferences[LOGINKEY] = userModel.isLogin
            preferences[EMAILKEY] = userModel.email
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val IDKEY= stringPreferencesKey("id")
        private val NAMEKEY= stringPreferencesKey("name")
        private val LOGINKEY = booleanPreferencesKey("login")
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
