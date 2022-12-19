package ro.alexk.thecocktaildb.data

import ro.alexk.thecocktaildb.data.model.Cocktail

class FavoriteRepository {

    private var favorites: MutableList<Cocktail> = mutableListOf()

    fun addFavorite(cocktail: Cocktail) {
        if (findById(cocktail.id) != null) {
            println("Already favorite")
            return
        }

        favorites.add(cocktail)
    }

    fun removeFavorite(cocktail: Cocktail) {
        favorites.removeIf { it.id == cocktail.id }
    }

    fun isFavorite(cocktail: Cocktail): Boolean {
        return findById(cocktail.id) != null
    }

    fun getFavorites() = favorites.toList()

    fun clearAll() {
        favorites.clear()
    }

    private fun findById(id: String): Cocktail? {
        return favorites.firstOrNull { it.id == id }
    }
}