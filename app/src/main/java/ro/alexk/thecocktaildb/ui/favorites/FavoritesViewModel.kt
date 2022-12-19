package ro.alexk.thecocktaildb.ui.favorites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ro.alexk.thecocktaildb.data.FavoriteRepository
import ro.alexk.thecocktaildb.data.model.Cocktail

class FavoritesViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(FavoriteViewState())
    val viewState = _viewState.asStateFlow()

    fun getFavorites() {
        _viewState.update {
            it.copy(
                cocktails = favoriteRepository.getFavorites()
            )
        }
    }

    fun clearAll() {
        favoriteRepository.clearAll()
        getFavorites()
    }
}

data class FavoriteViewState(
    val cocktails: List<Cocktail> = emptyList()
)