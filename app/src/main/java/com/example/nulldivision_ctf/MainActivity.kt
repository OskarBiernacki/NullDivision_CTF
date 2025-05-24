package com.example.nulldivision_ctf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.nulldivision_ctf.ui.theme.NullDivision_CTFTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.core.content.res.TypedArrayUtils.getText
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.delay
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NullDivision_CTFTheme {
                AppContent(this)
            }
        }
    }
}

fun makeAPIRequest(main : ComponentActivity, serverUrl : String, method : String, data : JSONObject, onResponseReaction: (response :  JSONObject ) -> Unit ) {
    val volleyQueue = Volley.newRequestQueue(main)

//        val jsonBody = JSONObject()
//        jsonBody.put("login", login)
//        jsonBody.put("password", password)

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET,
        serverUrl,
        data,
        { response ->
            onResponseReaction(response)
//              response.get("message")
        },
        { error ->
            Toast.makeText(main, error.message, Toast.LENGTH_LONG).show()
        }
    )
    volleyQueue.add(jsonObjectRequest)
}

@Composable
fun AppContent(main : ComponentActivity) {
    var isLoading by remember { mutableStateOf(false) }
    var loadingMessage by remember { mutableStateOf("") }
    val url = "http://${stringResource(id = R.string.serverUrl)}:${stringResource(id = R.string.serverPORT)}/"

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (!isLoading) {
            LoginScreen(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background)),
                onLoginClicked = {login, password ->

                    val jsonBody = JSONObject()
                    jsonBody.put("login", login)
                    jsonBody.put("password", password)

                    makeAPIRequest(main, url, "login",jsonBody, { response ->
                        val message = response.get("token");
                        Toast.makeText(main, message.toString(), Toast.LENGTH_LONG).show()
                    })

                    isLoading = true;
                },
                loadingMessage
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedDotsText("Connecting server...", color = colorResource(id = R.color.hackerBlue),
                    onFinishLoading = {isLoading=false; loadingMessage="Unable to login!"}
                    )
            }
        }
    }
}
@Composable
fun AnimatedDotsText(baseText: String, color: Color, onFinishLoading: () -> Unit) {
    var dotCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (dotCount<20) {
            dotCount++;
            delay(500)
        }
        onFinishLoading()
    }

    val dots = ".".repeat(dotCount%4)
    Text(text = "$baseText$dots", color = color)
}
@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginClicked: (login : String, password : String) -> Unit, message : String) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val logo = painterResource(R.drawable.logo)

    Box(
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = logo,
                contentDescription = null,

            )
            OutlinedTextField(
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
            OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
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
            if(message != "") Text(message, color = colorResource(id = R.color.hackerRed))
            Button(
                onClick = {
                    onLoginClicked(login, password)
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
                        shape = RoundedCornerShape(4.dp)
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