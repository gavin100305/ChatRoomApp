package com.example.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import data.Screen
import screen.ChatRoomListScreen
import screen.ChatScreen
import screen.LoginScreen
import screen.SignUpScreen
import viewmodel.AuthViewModel
import viewmodel.RoomViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    roomViewModel: RoomViewModel){
    NavHost(navController = navController, startDestination = Screen.SignUpScreen.route){
        composable(Screen.SignUpScreen.route){
            SignUpScreen(
                authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                }
            )
        }
        composable(Screen.LoginScreen.route){
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                onSignInSuccess = {
                    navController.navigate(Screen.ChatRoomScreen.route)
                }
            )
        }
        composable(Screen.ChatRoomScreen.route) {
            ChatRoomListScreen(roomViewModel = roomViewModel,
                onJoinClicked = {
                    navController.navigate("${Screen.ChatScreen.route}/${it.id}")
                }
            )
        }
        composable("${Screen.ChatScreen.route}/{roomId") {
            val roomId : String = it.arguments?.getString("roomId")?:""
            ChatScreen(roomId = roomId)

        }
    }
}