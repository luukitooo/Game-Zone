package com.lukaarmen.data.common

import com.lukaarmen.domain.common.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestHandler @Inject constructor() {

    fun <T> safeApiCall(apiCall: suspend () -> Response<T>) = flow {
        emit(Resource.Loader(isLoading = true))
        try {
            val response = apiCall()
            if (response.isSuccessful && response.body() != null)
                emit(Resource.Success(success = response.body()))
            else
                emit(Resource.Failure(error = response.errorBody()?.string()))
        } catch (t: Throwable) {
            emit(Resource.Failure(error = t.message))
        }
    }
}