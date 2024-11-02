package screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chat.R
import viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
){
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        OutlinedTextField(value = email, onValueChange ={
            email =  it
        }, label = { Text(text = "Email")},
            placeholder = { Text(text = "Enter your Email....")})
        OutlinedTextField(value = password, onValueChange ={
            password =  it
        }, label = { Text(text = "Password")},
            placeholder = { Text(text = "Enter your Password....")},
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(R.drawable.baseline_remove_red_eye_24)
                else
                    painterResource(R.drawable.outline_remove_red_eye_24)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null)
                }
            })
        OutlinedTextField(value = firstName, onValueChange ={
            firstName =  it
        }, label = { Text(text = "First Name")},
            placeholder = { Text(text = "Enter your First Name....")})
        OutlinedTextField(value = lastName, onValueChange ={
            lastName =  it
        }, label = { Text(text = "Last Name")},
            placeholder = { Text(text = "Enter your Last Name....")})

        Button(
            onClick = {
                authViewModel.signup(email,password,firstName, lastName)
                email = ""
                password = ""
                firstName = ""
                lastName = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Already have an account? Sign in.",
            modifier = Modifier.clickable { //add navigation
                onNavigateToLogin()}
                )
    }

}
