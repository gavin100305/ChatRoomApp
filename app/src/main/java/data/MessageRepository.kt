package data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepository(private val firestore: FirebaseFirestore) {

    suspend fun sendMessage(roomId: String, message: Message): Result<Unit> = try {
        firestore.collection("rooms").document(roomId)
            .collection("message")
            .add(message).await()
        Result.Success(Unit)
    }catch(e : Exception){
        Result.Error(e)
    }

    fun getCHatMessages(roomId : String): Flow<List<Message>> =
        callbackFlow {
            val subscription = firestore.collection("rooms").document(roomId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener {querySnapShot,_ ->
                    querySnapShot?.let{
                        trySend(it.documents.map {doc->
                            doc.toObject(Message::class.java)!!.copy()
                        }).isSuccess
                    }

                }
            awaitClose{ subscription.remove() }
        }
}