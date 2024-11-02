package screen

import android.os.Build
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.R
import viewmodel.MessageViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(roomId : String,
               messageViewModel: MessageViewModel = viewModel()){

    val messages by messageViewModel.message.observeAsState(emptyList())
    messageViewModel.setRoomId(roomId)
    var text by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ){
            items(messages){message->
                ChatMessageItem(message = message.copy(isSentByCurrentUser =
                message.senderId == messageViewModel.currentUser.value?.email))
            }

        }

        Row(modifier = Modifier
            .fillMaxWidth()
            ,
            verticalAlignment = Alignment.Bottom){
            
            OutlinedTextField(value = text, onValueChange ={
                text= it
            },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )

            IconButton(onClick = {
                if(text.isNotEmpty()){
                    messageViewModel.sendMessage(text.trim())
                    text = ""
                    messageViewModel.loadMessages()
                }

            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")

            }

        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimestamp(timestamp: Long):String {
    val messageDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val now = LocalDateTime.now()
    return when {
        isSameDay(messageDateTime,now) -> "Today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1),now) -> "Yesterday ${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(dateTime1 : LocalDateTime,dateTime2 : LocalDateTime):Boolean{
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return dateTime2.format(formatter) == dateTime2.format(formatter)

}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return dateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime: LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("MMM d,yyyy")
    return dateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message : data.Message){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = if (message.isSentByCurrentUser)
            Alignment.End else Alignment.Start) {
            Box(modifier = Modifier
                .background(
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700)
                    else Color.Gray, shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)){
                    Text(text = message.senderFirstName, textAlign = TextAlign.Start,
                        fontWeight = FontWeight.ExtraBold)
                    Text(text = message.text, textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal)
                    Text(text = message.timestamp.toString(), textAlign = TextAlign.End,
                        fontWeight = FontWeight.Normal, fontSize = 5.sp)
                }
            }

    }

}

