package com.lukaarmen.gamezone.common.base

import androidx.lifecycle.ViewModel
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.gamezone.common.utils.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel : ViewModel() {

    fun <T> stateHandler(state: Flow<Resource<T>>, currentState: ViewState<T>) = flow {
        state.collect { value ->
            value.apply {
                onSuccess { matchesList ->
                    emit(currentState.copy(data = matchesList, isLoading = false, error = ""))
                }
                onFailure { errorMsg ->
                    emit(currentState.copy(error = errorMsg, data = null, isLoading = false))
                }
                onLoader { isLoading ->
                    emit(currentState.copy(isLoading = isLoading, data = null, error = ""))
                }
            }
        }
    }
}