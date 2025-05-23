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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.nulldivision_ctf.ui.theme.NullDivision_CTFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NullDivision_CTFTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize().background(colorResource(id = R.color.background)))
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
                label = { Text("Username") },

                colors =  TextFieldDefaults.colors(focusedContainerColor = colorResource(id = android.R.color.transparent),
                unfocusedContainerColor = colorResource(id = android.R.color.transparent),
                focusedLabelColor = colorResource(id = R.color.hackerRed),
                unfocusedLabelColor = colorResource(id = R.color.hackerBlue),
                focusedIndicatorColor = colorResource(id = R.color.hackerRed),
                unfocusedIndicatorColor = colorResource(id = R.color.hackerBlue),
                cursorColor = colorResource(id = R.color.hackerBlue),
                focusedTextColor = colorResource(id = R.color.hackerRed),
                unfocusedTextColor = colorResource(id = R.color.hackerBlue)
            )
            )
            androidx.compose.material3.OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                label = { Text("Password") },
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                colors =  TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = android.R.color.transparent),
                    unfocusedContainerColor = colorResource(id = android.R.color.transparent),
                    focusedLabelColor = colorResource(id = R.color.hackerRed),
                    unfocusedLabelColor = colorResource(id = R.color.hackerBlue),
                    focusedIndicatorColor = colorResource(id = R.color.hackerRed),
                    unfocusedIndicatorColor = colorResource(id = R.color.hackerBlue),
                    cursorColor = colorResource(id = R.color.hackerBlue),
                    focusedTextColor = colorResource(id = R.color.hackerRed),
                    unfocusedTextColor = colorResource(id = R.color.hackerBlue)
                )
            )
            androidx.compose.material3.Button(
                onClick = {
                    password = "";

                          },
                modifier = Modifier.padding(top = 16.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.hackerBlue),
                    contentColor = colorResource(id = R.color.background),

                )
            ) {
                Text("Login")
            }
        }
    }
}