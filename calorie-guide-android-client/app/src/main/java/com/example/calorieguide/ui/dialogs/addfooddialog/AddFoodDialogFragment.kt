package com.example.calorieguide.ui.dialogs.addfooddialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.calorieguide.databinding.AddFoodDialogBinding
import com.example.calorieguide.R
import com.example.calorieguide.ui.dialogs.DialogListener
import com.example.calorieguide.ui.utils.DatePicker
import com.example.calorieguide.ui.utils.TimePicker
import com.example.calorieguide.utils.TimeUtils.toFormattedTimeDate
import com.example.core.model.Food
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import android.widget.ArrayAdapter

@AndroidEntryPoint
class AddFoodDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AddFoodDialogFragment"
        const val FOOD_ITEM_KEY = "food_item"
    }

    private val viewModel: FoodDialogViewModel by viewModels()

    private var _binding: AddFoodDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: DialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? DialogListener ?: context as? DialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = AddFoodDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setLayout(resources.getDimension(R.dimen.page_width).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_dropdown_item_1line)
        binding.name.setAdapter(adapter)

        viewModel.savedFoodNames.observe(this) {
            if (it != null) adapter.addAll(it)
        }

        val timePickerListener: (timePicker: MaterialTimePicker, timestamp: Long) -> Unit =
            { timePicker, timestamp ->
                val cal = Calendar.getInstance()
                cal.timeInMillis = timestamp
                cal[Calendar.HOUR_OF_DAY] = timePicker.hour
                cal[Calendar.MINUTE] = timePicker.minute
                viewModel.setTimeDate(cal.timeInMillis)
            }

        val datePickerListener: (timestamp: Long?) -> Unit = { timestamp ->
            if (timestamp != null) {
                viewModel.selectedDate = timestamp
                val timePicker = TimePicker.get(viewModel.timeDate.value)
                timePicker.addOnPositiveButtonClickListener {
                    timePickerListener(timePicker, timestamp)
                }
                timePicker.show(childFragmentManager, TimePicker.TAG)
            }
        }

        binding.timeDate.setOnClickListener {
            val datePicker = DatePicker.get(viewModel.timeDate.value, true)
            datePicker.addOnPositiveButtonClickListener {
                datePickerListener(it)
            }
            datePicker.show(childFragmentManager, DatePicker.TAG)
        }

        (childFragmentManager.findFragmentByTag(DatePicker.TAG) as? MaterialDatePicker<*>)
            ?.addOnPositiveButtonClickListener {
                datePickerListener(it as? Long)
            }

        (childFragmentManager.findFragmentByTag(TimePicker.TAG) as? MaterialTimePicker)?.apply {
            addOnPositiveButtonClickListener {
                viewModel.selectedDate?.let {
                    timePickerListener(this, it)
                }
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
                val food = Food("_", "_", foodName, time, calories)
                val bundle = Bundle()
                bundle.putParcelable(FOOD_ITEM_KEY, food)
                listener?.onDialogPositiveClick(TAG, bundle)
                dialog.dismiss()
            }
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

    fun show(childFragmentManager: FragmentManager) {
        show(childFragmentManager, TAG)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDialogDismiss(TAG)
    }
}