package ro.alexk.thecocktaildb.data

import retrofit2.Response
import ro.alexk.thecocktaildb.api.CocktailApi
import ro.alexk.thecocktaildb.api.data.CocktailAPI
import ro.alexk.thecocktaildb.api.data.CocktailList
import ro.alexk.thecocktaildb.data.model.Cocktail
import ro.alexk.thecocktaildb.data.model.Ingredient

class CocktailRepository(private val cocktailApi: CocktailApi) {

    private var cached: MutableList<Cocktail> = mutableListOf()

    suspend fun getByName(name: String) = handleResponse(cocktailApi.getByName(name))

    suspend fun getByIngredient(ingredient: String) =
        handleResponse(cocktailApi.getByIngredient(ingredient))

    suspend fun getById(id: String) = handleResponse(cocktailApi.getById(id))

    fun getCached() = cached

    fun update(cocktail: Cocktail) {
        val idx = cached.indexOfFirst { it.id == cocktail.id }
        cached.removeAt(idx)
        cached.add(idx, cocktail)
    }

    fun delete(cocktail: Cocktail) {
        cached.removeIf { it.id == cocktail.id }
    }

    private fun handleResponse(res: Response<CocktailList>): MutableList<Cocktail> {
        println(res)
        if (res.body() == null || res.code() != 200) {
            println("Failed to fetch")
            return mutableListOf()
        }

        cached = map(res.body()!!)
        return cached
    }

    private fun map(cocktailList: CocktailList): MutableList<Cocktail> {
        if (cocktailList.drinks == null) return mutableListOf()
        return cocktailList.drinks!!.map { map(it) }.toMutableList()
    }

    private fun map(cocktail: CocktailAPI): Cocktail {
        val ingredients = mutableListOf<Ingredient>()

        val ingredientNames = CocktailAPI::class.java.declaredFields
            .filter { it.name.contains("Ingredient") }
            .sortedBy { it.name.replace("strIngredient", "").toInt() }
            .map {
                it.isAccessible = true
                it.get(cocktail) as String?
            }

        val ingredientMeasures = CocktailAPI::class.java.declaredFields
            .filter { it.name.contains("Measure") }
            .sortedBy { it.name.replace("strMeasure", "").toInt() }
            .map {
                it.isAccessible = true
                (it.get(cocktail) ?: "") as String
            }

        for ((idx, i) in ingredientNames.withIndex()) {
            if (i == null) break
            ingredients.add(Ingredient(i, ingredientMeasures[idx]))
        }

        println(cocktail.idDrink)
        println(cocktail.strDrink)
        println(cocktail.strCategory)
        println(ingredients)
        println(cocktail.strDrinkThumb)

        return Cocktail(
            id = cocktail.idDrink,
            name = cocktail.strDrink,
            category = cocktail.strCategory ?: "",
            ingredients = ingredients,
            imgUrl = cocktail.strDrinkThumb
        )
    }
}