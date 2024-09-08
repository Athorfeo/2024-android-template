package io.github.athorfeo.template.ui.search.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.model.state.DetailSearchState
import io.github.athorfeo.template.navigation.Screen
import io.github.athorfeo.template.util.AppException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ResultSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    searchItemsRepository: SearchItemsRepository
): ViewModel() {
    val query: String = checkNotNull(savedStateHandle[Screen.QUERY_ARG])
    val uiState: StateFlow<DetailSearchState> = searchItemsRepository.searchItems(query).map { result ->
            when(result) {
                is Result.Loading -> {
                    DetailSearchState(isLoading = true)
                }
                is Result.Error -> {
                    val exception = AppException(cause = result.exception)
                    DetailSearchState(isLoading = false, exception = exception)
                }
                is Result.Success -> {
                    DetailSearchState(resultsQuery = result.data.map { it.id })
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailSearchState()
    )
}
