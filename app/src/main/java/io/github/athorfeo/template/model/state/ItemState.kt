package io.github.athorfeo.template.model.state

import io.github.athorfeo.template.network.response.ItemSearchItems
import io.github.athorfeo.template.util.AppException

data class ItemState(
    val isLoading: Boolean = false,
    val exception: AppException? = null,
    val item: ItemSearchItems? = null
)
