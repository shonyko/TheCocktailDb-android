package ro.alexk.thecocktaildb.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.alexk.thecocktaildb.data.model.Ingredient
import ro.alexk.thecocktaildb.databinding.IngredientListItemBinding

class IngredientsAdapter(
    private val ingredientClickListener: IngredientClickListener
) : RecyclerView.Adapter<IngredientsAdapter.IngredientListHolder>() {

    private val dataSource = mutableListOf<Ingredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientListHolder {
        val binding = IngredientListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return IngredientListHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientListHolder, position: Int) {
        val ingredient = dataSource[position]
        with(holder) {
            binding.name.text = ingredient.name
            binding.measure.text = ingredient.measure.ifBlank { "Not specified" }
            binding.deleteBtn.setOnClickListener {
                ingredientClickListener.onRemove(ingredient)
            }
        }
    }

    override fun getItemCount(): Int = dataSource.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(ingredients: List<Ingredient>) {
        dataSource.clear()
        dataSource.addAll(ingredients)
        this.notifyDataSetChanged()
    }

    inner class IngredientListHolder(val binding: IngredientListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

interface IngredientClickListener {
    fun onRemove(ingredient: Ingredient)
}