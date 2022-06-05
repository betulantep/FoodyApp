package com.betulantep.foody.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import coil.load
import com.betulantep.foody.R
import com.betulantep.foody.databinding.FragmentOverviewBinding
import com.betulantep.foody.models.Result
import com.betulantep.foody.ui.DetailsActivityArgs
import com.betulantep.foody.util.viewBinding
import org.jsoup.Jsoup

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val binding by viewBinding(FragmentOverviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")

        myBundle?.let {
            binding.apply {
                ivMain.load(myBundle.image)
                tvTitle.text = myBundle.title
                tvLikes.text = myBundle.aggregateLikes.toString()
                tvTime.text = myBundle.readyInMinutes.toString()
            }
            val summary = Jsoup.parse(myBundle.summary).text()
            binding.tvSummary.text = summary

            val greenColor = ContextCompat.getColor(requireContext(),R.color.green)

            if(myBundle.veryHealthy){
                binding.ivCheckHealthy.setColorFilter(greenColor)
                binding.tvCheckHealthy.setTextColor(greenColor)
            }
            if(myBundle.vegetarian){
                binding.ivCheckVegetarian.setColorFilter(greenColor)
                binding.tvCheckVegetarian.setTextColor(greenColor)
            }
            if(myBundle.vegan){
                binding.ivCheckVegan.setColorFilter(greenColor)
                binding.tvCheckVegan.setTextColor(greenColor)
            }
            if(myBundle.glutenFree){
                binding.ivCheckGlutenFree.setColorFilter(greenColor)
                binding.tvCheckGlutenFree.setTextColor(greenColor)
            }
            if(myBundle.dairyFree){
                binding.ivCheckDairyFree.setColorFilter(greenColor)
                binding.tvCheckDairyFree.setTextColor(greenColor)
            }
            if(myBundle.cheap){
                binding.ivCheckCheap.setColorFilter(greenColor)
                binding.tvCheckCheap.setTextColor(greenColor)
            }
        }


    }
}