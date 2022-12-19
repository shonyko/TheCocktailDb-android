package ro.alexk.thecocktaildb

import android.app.Application
import ro.alexk.thecocktaildb.data.di.AppContainer
import ro.alexk.thecocktaildb.data.di.AppContainerImpl

class MyApplication : Application() {

    val appContainer: AppContainer = AppContainerImpl(this)
}