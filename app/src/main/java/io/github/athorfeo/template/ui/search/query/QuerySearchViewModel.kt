package io.github.athorfeo.template.ui.search.query

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QuerySearchViewModel @Inject constructor(): ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun onQueryChange(text: String) {
        _query.update { text }
    }
}
