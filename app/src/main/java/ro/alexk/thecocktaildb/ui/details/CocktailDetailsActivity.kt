package ro.alexk.thecocktaildb.ui.details

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import ro.alexk.thecocktaildb.MyApplication
import ro.alexk.thecocktaildb.R
import ro.alexk.thecocktaildb.data.model.Ingredient
import ro.alexk.thecocktaildb.databinding.ActivityCocktailDetailsBinding

class CocktailDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCocktailDetailsBinding

    private lateinit var viewModel: CocktailDetailsViewModel

    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCocktailDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dependencyContainer = (application as MyApplication).appContainer
        viewModel = CocktailDetailsViewModel(
            dependencyContainer.cocktailRepository,
            dependencyContainer.favoriteRepository
        )

        val id = intent.extras?.getString("id") ?: ""
        viewModel.selectCocktail(id)

        setupView()
        observeState()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    private fun setupView() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ingredientsAdapter = IngredientsAdapter(object : IngredientClickListener {
            override fun onRemove(ingredient: Ingredient) {
                viewModel.removeIngredient(ingredient)
            }
        })
        binding.description.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientsAdapter
        }

        binding.addBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val measure = binding.measure.text.toString()

            viewModel.addIngredient(Ingredient(name = name, measure = measure))

            binding.name.text.clear()
            binding.measure.text.clear()
        }

        binding.favoriteBtn.setOnClickListener {
            viewModel.toggleFavorite(viewModel.viewState.value.cocktail!!)
        }

        binding.deleteBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
            dialogBuilder
                .setTitle("Please confirm")
                .setMessage("Do you want to delete this?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.delete(viewModel.viewState.value.cocktail!!)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect { state ->
                binding.toolbar.title =
                    String.format("%s (%s)", state.cocktail?.name, state.cocktail?.id)
                binding.image.load(state.cocktail?.imgUrl)
                ingredientsAdapter.setDataSource(state.cocktail?.ingredients ?: emptyList())

                var color = Color.BLACK
                if (state.isFavorite) color = Color.parseColor("#dc143c")
                binding.favoriteIcon.setColorFilter(color)
            }
        }
    }
}