package io.github.athorfeo.template.domain

import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.model.Item
import io.github.athorfeo.template.network.response.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLastSearchItemsUseCase @Inject constructor(
    private val searchItemsRepository: SearchItemsRepository
) {
    fun get(): Flow<List<Item>> {
        return searchItemsRepository
            .getLastSearch()
            .map { items ->
                items.map { it.toDomainModel() }
            }
    }
}