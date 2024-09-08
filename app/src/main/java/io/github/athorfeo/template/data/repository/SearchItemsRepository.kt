package io.github.athorfeo.template.data.repository

import dagger.hilt.android.scopes.ViewModelScoped
import io.github.athorfeo.template.data.datasource.LocalItemsSearchesDataSource
import io.github.athorfeo.template.data.datasource.NetworkItemsSearchesDataSource
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.network.response.ItemSearchItems
import io.github.athorfeo.template.util.AppException
import io.github.athorfeo.template.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ViewModelScoped
class SearchItemsRepository @Inject constructor(
    private val logger: Logger,
    private val networkItemsSearchesDataSource: NetworkItemsSearchesDataSource,
    private val localItemsSearchesDataSource: LocalItemsSearchesDataSource
) {
    fun searchItems(query: String): Flow<Result<List<ItemSearchItems>>> {
        return flow {
            emit(Result.Loading)
            val response = networkItemsSearchesDataSource.fetchSearchItems(query)
            localItemsSearchesDataSource.saveSearchedItems(response.results)
            emit(Result.Success(response.results))
        }.catch {
            emit(Result.Error(it))
        }
    }

    suspend fun getItemInCache(itemId: String): Flow<Result<ItemSearchItems>> {
        logger.d("itemId: $itemId")
        return localItemsSearchesDataSource
            .getSearchedItems()
            .map { items ->

                items?.find{ it.id == itemId }?.let { itemFound ->
                    Result.Success(itemFound)
                } ?: run {
                    val exception = AppException()
                    Result.Error(exception)
                }
            }
            .onStart { emit(Result.Loading) }
            .catch { emit(Result.Error(it)) }
    }
}
