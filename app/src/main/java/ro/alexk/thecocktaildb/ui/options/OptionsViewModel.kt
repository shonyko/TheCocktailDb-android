package ro.alexk.thecocktaildb.ui.options

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ro.alexk.thecocktaildb.data.LoginRepository

class OptionsViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(OptionsViewState())
    val viewState = _viewState.asStateFlow()

    init {
        _viewState.update {
            it.copy(
                username = loginRepository.user?.displayName
            )
        }
    }
}

data class OptionsViewState(
    val username: String? = ""
)