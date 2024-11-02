package data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RoomRepository(private val firestore: FirebaseFirestore) {

    suspend fun createRoom(name: String): Result<Unit> = try {
        val room = Room(name = name)
        firestore.collection("rooms").add(room).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapShot = firestore.collection("rooms").get().await()
        val room = querySnapShot.documents.map {document ->
            document.toObject(Room::class.java)!!.copy(id = document.id)
        }
        Result.Success(room)
    }catch (e : Exception){
        Result.Error(e)
    }
}