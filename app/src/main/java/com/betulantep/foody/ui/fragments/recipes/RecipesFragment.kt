package com.betulantep.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.betulantep.foody.R
import com.betulantep.foody.adapters.RecipesAdapter
import com.betulantep.foody.databinding.FragmentRecipesBinding
import com.betulantep.foody.util.NetworkListener
import com.betulantep.foody.util.NetworkResult
import com.betulantep.foody.util.observeOnce
import com.betulantep.foody.util.viewBinding
import com.betulantep.foody.viewmodels.MainViewModel
import com.betulantep.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipesFragmentArgs>()
    private val binding by viewBinding(FragmentRecipesBinding::bind)
    private val mainViewModel: MainViewModel by viewModels()
    private val recipeViewModel: RecipesViewModel by viewModels()
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var networkListener: NetworkListener
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        observe()
        binding.fabRecipe.setOnClickListener { fabRecipeClicked() }
    }

    private fun observe() {
        //Back online listener
        recipeViewModel.readBackOnline.observe(viewLifecycleOwner, Observer {
            recipeViewModel.backOnline = it
        })

        //network listener
        networkListener = NetworkListener()
        lifecycleScope.launchWhenStarted {
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    readDatabase()
                }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    //Start Search Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.search_menu)
        searchView = (search.actionView as? SearchView)!!
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { searchApiData(it) }
        searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
    //End Search Menu

    private fun fabRecipeClicked() {
        if (recipeViewModel.networkStatus) {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        } else {
            recipeViewModel.showNetworkStatus()
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "read Database")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "remote source API")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipeViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> { showShimmerEffect() }
            }
        })
    }


    private fun showShimmerEffect() = with(binding) {
        shimmerFrameLayout.startShimmer()
        recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() = with(binding) {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
    }


}