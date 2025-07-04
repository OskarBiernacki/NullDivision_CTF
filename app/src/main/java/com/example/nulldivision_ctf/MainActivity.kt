package com.example.nulldivision_ctf

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.nulldivision_ctf.ui.theme.NullDivision_CTFTheme
import kotlinx.coroutines.delay
import org.json.JSONObject

enum class Page{
    LoginScreen,
    LoadingScreen,
    AccessScreen,
    UserRegister
}

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
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.POST,
        serverUrl+method,
        data,
        { response -> onResponseReaction(response) },
        { error -> main.runOnUiThread {
            val errorMsg = try {
                val statusCode = error.networkResponse?.statusCode
                val dataString = error.networkResponse?.data?.toString(Charsets.UTF_8)
                "Błąd ${statusCode ?: "unknown"}: ${dataString ?: "brak odpowiedzi"}"
            } catch (ex: Exception) {
                "Błąd połączenia: ${ex.localizedMessage ?: "nieznany błąd"}"
            }
            Log.e("API_ERROR", error.toString())
            Toast.makeText(main, errorMsg, Toast.LENGTH_LONG).show()
        } }
    )
    volleyQueue.add(jsonObjectRequest)
}
@Composable
fun AppContent(main : ComponentActivity) {
    var commands by remember { mutableStateOf(listOf<String>()) }
    var currentPage by remember { mutableStateOf(Page.LoginScreen) }
    var loadingMessage by remember { mutableStateOf("") }
    var userToken by remember { mutableStateOf("") }
    var currentUsername by remember { mutableStateOf("") }
    val context = LocalContext.current
    val url = "http://${context.getString(R.string.serverUrl)}:${context.getString(R.string.serverPORT)}/"

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (currentPage == Page.LoginScreen) {
            LoginScreen(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background)),
                onLoginClicked = {login, password ->

                    val jsonBody = JSONObject()
                    jsonBody.put("username", login)
                    jsonBody.put("password", password)

                    makeAPIRequest(main, url, "login", jsonBody) { response ->
                        val token = response.optString("token", "")
                        main.runOnUiThread {
                            if (token.isNotEmpty()) {
                                userToken = token
                                currentUsername = login
                                currentPage = Page.AccessScreen
                                Toast.makeText(main, "Logged inn", Toast.LENGTH_LONG).show()

                                val jsonBody2 = JSONObject()
                                jsonBody2.put("token", token)
                                jsonBody2.put("username",currentUsername)
                                makeAPIRequest(main = context as ComponentActivity, url, "getCommands", jsonBody2) { response ->
                                    response.optJSONArray("commands")?.let { array ->
                                        val list = mutableListOf<String>()
                                        for (i in 0 until array.length()) {
                                            list.add(array.getString(i))
                                        }
                                        commands = list
                                    }
                                }

                            } else {
                                Toast.makeText(main, "Login failed: invalid response", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    currentPage = Page.LoadingScreen
                },
                loadingMessage
            )
        } else if(currentPage == Page.LoadingScreen){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedDotsText("Connecting server...", color = colorResource(id = R.color.hackerBlue),
                    onFinishLoading = {
                        loadingMessage="Unable to login!"
                        currentPage = Page.LoginScreen
                    }
                )
            }
        }else if(currentPage == Page.AccessScreen){
            AccessScreen(innerPadding, currentUsername, userToken, onUserRegisterButton = {
                currentPage = Page.UserRegister
            }, commands)
        }else if(currentPage == Page.UserRegister) {
            UserRegisterScreen(
                innerPadding,
                onUserRegisterButton = { username, password ->
                    val jsonBody = JSONObject()
                    jsonBody.put("username", username)
                    jsonBody.put("password", password)
                    jsonBody.put("secretAccessKey", context.getString(R.string.secretAccessKey))

                    makeAPIRequest(main, url, "register", jsonBody) { response ->
                        val message = response.optString("message", "")
                        main.runOnUiThread {
                            Toast.makeText(main, message.toString(), Toast.LENGTH_LONG).show()
                            currentPage = Page.AccessScreen
                        }
                    }

                },
                onBackButton = {
                    currentPage = Page.AccessScreen
                }
            )
        }
    }
}
@Composable
fun AnimatedDotsText(baseText: String, color: Color, onFinishLoading: () -> Unit) {
    var dotCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (dotCount<20) {
            dotCount++
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
@SuppressLint("MutableCollectionMutableState")
@Composable
fun AccessScreen(innerPadding : PaddingValues, username: String, token: String, onUserRegisterButton: () -> Unit, commands : List<String> ){
//    var commands by remember { mutableStateOf(listOf<String>()) }

    val logo = painterResource(R.drawable.logo)

//    val context = LocalContext.current
//    val url = "http://${context.getString(R.string.serverUrl)}:${context.getString(R.string.serverPORT)}/"
//    val jsonBody = JSONObject()
//    jsonBody.put("token", token)
//    jsonBody.put("username",username)
//    makeAPIRequest(main = context as ComponentActivity, url, "getCommands", jsonBody) { response ->
//        response.optJSONArray("commands")?.let { array ->
//            val list = mutableListOf<String>()
//            for (i in 0 until array.length()) {
//                list.add(array.getString(i))
//            }
//            commands = list
//        }
//    }

    Log.d("AccessScreen", "username: $username, token: $token")
    Box(
        modifier = Modifier.padding(innerPadding).fillMaxSize().background(colorResource(id = R.color.background)),
    ){
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = logo,
                contentDescription = null,
            )
            Text("Logged in as $username", color = colorResource(id = R.color.hackerBlue), fontSize = 30.sp)
            Button(
                onClick = { onUserRegisterButton() },
                border = BorderStroke(
                    width = 2.dp,
                    color = colorResource(id = R.color.hackerRed),
                ),
                modifier = Modifier.padding(top = 16.dp).background(
                        color = colorResource(id = android.R.color.transparent),
                        shape = RoundedCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = android.R.color.transparent),
                    contentColor = colorResource(id = R.color.hackerRed)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ){
                Text("RECRUIT NEW MEMBER")
            }
            Text("Your list of tasks:", color = colorResource(id = R.color.hackerRed), fontSize = 20.sp)
            for(i in 0..commands.size-1)
                Box(
                    modifier = Modifier.width(300.dp)
                        .padding(8.dp)
                        .border(
                            width = 2.dp,
                            color = colorResource(id = R.color.hackerBlue),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
            ){ Text(commands[i], color = colorResource(id = R.color.hackerBlue)) }
        }
    }
}
@Composable
fun UserRegisterScreen(innerPadding : PaddingValues, onUserRegisterButton: (username : String, password : String) -> Unit,
                       onBackButton: () -> Unit){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.padding(innerPadding).fillMaxSize().background(colorResource(id = R.color.background)),
    ){
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("RECRUIT NEW MEMBER", color= colorResource(R.color.hackerBlue), fontSize = 35.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = username,
                onValueChange = {username=it},
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
            Button(
                onClick = {
                    onUserRegisterButton(username, password)
                },
                border = BorderStroke(
                    width = 2.dp,
                    color = colorResource(id = R.color.hackerBlue),
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(color = colorResource(id = android.R.color.transparent), shape = RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = android.R.color.transparent),
                    contentColor = colorResource(id = R.color.hackerBlue)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "CREATE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
            Button(
                onClick = {
                        onBackButton()
                },
                border = BorderStroke(
                    width = 2.dp,
                    color = colorResource(id = R.color.hackerRed),
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(color = colorResource(id = android.R.color.transparent), shape = RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = android.R.color.transparent),
                    contentColor = colorResource(id = R.color.hackerRed)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "BACK",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}