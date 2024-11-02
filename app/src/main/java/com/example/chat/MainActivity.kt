package com.example.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.chat.ui.theme.ChatTheme
import screen.ChatRoomListScreen
import screen.ChatScreen
import viewmodel.AuthViewModel
import viewmodel.RoomViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel : AuthViewModel = viewModel()
            val roomViewModel : RoomViewModel = viewModel()

            ChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    
                    Navigation(navController = navController,
                        authViewModel = authViewModel,
                        roomViewModel = roomViewModel)


                }
            }
        }
    }
}

