package com.lukaarmen.gamezone.common.base

import androidx.lifecycle.ViewModel
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.gamezone.common.util.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel : ViewModel() {

    fun <T> stateHandler(state: Flow<Resource<T>>, currentState: ViewState<T>, withLoader: Boolean = true) = flow {
        state.collect { value ->
            value.apply {
                onSuccess { matchesList ->
                    emit(currentState.copy(data = matchesList, isLoading = null, error = null))
                }
                onFailure { errorMsg ->
                    emit(currentState.copy(error = errorMsg, data = null, isLoading = null))
                }
                onLoader { isLoading ->
                    if (withLoader)
                        emit(currentState.copy(isLoading = isLoading, data = null, error = null))
                }
            }
        }
    }
}