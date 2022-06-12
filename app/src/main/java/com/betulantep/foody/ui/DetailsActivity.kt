package com.betulantep.foody.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.betulantep.foody.R
import com.betulantep.foody.adapters.PagerAdapter
import com.betulantep.foody.data.database.entities.FavoriteEntity
import com.betulantep.foody.databinding.ActivityDetailsBinding
import com.betulantep.foody.ui.fragments.ingredients.IngredientsFragment
import com.betulantep.foody.ui.fragments.instructions.InstructionsFragment
import com.betulantep.foody.ui.fragments.overview.OverviewFragment
import com.betulantep.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.betulantep.foody.util.viewBinding
import com.betulantep.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDetailsBinding::inflate)
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel by viewModels<MainViewModel>()
    var savedFavoriteRecipe = false
    var savedFavoriteRecipeId = 0
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
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
        val pagerAdapter = PagerAdapter(resultBundle, titles, fragments, this)
        binding.viewPager2.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.saveToFavoritesMenu)
        checkSavedRecipes(menuItem)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        else if(item.itemId == R.id.saveToFavoritesMenu && !savedFavoriteRecipe){
            saveToFavorite(item)

        }else if(item.itemId == R.id.saveToFavoritesMenu && savedFavoriteRecipe){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, Observer { listFavorite ->
            try {
                savedFavoriteRecipe = false
                for (savedRecipe in listFavorite) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedFavoriteRecipeId = savedRecipe.id
                        savedFavoriteRecipe = true
                        break
                    }
                    changeMenuItemColor(menuItem, R.color.white)
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(0, args.result)
        mainViewModel.insertFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        savedFavoriteRecipe = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(savedFavoriteRecipeId,args.result)
        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Removed from Favorites")
        savedFavoriteRecipe = false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}
            .show()
    }

}