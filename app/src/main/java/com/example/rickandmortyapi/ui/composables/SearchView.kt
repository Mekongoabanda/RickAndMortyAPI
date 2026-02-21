package com.example.rickandmortyapi.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapi.models.TabButtonType
import com.example.rickandmortyapi.ui.theme.RickAndMortyAPITheme
import com.example.rickandmortyapi.viewModel.RickAndMortyViewModel

@Composable
fun SearchView(vm: RickAndMortyViewModel = viewModel()){

    val currentTab by vm.currentTab.collectAsState()
    val name = vm.name ?: ""

    SearchViewContent(
        name = name,
        currentTab = currentTab,
        onNameChange = { vm.updateTextFieldsValue(it) },
        onSearch = { vm.launchAPI(currentTab) }
    )
}

@Composable
fun SearchViewContent(
    name: String,
    currentTab: TabButtonType,
    onNameChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val manager = LocalFocusManager.current
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onSearch()
            manager.clearFocus()
        }),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        label = {
            Text("Search name of $currentTab")
        },
        placeholder = {
            Text("Search name of $currentTab")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    RickAndMortyAPITheme {
        SearchViewContent(
            name = "Rick",
            currentTab = TabButtonType.CHARACTERS,
            onNameChange = {},
            onSearch = {}
        )
    }
}