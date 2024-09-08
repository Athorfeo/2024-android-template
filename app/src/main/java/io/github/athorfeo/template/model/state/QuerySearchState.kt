package io.github.athorfeo.template.model.state

import io.github.athorfeo.template.util.AppException

data class QuerySearchState(
    val isLoading: Boolean = false,
    val exception: AppException? = null,
    val query: String = ""
)
