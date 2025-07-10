package com.example.cryptlab

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("encrypted_prefs")

object EncryptedPrefs {
    private val CIPHERTEXT_KEY = stringPreferencesKey("ciphertext")
    private val IV_KEY = stringPreferencesKey("iv")

    suspend fun saveEncryptedData(context: Context, ciphertext: String, iv: String) =
        context.dataStore.edit { prefs ->
            prefs[CIPHERTEXT_KEY] = ciphertext
            prefs[IV_KEY] = iv
        }

    fun getEncryptedData(context: Context): Flow<Pair<String?, String?>> =
        context.dataStore.data.map { prefs ->
            Pair(prefs[CIPHERTEXT_KEY], prefs[IV_KEY])
        }
}
