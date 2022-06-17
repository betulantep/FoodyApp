package com.betulantep.foody.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.betulantep.foody.R
import com.betulantep.foody.databinding.FragmentFoodJokeBinding
import com.betulantep.foody.util.Constants.Companion.API_KEY
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
class FoodJokeFragment : Fragment(R.layout.fragment_food_joke) {

    private val binding by viewBinding(FragmentFoodJokeBinding::bind)
    private val mainViewModel by viewModels<MainViewModel>()
    private var foodJoke = "No Food Joke"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.tvFoodJoke.text = response.data?.text
                    response.data?.let {
                        foodJoke = it.text
                    }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("FoodJokeFragment", "Loading")
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share_food_joke_menu){
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()){
                    binding.tvFoodJoke.text = it[0].foodJoke.text
                    foodJoke = it[0].foodJoke.text
                }
            })
        }
    }


}