package io.github.athorfeo.template.ui.item.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.model.state.ItemState
import io.github.athorfeo.template.navigation.Screen
import io.github.athorfeo.template.util.AppException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchItemsRepository: SearchItemsRepository
): ViewModel() {
    val item: StateFlow<ItemState> = savedStateHandle
        .getStateFlow(Screen.ITEM_ID_ARG, "")
        .filterNotNull()
        .flatMapLatest {
            searchItemsRepository.getItemInCache(it).map { result ->
                when(result) {
                    is Result.Loading -> {
                        ItemState(isLoading = true)
                    }
                    is Result.Error -> {
                        val exception = AppException(cause = result.exception)
                        ItemState(isLoading = false, exception = exception)
                    }
                    is Result.Success -> {
                        ItemState(item = result.data)
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ItemState()
    )
}
