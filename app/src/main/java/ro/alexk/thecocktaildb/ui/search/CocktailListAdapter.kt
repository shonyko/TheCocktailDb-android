package ro.alexk.thecocktaildb.ui.search

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ro.alexk.thecocktaildb.data.FavoriteRepository
import ro.alexk.thecocktaildb.data.model.Cocktail
import ro.alexk.thecocktaildb.databinding.FragmentCocktailsListItemBinding
import java.util.*

class CocktailListAdapter(
    private val cocktailClickListener: CocktailClickListener,
    private val favoriteRepository: FavoriteRepository
) : RecyclerView.Adapter<CocktailListAdapter.CocktailListHolder>() {

    private val dataSource = mutableListOf<Cocktail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailListHolder {
        val binding = FragmentCocktailsListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CocktailListHolder(binding)
    }

    override fun onBindViewHolder(holder: CocktailListHolder, position: Int) {
        val cocktail = dataSource[position]
        with(holder) {
            binding.title.text = String.format("%s (%s)", cocktail.name, cocktail.id)
            binding.description.text = String.format("%s", cocktail.category)
            binding.image.load(data = cocktail.imgUrl)

            var color = Color.BLACK
            if (favoriteRepository.isFavorite(cocktail)) color = Color.parseColor("#dc143c")
            binding.favoriteIcon.setColorFilter(color)

            binding.root.setOnClickListener {
                cocktailClickListener.itemSelected(cocktail.id)
            }
        }
    }

    override fun getItemCount(): Int = dataSource.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(cocktails: List<Cocktail>) {
        dataSource.clear()
        dataSource.addAll(cocktails)
        this.notifyDataSetChanged()
    }

    inner class CocktailListHolder(val binding: FragmentCocktailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

interface CocktailClickListener {
    fun onRemove(id: String)
    fun itemSelected(id: String)
}