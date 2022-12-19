package ro.alexk.thecocktaildb.ui.details

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ro.alexk.thecocktaildb.data.CocktailRepository
import ro.alexk.thecocktaildb.data.FavoriteRepository
import ro.alexk.thecocktaildb.data.model.Cocktail
import ro.alexk.thecocktaildb.data.model.Ingredient

class CocktailDetailsViewModel(
    private val cocktailRepository: CocktailRepository,
    private val favoriteRepository: FavoriteRepository
) {

    private val _state = MutableStateFlow(CocktailDetailsState())
    val viewState = _state.asStateFlow()

    fun selectCocktail(id: String) {
        val cocktail = cocktailRepository.getCached().firstOrNull { x -> x.id == id }
        _state.update { state ->
            state.copy(
                cocktail = cocktail,
                isFavorite = favoriteRepository.isFavorite(cocktail!!)
            )
        }
    }

    fun removeIngredient(ingredient: Ingredient) {
        if (_state.value.cocktail == null) return

        val cocktail = _state.value.cocktail!!
        val ingredients =
            cocktail.ingredients.filter { it.name != ingredient.name }

        val newCocktail = cocktail.copy(
            ingredients = ingredients
        )
        cocktailRepository.update(newCocktail)

        _state.update {
            it.copy(
                cocktail = newCocktail
            )
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        if (_state.value.cocktail == null) return

        val cocktail = _state.value.cocktail!!
        val ingredients = mutableListOf<Ingredient>()
        ingredients.addAll(cocktail.ingredients)
        ingredients.add(ingredient)

        val newCocktail = cocktail.copy(
            ingredients = ingredients
        )
        cocktailRepository.update(newCocktail)

        _state.update {
            it.copy(
                cocktail = newCocktail
            )
        }
    }

    fun toggleFavorite(cocktail: Cocktail) {
        if (favoriteRepository.isFavorite(cocktail)) {
            favoriteRepository.removeFavorite(cocktail)
        } else {
            favoriteRepository.addFavorite(cocktail)
        }

        _state.update { state ->
            state.copy(
                isFavorite = favoriteRepository.isFavorite(cocktail)
            )
        }
    }

    fun delete(cocktail: Cocktail) {
        cocktailRepository.delete(cocktail)
        favoriteRepository.removeFavorite(cocktail)
    }
}

data class CocktailDetailsState(
    val cocktail: Cocktail? = null,
    val isFavorite: Boolean = false
)