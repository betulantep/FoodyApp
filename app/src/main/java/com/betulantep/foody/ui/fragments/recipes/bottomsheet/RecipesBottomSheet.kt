package com.betulantep.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.betulantep.foody.R
import com.betulantep.foody.databinding.RecipesBottomSheetBinding
import com.betulantep.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.betulantep.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.betulantep.foody.util.viewBinding
import com.betulantep.foody.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class RecipesBottomSheet() : BottomSheetDialogFragment() {
    private val binding by viewBinding(RecipesBottomSheetBinding::inflate)
    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        observeRecipesViewModel()

        binding.chipGroupMealType.setOnCheckedChangeListener { group, selectedChipId ->
            chipGroupMealTypeListener(group, selectedChipId)
        }
        binding.chipGroupDietType.setOnCheckedChangeListener { group, selectedChipId ->
            chipGroupDietTypeListener(group, selectedChipId)
        }
        binding.btnApply.setOnClickListener { btnApplyClicked() }
        return binding.root
    }

    private fun observeRecipesViewModel() {
        recipesViewModel.readMealAndDietType.asLiveData()
            .observe(viewLifecycleOwner, Observer { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(value.selectedMealTypeId, binding.chipGroupMealType)
                updateChip(value.selectedDietTypeId, binding.chipGroupDietType)
            })
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("Recipes Bottom Sheet", e.message.toString())
            }
        }
    }

    private fun chipGroupDietTypeListener(group: ChipGroup, selectedChipId: Int) {
        val chip = group.findViewById<Chip>(selectedChipId)
        val selectedDietType = chip.text.toString().lowercase()
        dietTypeChip = selectedDietType
        dietTypeChipId = selectedChipId
    }

    private fun chipGroupMealTypeListener(group: ChipGroup, selectedChipId: Int) {
        val chip = group.findViewById<Chip>(selectedChipId)
        val selectedMealType = chip.text.toString().lowercase()
        mealTypeChip = selectedMealType
        mealTypeChipId = selectedChipId
    }

    private fun btnApplyClicked() {
        recipesViewModel.saveMealAndDietType(
            mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId
        )
        val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
        findNavController().navigate(action)
        Log.d("Main Course Gluten Free", mealTypeChip)
        Log.d("Main Course Gluten Free Id", mealTypeChipId.toString())
    }


}