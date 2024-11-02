package data

sealed class Screen(val route : String) {
    object LoginScreen : Screen("loginscreen")
    object SignUpScreen : Screen("signupscreen")
    object ChatRoomScreen : Screen("chatroomscreen")
    object ChatScreen : Screen("chatscreen")
}