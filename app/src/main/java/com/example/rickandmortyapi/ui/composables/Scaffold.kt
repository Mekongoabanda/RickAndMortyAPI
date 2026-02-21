package com.example.rickandmortyapi.ui.composables


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapi.models.Character
import com.example.rickandmortyapi.models.Location
import com.example.rickandmortyapi.models.TabButtonType
import com.example.rickandmortyapi.ui.uiStates.RickAndMortyUIState
import com.example.rickandmortyapi.viewModel.RickAndMortyViewModel


@Composable
fun AppScaffold(vm: RickAndMortyViewModel = viewModel()){
    val selectedTab by vm.currentTab.collectAsState()
    val rickAndMortyUIState by vm.uiState.collectAsState()

    // Important: lance l'appel quand l'onglet change (launchAPI ne doit pas être appelé à chaque recomposition)
    LaunchedEffect(selectedTab) {
        vm.launchAPI(selectedTab)
    }

    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TabButtons(
                    onCharacterClick = {
                        vm.updateTab(it)
                        vm.launchAPI(it)
                    },
                    onLocationClick = {
                        vm.updateTab(it)
                        vm.launchAPI(it)
                    },
                    onEpisodeClick = {
                        vm.updateTab(it)
                        vm.launchAPI(it)
                    }
                )
                SearchView()

                when (val state = rickAndMortyUIState) {
                    is RickAndMortyUIState.LOADING -> Text(text = "Loading...")
                    is RickAndMortyUIState.ERROR -> Text(text = "Error")
                    is RickAndMortyUIState.SUCCESS -> {
                        when (selectedTab) {
                            TabButtonType.CHARACTERS -> {
                                val list = (state.result.dataList as? List<Character>).orEmpty()
                                LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    items(list) { character ->
                                        CharacterListItem(character = character)
                                    }
                                }
                            }

                            TabButtonType.LOCATIONS -> {
                                val list = (state.result.dataList as? List<Location>).orEmpty()
                                LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    items(list) { location ->
                                        LocationListItem(location = location)
                                    }
                                }
                            }

                            TabButtonType.EPISODES -> {
                                LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    items(state.result.dataList.orEmpty()) { data ->
                                        Text(data.toString())
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    TopAppBar(title = {
        Text(text = "Rick & Morty API",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold)
    })
}

@Preview(showBackground = true)
@Composable
fun AppScaffoldPreview(){
    AppScaffold()
}