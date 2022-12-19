package ro.alexk.thecocktaildb.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ro.alexk.thecocktaildb.api.data.CocktailList

const val CONTENT_TYPE = "Content-Type: application/json"

interface CocktailApi {

    @Headers(CONTENT_TYPE)
    @GET("search.php")
    suspend fun getByName(@Query("s") name: String): Response<CocktailList>

    @Headers(CONTENT_TYPE)
    @GET("filter.php")
    suspend fun getByIngredient(@Query("i") ingredient: String): Response<CocktailList>

    @Headers(CONTENT_TYPE)
    @GET("lookup.php")
    suspend fun getById(@Query("i") id: String): Response<CocktailList>

    companion object {

        fun create(): CocktailApi {
            return Retrofit.Builder()
                .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CocktailApi::class.java)
        }
    }
}