package data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val auth: FirebaseAuth,
                     private val firestore: FirebaseFirestore
) {
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName,lastName,email)
            //add user to firestore
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email : String, password : String): Result<Boolean> =
        try{
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        }catch(e : Exception){
            Result.Error(e)
        }

    suspend fun getCurrentUser(): Result<data.User> = try {
        val uid = auth.currentUser?.email
        if (uid != null){
            val userDocument = firestore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(data.User::class.java)
            if (user != null) {
                Log.d("user2", "$uid")
                Result.Success(user)
            } else {
                Result.Error(java.lang.Exception("User data not found"))
            }
        }else{
            Result.Error(java.lang.Exception("User data not authenticated"))
        }
    }catch(e : Exception){
        Result.Error(e)
    }
    }



