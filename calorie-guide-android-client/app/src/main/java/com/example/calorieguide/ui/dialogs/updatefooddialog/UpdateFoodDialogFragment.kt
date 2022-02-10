package com.example.calorieguide.ui.dialogs.updatefooddialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.calorieguide.R
import com.example.calorieguide.databinding.UpdateFoodDialogBinding
import com.example.calorieguide.ui.dialogs.DialogListener
import com.example.calorieguide.ui.dialogs.addfooddialog.FoodDialogViewModel
import com.example.calorieguide.ui.utils.DateTimePicker
import com.example.calorieguide.utils.TimeUtils.toFormattedTimeDate
import com.example.core.model.Food
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateFoodDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "UpdateFoodDialogFragment"
        const val FOOD_ITEM_KEY = "food_item"
    }

    private val viewModel: FoodDialogViewModel by viewModels()

    private var _binding: UpdateFoodDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: DialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? DialogListener ?: context as? DialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = UpdateFoodDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setLayout(resources.getDimension(R.dimen.page_width).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val food = arguments?.get(FOOD_ITEM_KEY) as? Food

        if (food == null) {
            Toast.makeText(requireContext(), R.string.error_unknown, Toast.LENGTH_LONG).show()
            dismiss()
            return dialog
        }

        if (savedInstanceState == null) {
            binding.name.setText(food.name)
            viewModel.setTimeDate(food.timestamp)
            binding.calories.setText(food.calories.toString())
        }

        binding.timeDate.setOnClickListener {
            DateTimePicker.show(
                childFragmentManager,
                "TimeDatePicker",
                viewModel.timeDate.value
            ) {
                viewModel.setTimeDate(it)
            }
        }

        viewModel.timeDate.observe(this) {
            if (it != null) {
                binding.timeDate.error = null
                binding.timeDate.setText(it.toFormattedTimeDate())
            }
        }

        binding.cancelButton.setOnClickListener {
            listener?.onDialogNegativeClick(TAG)
            dialog.dismiss()
        }

        binding.saveButton.setOnClickListener {
            val foodName = binding.name.text.toString()
            val time = viewModel.timeDate.value
            val calories = binding.calories.text.toString().toIntOrNull()

            if (viewModel.validateInput(foodName, calories) && time != null && calories != null) {
                val updatedFood = Food(food.id, "_", foodName, time, calories)
                val bundle = Bundle()
                bundle.putParcelable(FOOD_ITEM_KEY, updatedFood)
                listener?.onDialogPositiveClick(TAG, bundle)
                dialog.dismiss()
            }
        }

        binding.deleteButton.setOnClickListener {

        }

        viewModel.nameError.observe(this) {
            binding.name.error = if (it != null) getString(it) else null
        }

        viewModel.timeDateError.observe(this) {
            binding.timeDate.error = if (it != null) getString(it) else null
        }

        viewModel.caloriesError.observe(this) {
            binding.calories.error = if (it != null) getString(it) else null
        }

        return dialog
    }

    fun show(childFragmentManager: FragmentManager, food: Food) {
        val bundle = Bundle()
        bundle.putParcelable(FOOD_ITEM_KEY, food)
        this.arguments = bundle
        show(childFragmentManager, TAG)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDialogDismiss(TAG)
    }
}