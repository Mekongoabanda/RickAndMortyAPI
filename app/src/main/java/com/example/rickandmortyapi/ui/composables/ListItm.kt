package com.example.rickandmortyapi.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.placeholder
import com.example.rickandmortyapi.R
import com.example.rickandmortyapi.models.Character
import com.example.rickandmortyapi.models.CharacterLocation
import com.example.rickandmortyapi.models.Location
import kotlin.collections.emptyList

@Composable
fun CharacterListItem(character: Character, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3C3E44)
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            var imageState: AsyncImagePainter.State by remember { mutableStateOf(AsyncImagePainter.State.Empty) }

            Box(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(character.image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .build(),
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop,
                    onState = { imageState = it }
                )

                when (imageState) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Color(0x22000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        }
                    }

                    is AsyncImagePainter.State.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Color(0xFF2B2D31)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Image KO",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                    }

                    else -> Unit
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = character.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (character.status.lowercase()) {
                        "alive" -> Color.Green
                        "dead" -> Color.Red
                        else -> Color.Gray
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${character.status} - ${character.species}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Last known location:",
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp
                )
                Text(
                    text = character.location.name,
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "First seen in:",
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp
                )
                // character.episode list contains URLs. To display the name we'd need more logic,
                // but for now let's just show a placeholder or the first entry's ID part if possible.
                Text(
                    text = "The Wedding Squanchers", // Placeholder as per image or character.episode.firstOrNull() ?: "Unknown"
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LocationListItem(location: Location, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3C3E44)
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .wrapContentHeight()
            ) {
                Text(
                    text = location.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${location.type} - ${location.dimension}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Residents:",
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp
                )

                // IMPORTANT: pas de composant scrollable vertical (LazyColumn) à l'intérieur d'un item
                // d'une autre liste scrollable. Ça déclenche des contraintes infinies.
                val residents = location.residents.orEmpty()
                val toShow = residents.take(5)
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    toShow.forEach { residentUrl ->
                        Text(
                            text = residentUrl,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                    if (residents.size > toShow.size) {
                        Text(
                            text = "+${residents.size - toShow.size} autres…",
                            color = Color(0xFF9E9E9E),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Created at: ${location.created}",
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    val sampleCharacter = Character(
        id = 1,
        name = "Donna Gueterman",
        status = "Dead",
        species = "Robot",
        type = "",
        gender = "Female",
        origin = CharacterLocation("unknown", ""),
        location = CharacterLocation("Planet Squanch", ""),
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episode = listOf("https://rickandmortyapi.com/api/episode/1"),
        url = "",
        created = ""
    )

    val sampleLocation = Location(
        id = 1,
        name = "Earth",
        type = "Planet",
        dimension = "Dimension C-137",
        residents =  listOf("https://rickandmortyapi.com/api/character/1", "https://rickandmortyapi.com/api/character/2"),
        url =  "https://rickandmortyapi.com/api/location/1",
        created =  "2017-11-10T12:42:04.162Z"
    )
    Column() {
        CharacterListItem(character = sampleCharacter)
        LocationListItem(location = sampleLocation)
    }
}
