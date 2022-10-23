package com.lukaarmen.domain.common

sealed class Resource<T>(
    val success: T? = null,
    val error: String? = null,
    val isLoading: Boolean? = null
) {

    class Success<T>(success: T?): Resource<T>(success = success)
    class Failure<T>(error: String?): Resource<T>(error = error)
    class Loader<T>(isLoading: Boolean?): Resource<T>(isLoading = isLoading)

    suspend fun onSuccess(func: suspend (T) -> Unit) {
        if (this is Success) {
            this.success?.let {
                func(it)
            }
        }
    }

    suspend fun onFailure(func: suspend (String) -> Unit) {
        if (this is Failure) {
            this.error?.let {
                func(it)
            }
        }
    }

    suspend fun onLoader(func: suspend (Boolean) -> Unit) {
        if (this is Loader) {
            this.isLoading?.let {
                func(it)
            }
        }
    }

}

fun <T, R> Resource<List<T>>.mapSuccess(transform: (T) -> R): Resource<List<R>> {
    return when (this) {
        is Resource.Success -> Resource.Success(success = this.success?.map { transform(it) })
        is Resource.Failure -> Resource.Failure(error = this.error)
        is Resource.Loader -> Resource.Loader(isLoading = this.isLoading)
    }
}


fun <T, R> Resource<T>.success(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(success = transform(success!!))
        is Resource.Failure -> Resource.Failure(error = this.error)
        is Resource.Loader -> Resource.Loader(isLoading = this.isLoading)
    }
}