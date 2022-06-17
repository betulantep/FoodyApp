package com.betulantep.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.betulantep.foody.data.database.entities.FoodJokeEntity
import com.betulantep.foody.models.FoodJoke
import com.betulantep.foody.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {
    companion object {
        @BindingAdapter("readApiResponseJoke", "readDatabaseJoke", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.VISIBLE
                        is MaterialCardView -> view.visibility = View.INVISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> view.visibility = View.VISIBLE
                    }
                }
            }
        }

        @BindingAdapter(
            "readApiResponseJokeError",
            "readDatabaseResponseJokeError",
            requireAll = true
        )
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if (apiResponse is NetworkResult.Error)
                if (database.isNullOrEmpty()) {
                    when (view) {
                        is ImageView -> view.visibility = View.VISIBLE
                        is TextView -> {
                            view.visibility = View.VISIBLE
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
        }
    }
}


