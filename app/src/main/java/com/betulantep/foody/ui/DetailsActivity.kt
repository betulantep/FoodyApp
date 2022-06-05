package com.betulantep.foody.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.betulantep.foody.R
import com.betulantep.foody.adapters.PagerAdapter
import com.betulantep.foody.databinding.ActivityDetailsBinding
import com.betulantep.foody.ui.fragments.ingredients.IngredientsFragment
import com.betulantep.foody.ui.fragments.instructions.InstructionsFragment
import com.betulantep.foody.ui.fragments.overview.OverviewFragment
import com.betulantep.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.betulantep.foody.util.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDetailsBinding::inflate)
    private val args by navArgs<DetailsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_BUNDLE_KEY, args.result)
        val pagerAdapter = PagerAdapter(resultBundle,titles,fragments,this)
        binding.viewPager2.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}