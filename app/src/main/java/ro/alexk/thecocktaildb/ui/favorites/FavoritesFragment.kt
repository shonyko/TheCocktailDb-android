package ro.alexk.thecocktaildb.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ro.alexk.thecocktaildb.MainActivity
import ro.alexk.thecocktaildb.R
import ro.alexk.thecocktaildb.databinding.FragmentFavoritesBinding
import ro.alexk.thecocktaildb.ui.details.CocktailDetailsActivity
import ro.alexk.thecocktaildb.ui.search.CocktailClickListener
import ro.alexk.thecocktaildb.ui.search.CocktailListAdapter

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var viewModel: FavoritesViewModel

    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dependencyContainer = (activity as MainActivity).dependencyContainer
        viewModel = FavoritesViewModel(dependencyContainer.favoriteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    private fun setupView() {
        cocktailListAdapter = CocktailListAdapter(object : CocktailClickListener {
            override fun onRemove(id: String) {
                TODO("Not yet implemented")
            }

            override fun itemSelected(id: String) {
                val intent = Intent(context, CocktailDetailsActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }

        }, (activity as MainActivity).dependencyContainer.favoriteRepository)
        binding.cocktailList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = cocktailListAdapter
        }

        binding.clearAllBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
            dialogBuilder
                .setTitle("Please confirm")
                .setMessage("Do you want to clear all favorites?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.clearAll()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect { state ->
                cocktailListAdapter.setDataSource(state.cocktails)

                binding.notFoundText.visibility =
                    if (state.cocktails.isEmpty()) View.VISIBLE else View.GONE

                binding.cocktailList.visibility =
                    if (state.cocktails.isEmpty()) View.INVISIBLE else View.VISIBLE
            }
        }
    }
}