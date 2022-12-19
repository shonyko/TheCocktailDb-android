package ro.alexk.thecocktaildb.data.model

data class Cocktail(
    val id: String,
    val name: String,
    val category: String,
    val ingredients: List<Ingredient>,
    val imgUrl: String
)
