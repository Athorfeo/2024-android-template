package io.github.athorfeo.template.util

import androidx.annotation.StringRes
import java.lang.Exception

open class AppException(
    @StringRes val title: Int? = null,
    @StringRes val description: Int? = null,
    cause: Throwable? = null
): Exception(cause)
