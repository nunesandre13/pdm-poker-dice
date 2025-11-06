package pt.isel.pdm.utils

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.State

interface ViewModelState <S: State,E: DomainError> {

        val stateUi: StateFlow<S>
        val errorState: StateFlow<E>

        fun navigateTo(newState: S)
        fun emitError(error: E)
        fun dismissError()
}