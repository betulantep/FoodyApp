package com.betulantep.foody.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.betulantep.foody.MainViewModel
import com.betulantep.foody.adapters.RecipesAdapter
import com.betulantep.foody.databinding.FragmentRecipesBinding
import com.betulantep.foody.util.Constants.Companion.API_KEY
import com.betulantep.foody.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater,container,false)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupRecyclerView()
        requestApiData()
        return binding.root
    }

    private fun requestApiData(){
        mainViewModel.getRecipes(applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let{ mAdapter.setData(it)}
                }
                is NetworkResult.Error -> {
                   hideShimmerEffect()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun applyQueries(): HashMap<String,String>{
        val queries: HashMap<String,String> = HashMap()
        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return queries
    }

    private fun setupRecyclerView(){
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() = with(binding){
        shimmerFrameLayout.startShimmer()
        recyclerview.visibility = View.GONE
    }
    private fun hideShimmerEffect() = with(binding){
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}