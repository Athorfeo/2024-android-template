package io.github.athorfeo.template.ui.search.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.athorfeo.template.domain.GetLastSearchItemsUseCase
import io.github.athorfeo.template.model.Item
import io.github.athorfeo.template.navigation.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ResultSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    fetchSearchItems: GetLastSearchItemsUseCase
): ViewModel() {
    val query: String = checkNotNull(savedStateHandle[Screen.QUERY_ARG])
    val items: StateFlow<List<Item>> = fetchSearchItems
        .get()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )
}

