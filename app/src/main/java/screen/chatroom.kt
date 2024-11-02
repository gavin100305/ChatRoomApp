package screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomListScreen(
    roomViewModel: RoomViewModel = viewModel(),
    onJoinClicked : (data.Room) -> Unit
) {
    var showdialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    val rooms by roomViewModel.rooms.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat Rooms",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(rooms){room->
                RoomItem(room = room,
                    onJoinClicked={onJoinClicked(room)})

            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showdialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Create Room")
        }

        if (showdialog) {
            AlertDialog(
                onDismissRequest = { showdialog = false },
                title = { Text(text = "Create a new Room") },
                text = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (name.isNotBlank()) showdialog = false
                        else{
                          Toast.makeText(context,"Enter Name of the Chat Room",Toast.LENGTH_LONG).show()
                        }
                    }) {
                        Text(text = "ADD")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showdialog = false
                    }) {
                        Text(text = "CANCEL")
                    }
                }
            )
        }
    }
}

@Composable
fun RoomItem(room : data.Room,onJoinClicked : (data.Room) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = room.name, fontSize = 16.sp, fontWeight = FontWeight.Normal)
        OutlinedButton(onClick = {
            onJoinClicked(room)
        }) {
            Text(text = "Join")
        }
    }
}