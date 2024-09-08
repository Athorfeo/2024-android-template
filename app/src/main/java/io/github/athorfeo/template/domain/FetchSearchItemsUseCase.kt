package io.github.athorfeo.template.domain

import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.model.state.SearchItemsState
import io.github.athorfeo.template.network.response.toDomainModel
import io.github.athorfeo.template.util.AppException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchSearchItemsUseCase @Inject constructor(
    private val searchItemsRepository: SearchItemsRepository
) {
    fun fetch(query: String): Flow<SearchItemsState> {
        return searchItemsRepository.searchItems(query).map { result ->
            when(result) {
                is Result.Loading -> {
                    SearchItemsState(isLoading = true)
                }
                is Result.Error -> {
                    val exception = AppException(cause = result.exception)
                    SearchItemsState(isLoading = false, exception = exception)
                }
                is Result.Success -> {
                    val items = result.data.map { it.toDomainModel() }
                    SearchItemsState(results = items)
                }
            }
        }
    }
}