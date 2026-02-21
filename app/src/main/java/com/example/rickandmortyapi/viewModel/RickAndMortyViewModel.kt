package com.example.rickandmortyapi.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapi.models.APIResponse
import com.example.rickandmortyapi.models.Character
import com.example.rickandmortyapi.models.Episode
import com.example.rickandmortyapi.models.Location
import com.example.rickandmortyapi.models.TabButtonType
import com.example.rickandmortyapi.service.CharacterApi
import com.example.rickandmortyapi.service.EpisodeApi
import com.example.rickandmortyapi.service.LocationApi
import com.example.rickandmortyapi.ui.uiStates.RickAndMortyUIState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class RickAndMortyViewModel: ViewModel() {
    private val _uiState = MutableStateFlow<RickAndMortyUIState>(RickAndMortyUIState.LOADING)
    var uiState: StateFlow<RickAndMortyUIState> = _uiState
    var name: String? by mutableStateOf(null)
    private val _uiStateTab = MutableStateFlow<TabButtonType>(TabButtonType.CHARACTERS)
    var currentTab: StateFlow<TabButtonType> = _uiStateTab

    fun launchAPI(currentTab: TabButtonType){
        _uiState.value = RickAndMortyUIState.LOADING

        viewModelScope.launch {
            try {
                val response = when(currentTab){
                    TabButtonType.CHARACTERS -> CharacterApi.retrofitService.getCharacters(name = name)
                    TabButtonType.LOCATIONS -> LocationApi.service.getLocations(name = name)
                    TabButtonType.EPISODES -> EpisodeApi.service.getEpisodes(name = name)
                }
                _uiState.value = RickAndMortyUIState.SUCCESS(convertDatas(response, currentTab))


            } catch (io: IOException) {
                _uiState.value = RickAndMortyUIState.ERROR
            } catch (http: HttpException) {
                _uiState.value = RickAndMortyUIState.ERROR
            } catch (e: Exception) {
                _uiState.value = RickAndMortyUIState.ERROR
            }
        }
    }

    fun updateTextFieldsValue(text: String){
        name = text
    }
    fun updateTab(newTab: TabButtonType){
        _uiStateTab.value = newTab
    }

    private fun convertDatas(json: String, currentTab: TabButtonType): APIResponse {
        val gson = Gson()
        return when (currentTab) {
            TabButtonType.CHARACTERS -> {
                val type = object : TypeToken<GenericResponse<Character>>() {}.type
                val response: GenericResponse<Character> = gson.fromJson(json, type)
                APIResponse(response.info, response.results)
            }
            TabButtonType.LOCATIONS -> {
                val type = object : TypeToken<GenericResponse<Location>>() {}.type
                val response: GenericResponse<Location> = gson.fromJson(json, type)
                APIResponse(response.info, response.results)
            }
            TabButtonType.EPISODES -> {
                val type = object : TypeToken<GenericResponse<Episode>>() {}.type
                val response: GenericResponse<Episode> = gson.fromJson(json, type)
                APIResponse(response.info, response.results)
            }
        }
    }

    private data class GenericResponse<T>(
        val info: com.example.rickandmortyapi.models.Info?,
        val results: List<T>?
    )
}