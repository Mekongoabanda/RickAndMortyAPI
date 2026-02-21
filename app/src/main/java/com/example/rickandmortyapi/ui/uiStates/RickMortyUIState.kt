package com.example.rickandmortyapi.ui.uiStates

import com.example.rickandmortyapi.models.APIResponse

interface RickAndMortyUIState{
    data class SUCCESS(val result: APIResponse) : RickAndMortyUIState
    object LOADING : RickAndMortyUIState
    object ERROR : RickAndMortyUIState
}