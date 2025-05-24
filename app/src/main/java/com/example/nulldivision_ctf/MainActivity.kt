package com.example.nulldivision_ctf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.nulldivision_ctf.ui.theme.NullDivision_CTFTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NullDivision_CTFTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (!isLoading) {
            LoginScreen(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background)),
                onLoginClicked = { isLoading = true }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedDotsText("Connecting server...", color = colorResource(id = R.color.hackerBlue),
                    onFinishLoading = {isLoading=false}
                    )
            }
        }
    }
}
@Composable
fun AnimatedDotsText(baseText: String, color: androidx.compose.ui.graphics.Color, onFinishLoading: () -> Unit) {
    var dotCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (dotCount<10) {
            dotCount = (dotCount + 1)
            kotlinx.coroutines.delay(500)
        }
        onFinishLoading()
    }

    val dots = ".".repeat(dotCount%4)
    Text(text = "$baseText$dots", color = color)
}


@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginClicked: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val logo = painterResource(R.drawable.logo)

    Box(
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
        ) {
            Image(
                painter = logo,
                contentDescription = null,

            )
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
                    password = ""
                    onLoginClicked()
                },
                border = BorderStroke(
                    width = 2.dp,
                    color = colorResource(id = R.color.hackerBlue),
//                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(
                        color = colorResource(id = android.R.color.transparent),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = android.R.color.transparent),
                    contentColor = colorResource(id = R.color.hackerBlue)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "LOGIN",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}