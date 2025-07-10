package com.example.cryptlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MainViewModel = viewModel()

            Scaffold(
                topBar = { TopAppBar(title = { Text("KeyStore + DataStore Demo") }) }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = vm.plainText,
                        onValueChange = { vm.plainText = it },
                        label = { Text("Secret text (persisted)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(onClick = { vm.encryptAndPersist() }) {
                        Text("Encrypt & Save")
                    }

                    Divider()

                    Text("Encrypted (Base64):")
                    Text(vm.encryptedBase64)

                    Divider()

                    Text("Decrypted (Loaded):")
                    Text(vm.decryptedText)
                }
            }
        }
    }
}
