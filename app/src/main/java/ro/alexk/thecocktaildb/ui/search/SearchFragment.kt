package ro.alexk.thecocktaildb.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ro.alexk.thecocktaildb.MainActivity
import ro.alexk.thecocktaildb.R
import ro.alexk.thecocktaildb.databinding.FragmentSearchBinding
import ro.alexk.thecocktaildb.ui.details.CocktailDetailsActivity

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: SearchViewModel

    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dependencyContainer = (activity as MainActivity).dependencyContainer
        viewModel = SearchViewModel(dependencyContainer.cocktailRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        cocktailListAdapter.setDataSource(viewModel.viewState.value.cocktails)
    }

    private fun setupView() {
        initDropdown()

        binding.searchBar.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String?) = false

            override fun onQueryTextSubmit(query: String?): Boolean {
                val filter = binding.spinner.selectedItem.toString()
                viewModel.onSearch(query ?: "", filter)
                binding.header.requestFocus()
                return false
            }
        })

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
    }

    private fun initDropdown() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.search_filter,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner.adapter = adapter
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect { state ->
                cocktailListAdapter.setDataSource(state.cocktails)

                binding.loading.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                binding.notFoundText.visibility =
                    if (state.cocktails.isEmpty() && !state.isLoading) View.VISIBLE else View.GONE

                binding.cocktailList.visibility =
                    if (state.cocktails.isEmpty()) View.INVISIBLE else View.VISIBLE
            }
        }
    }
}