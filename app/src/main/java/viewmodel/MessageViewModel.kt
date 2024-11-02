package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import data.Injection
import data.Message
import data.MessageRepository
import data.Result
import data.UserRepository
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val messageRepository : MessageRepository
    private val userRepository : UserRepository

    init{
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(FirebaseAuth.getInstance(),
           Injection.instance())
        loadCurrentUser()

    }

    private val _messages = MutableLiveData<List<Message>>()
    val message : LiveData<List<Message>> get() = _messages


    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<data.User>()
    val currentUser: LiveData<data.User> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {
                    // Log or handle the error
                    Log.e("loadCurrentUser", "Error loading user: ${result.exception}")
                    // You could also update an error message to the UI here
                }
            }
        }

    }

    fun loadMessages(){
        viewModelScope.launch {
            if(_roomId != null){
                messageRepository.getCHatMessages(_roomId.value.toString())
                    .collect{
                        _messages.value = it
                    }
            }
        }
    }

    fun sendMessage(text : String) {
        if (_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text
            )

            viewModelScope.launch {
                when (messageRepository.sendMessage(_roomId.value.toString(), message)){
                    is Result.Success -> Unit
                    else -> {

                    }
                }
            }

        }
    }

    fun setRoomId(roomId : String){
          _roomId.value = roomId
        loadMessages()
    }


}