package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import data.Injection
import data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository : UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    private val _authResult = MutableLiveData<data.Result<Boolean>>()
    val authResult : LiveData<data.Result<Boolean>> get() = _authResult

    fun signup(email: String, password: String, firstName: String, lastName: String){
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, firstName, lastName)
        }
    }

    fun login(email:String,password: String){
        viewModelScope.launch{
            _authResult.value = userRepository.login(email, password)
        }
    }
}