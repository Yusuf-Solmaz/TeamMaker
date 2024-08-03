package com.yusuf.navigation.main_datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.myPreferencesDataStore: DataStore<Preferences> by preferencesDataStore( "settings")

@Singleton
class MainDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val myPreferencesDataStore = context.myPreferencesDataStore

    private object PreferencesKeys {
        val APP_ENTRY_KEY = booleanPreferencesKey("app_entry")
        val TOOLTIP_SHOWN_KEY = booleanPreferencesKey("tooltip_shown")
    }

    val readAppEntry: Flow<Boolean> = myPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.APP_ENTRY_KEY] ?: true
        }

    val readTooltipShown: Flow<Boolean> = myPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.TOOLTIP_SHOWN_KEY] ?: false
        }

    suspend fun saveAppEntry() {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_ENTRY_KEY] = false
        }
    }

    suspend fun saveTooltipShown() {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.TOOLTIP_SHOWN_KEY] = true
        }
    }
}