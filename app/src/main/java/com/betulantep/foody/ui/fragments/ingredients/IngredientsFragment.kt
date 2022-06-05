package com.betulantep.foody.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.betulantep.foody.R
import com.betulantep.foody.adapters.IngredientsAdapter
import com.betulantep.foody.databinding.FragmentIngredientsBinding
import com.betulantep.foody.models.Result
import com.betulantep.foody.util.Constants
import com.betulantep.foody.util.viewBinding

class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {

    private val binding by viewBinding(FragmentIngredientsBinding::bind)
    private val mAdapter: IngredientsAdapter by lazy {
        IngredientsAdapter()}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_BUNDLE_KEY)
        setupRecyclerView()
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }
    }

    private fun setupRecyclerView() {
        binding.rvIngredients.adapter = mAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
    }
}