package com.betulantep.foody.ui.fragments.instructions

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.betulantep.foody.R
import com.betulantep.foody.databinding.FragmentInstructionsBinding
import com.betulantep.foody.models.Result
import com.betulantep.foody.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.betulantep.foody.util.viewBinding

class InstructionsFragment : Fragment(R.layout.fragment_instructions) {

    private val binding by viewBinding(FragmentInstructionsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_BUNDLE_KEY)

        binding.instructionsWebView.webViewClient = object : WebViewClient(){}
        val webSiteUrl = myBundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(webSiteUrl)
    }
}