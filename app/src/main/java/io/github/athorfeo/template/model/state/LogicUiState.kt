package io.github.athorfeo.template.model.state

import io.github.athorfeo.template.util.AppException

data class LogicUiState(
    val isLoading: Boolean = false,
    val exception: AppException? = null
)
