package ro.alexk.thecocktaildb.data.di

import android.content.Context
import ro.alexk.thecocktaildb.api.CocktailApi
import ro.alexk.thecocktaildb.data.CocktailRepository
import ro.alexk.thecocktaildb.data.FavoriteRepository
import ro.alexk.thecocktaildb.data.LoginDataSource
import ro.alexk.thecocktaildb.data.LoginRepository

interface AppContainer {
    val loginRepository: LoginRepository
    val cocktailRepository: CocktailRepository
    val favoriteRepository: FavoriteRepository
}

class AppContainerImpl(context: Context) : AppContainer {

    override val loginRepository: LoginRepository by lazy {
        LoginRepository(LoginDataSource())
    }

    override val cocktailRepository: CocktailRepository by lazy {
        CocktailRepository(CocktailApi.create())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepository()
    }
}