package com.example.calorieguide.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorieguide.databinding.FragmentFilterFoodPageBinding
import com.example.calorieguide.ui.dialogs.DialogListener
import com.example.calorieguide.ui.dialogs.updatefooddialog.UpdateFoodDialogFragment
import com.example.calorieguide.ui.recyclerview.food.FoodListAdapter
import com.example.calorieguide.ui.utils.DatePicker
import com.example.calorieguide.utils.TimeUtils.DAY
import com.example.calorieguide.utils.TimeUtils.SECOND
import com.example.calorieguide.utils.TimeUtils.toFormattedDate
import com.example.calorieguide.utils.TimeUtils.toLocalTime
import com.example.core.model.Food
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFoodPageFragment : Fragment(), DialogListener {

    private val homeViewModel: HomeViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val viewModel: FoodPageViewModel by viewModels()

    private var _binding: FragmentFilterFoodPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val USERNAME_KEY = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterFoodPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(USERNAME_KEY)

        viewModel.initList(viewModel.getStartTime(), viewModel.getEndTime(), username)

        val adapter = FoodListAdapter(requireContext(), true) {
            UpdateFoodDialogFragment().show(childFragmentManager, it)
        }

        binding.foodList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.foodList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyListMessage.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshData(viewModel.getStartTime(), viewModel.getEndTime(), username)
        }

        homeViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing) {
                viewModel.updateList()
            }
        }

        val fromDatePickerListener: (timestamp: Long?) -> Unit = {
            if (it != null) {
                val fromTime = it.toLocalTime()
                viewModel.initList(fromTime, viewModel.getEndTime(), username)
                homeViewModel.refreshData(fromTime, viewModel.getEndTime(), username)
                binding.listHeader.fromButton.text = fromTime.toFormattedDate()
            }
        }

        binding.listHeader.fromButton.text = viewModel.getStartTime().toFormattedDate()
        binding.listHeader.fromButton.setOnClickListener {
            val datePicker = DatePicker.get(viewModel.getStartTime())
            datePicker.addOnPositiveButtonClickListener {
                fromDatePickerListener(it)
            }
            datePicker.show(childFragmentManager, "FromDatePicker")
        }

        (childFragmentManager.findFragmentByTag("FromDatePicker") as? MaterialDatePicker<*>)
            ?.addOnPositiveButtonClickListener {
                fromDatePickerListener(it as? Long)
            }

        val toDatePickerListener: (timestamp: Long?) -> Unit = {
            if (it != null) {
                val toTime = it.toLocalTime() + DAY - SECOND
                viewModel.initList(viewModel.getStartTime(), toTime, username)
                homeViewModel.refreshData(viewModel.getStartTime(), toTime, username)
                binding.listHeader.toButton.text = toTime.toFormattedDate()
            }
        }

        binding.listHeader.toButton.text = viewModel.getEndTime().toFormattedDate()
        binding.listHeader.toButton.setOnClickListener {
            val datePicker = DatePicker.get(viewModel.getEndTime())
            datePicker.addOnPositiveButtonClickListener {
                toDatePickerListener(it)
            }
            datePicker.show(childFragmentManager, "ToDatePicker")
        }

        (childFragmentManager.findFragmentByTag("ToDatePicker") as? MaterialDatePicker<*>)
            ?.addOnPositiveButtonClickListener {
                toDatePickerListener(it as? Long)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDialogPositiveClick(tag: String, bundle: Bundle?) {
        val food: Food? = bundle?.getParcelable(UpdateFoodDialogFragment.FOOD_ITEM_KEY)
        if (food != null) {
            homeViewModel.updateFoodItem(food)
        }
    }

    override fun onDialogNegativeClick(tag: String, bundle: Bundle?) {
        val id: String? = bundle?.getString(UpdateFoodDialogFragment.FOOD_ID_KEY)
        if (id != null) {
            homeViewModel.deleteFood(id)
        }
    }

    override fun onDialogDismiss(tag: String, bundle: Bundle?) { }
}