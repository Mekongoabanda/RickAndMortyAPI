package com.example.rickandmortyapi.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapi.models.TabButtonType
import com.example.rickandmortyapi.viewModel.RickAndMortyViewModel
import androidx.compose.ui.draw.alpha

@Composable
fun TabButtons(onCharacterClick: (TabButtonType) -> Unit = {},
               onLocationClick: (TabButtonType) -> Unit = {},
               onEpisodeClick: (TabButtonType) -> Unit = {},
               vm: RickAndMortyViewModel = viewModel()){

    val currentTab by vm.currentTab.collectAsState(initial = TabButtonType.CHARACTERS)

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onCharacterClick(TabButtonType.CHARACTERS) },
            modifier = Modifier.alpha(if (currentTab == TabButtonType.CHARACTERS) 1f else 0.3f)
        ) {
            Text("Characters")
        }
        Button(
            onClick = { onLocationClick(TabButtonType.LOCATIONS) },
            modifier = Modifier.alpha(if (currentTab == TabButtonType.LOCATIONS) 1f else 0.3f)
        ) {
            Text("Locations")
        }
        Button(
            onClick = { onEpisodeClick(TabButtonType.EPISODES) },
            modifier = Modifier.alpha(if (currentTab == TabButtonType.EPISODES) 1f else 0.3f)
        ) {
            Text("Episodes")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabButtonsPreview(){
    TabButtons()
}
