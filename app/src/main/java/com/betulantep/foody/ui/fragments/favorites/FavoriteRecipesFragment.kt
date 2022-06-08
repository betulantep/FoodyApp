package com.betulantep.foody.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betulantep.foody.R
import com.betulantep.foody.adapters.FavoriteRecipesAdapter
import com.betulantep.foody.databinding.FragmentFavoriteRecipesBinding
import com.betulantep.foody.util.viewBinding
import com.betulantep.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {

    private val binding by viewBinding(FragmentFavoriteRecipesBinding::bind)
    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { FavoriteRecipesAdapter() }

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


}