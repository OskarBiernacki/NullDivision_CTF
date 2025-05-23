package com.example.nulldivision_ctf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nulldivision_ctf.ui.theme.NullDivision_CTFTheme
import androidx.compose.runtime.*



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NullDivision_CTFTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize().background(Color.LightGray))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        modifier = modifier,
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = login,
                onValueChange = {login=it},
                label = { Text("Login") }
            )
            androidx.compose.material3.OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                label = { Text("Hasło") },
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
            )
            androidx.compose.material3.Button(
                onClick = {
                    password = "";

                          },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Zaloguj się")
            }
        }
    }
}