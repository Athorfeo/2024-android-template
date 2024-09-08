package io.github.athorfeo.template.ui.search.query

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.athorfeo.template.R
import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.model.state.QuerySearchState
import io.github.athorfeo.template.util.AppException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuerySearchViewModel @Inject constructor(
    private val searchItemsRepository: SearchItemsRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(QuerySearchState())
    val uiState: StateFlow<QuerySearchState> = _uiState

    fun onDismissError() {
        _uiState.update { it.copy(isLoading = false, exception = null) }
    }

    fun onQueryChange(text: String) {
        _uiState.update { it.copy(query = text) }
    }

    fun onSearch(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            searchItemsRepository
                .searchItems(uiState.value.query)
                .collect { result ->
                    when(result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is Result.Error -> {
                            val exception = AppException(cause = result.exception)
                            _uiState.update { it.copy(isLoading = false, exception = exception) }
                        }
                        is Result.Success -> {
                            if(result.data.isEmpty()) {
                                val exception = AppException(
                                    R.string.title_not_found_item_search_dialog,
                                    R.string.text_not_found_item_search_dialog
                                )
                                _uiState.update { it.copy(isLoading = false, exception = exception) }
                            } else {
                                _uiState.update { it.copy(isLoading = false, exception = null) }
                                onSuccess(uiState.value.query)
                            }
                        }
                    }
                }
        }
    }
}
