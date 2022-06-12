package com.betulantep.foody.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.betulantep.foody.R
import com.betulantep.foody.adapters.FavoriteRecipesAdapter
import com.betulantep.foody.databinding.FragmentFavoriteRecipesBinding
import com.betulantep.foody.util.viewBinding
import com.betulantep.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {

    private val binding by viewBinding(FragmentFavoriteRecipesBinding::bind)
    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }

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
        binding.mAdapter = mAdapter
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(binding) {
        rvFavoriteRecipes.adapter = mAdapter
        rvFavoriteRecipes.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAllFavoriteRecipes) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "All Recipes Removed",Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.clearContextualActionMode()
    }

}