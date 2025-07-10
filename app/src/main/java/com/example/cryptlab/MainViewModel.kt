package com.example.cryptlab

import android.app.Application
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptlab.ui.theme.CryptoManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var plainText by mutableStateOf("")
    var encryptedBase64 by mutableStateOf("")
    var decryptedText by mutableStateOf("")

    init {
        viewModelScope.launch {
            EncryptedPrefs.getEncryptedData(application).collectLatest { (cipher, iv) ->
                if (!cipher.isNullOrEmpty() && !iv.isNullOrEmpty()) {
                    try {
                        val encryptedData = CryptoManager.EncryptedData(
                            ciphertext = Base64.decode(cipher, Base64.NO_WRAP),
                            iv = Base64.decode(iv, Base64.NO_WRAP)
                        )
                        encryptedBase64 = cipher
                        val decrypted = CryptoManager.decrypt(encryptedData)
                        decryptedText = decrypted
                        plainText = decrypted // populate text field
                    } catch (e: Exception) {
                        e.printStackTrace()
                        decryptedText = "Error decrypting"
                    }
                }
            }
        }
    }

    fun encryptAndPersist() {
        viewModelScope.launch {
            val encrypted = CryptoManager.encrypt(plainText)
            val cipherB64 = Base64.encodeToString(encrypted.ciphertext, Base64.NO_WRAP)
            val ivB64 = Base64.encodeToString(encrypted.iv, Base64.NO_WRAP)

            encryptedBase64 = cipherB64
            decryptedText = plainText // immediate feedback

            EncryptedPrefs.saveEncryptedData(getApplication(), cipherB64, ivB64)
        }
    }
}
