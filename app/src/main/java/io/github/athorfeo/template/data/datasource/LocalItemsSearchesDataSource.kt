package io.github.athorfeo.template.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import io.github.athorfeo.template.network.response.ItemSearchItems
import io.github.athorfeo.template.data.datastore.SEARCH_ITEMS_STORE
import io.github.athorfeo.template.data.datastore.resource.SearchListResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalItemsSearchesDataSource @Inject constructor(
    private val cacheDataStore: DataStore<Preferences>,
    private val serializer: Gson,
) {
    suspend fun saveSearchedItems(items: List<ItemSearchItems>) {
        cacheDataStore.edit { settings ->
            val resource = SearchListResource(items)
            val json = serializer.toJson(resource)
            settings[SEARCH_ITEMS_STORE] = json
        }
    }

    fun getSearchedItems(): Flow<List<ItemSearchItems>?> {
        return cacheDataStore.data.map { preferences ->
            preferences[SEARCH_ITEMS_STORE]?.let {
                serializer.fromJson(it, SearchListResource::class.java).data
            }
        }
    }
}
