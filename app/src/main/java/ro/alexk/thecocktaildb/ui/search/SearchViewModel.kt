package ro.alexk.thecocktaildb.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ro.alexk.thecocktaildb.data.CocktailRepository
import ro.alexk.thecocktaildb.data.model.Cocktail

class SearchViewModel(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(SearchViewState())
    val viewState = _viewState.asStateFlow()

    fun onSearch(query: String, filter: String) = viewModelScope.launch {
        _viewState.update {
            it.copy(
                isLoading = true,
                cocktails = emptyList()
            )
        }

        delay(1500)
        val cocktails: List<Cocktail> = when (filter) {
            "name" -> cocktailRepository.getByName(query)
            "ingredient" -> cocktailRepository.getByIngredient(query)
            "id" -> cocktailRepository.getById(query)
            else -> return@launch
        }

        _viewState.update {
            it.copy(
                isLoading = false,
                cocktails = cocktails
            )
        }
    }
}

data class SearchViewState(
    val isLoading: Boolean = false,
    val cocktails: List<Cocktail> = emptyList()
)