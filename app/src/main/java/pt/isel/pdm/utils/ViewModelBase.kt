package pt.isel.pdm.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.State

class ViewModelBase<S : State, E : DomainError>(
    initialState: S,
    private val noError: E
) : ViewModel(), ViewModelState<S, E> {

    private val _stateUi: MutableStateFlow<S> = MutableStateFlow(initialState)
    override val stateUi = _stateUi.asStateFlow()

    private val _errorState: MutableStateFlow<E> = MutableStateFlow(noError)
    override val errorState = _errorState.asStateFlow()

    override fun navigateTo(newState: S) {
        _stateUi.value = newState
    }

    override fun emitError(error: E) {
        _errorState.value = error
    }

    override fun dismissError() {
        _errorState.value = noError
    }
}
