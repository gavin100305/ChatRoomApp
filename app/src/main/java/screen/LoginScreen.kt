package screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit

) {
    val result by authViewModel.authResult.observeAsState()
    var email by remember { mutableStateOf("") }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {

                authViewModel.login(email, password)
            },

                    modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Sign up.",
            modifier = Modifier.clickable {  onNavigateToSignUp() }
        )
    }
    when (result) {
        is data.Result.Success -> {
            Log.d("LoginScreen", "Login successful")
            onSignInSuccess()
        }
        is data.Result.Error -> {
            Log.e("LoginScreen", "Login failed", (result as data.Result.Error).exception)
            // Show an error message or take other appropriate action
        }
        else -> {
            Log.d("LoginScreen", "Loading or uninitialized state")
        }
    }

}