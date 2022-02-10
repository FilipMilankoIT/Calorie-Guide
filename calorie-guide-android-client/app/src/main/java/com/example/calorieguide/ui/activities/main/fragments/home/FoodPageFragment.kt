package com.example.calorieguide.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorieguide.databinding.FragmentFoodPageBinding
import com.example.calorieguide.ui.dialogs.DialogListener
import com.example.calorieguide.ui.dialogs.updatefooddialog.UpdateFoodDialogFragment
import com.example.calorieguide.ui.recyclerview.food.FoodListAdapter
import com.example.core.model.Food
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodPageFragment : Fragment(), DialogListener {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: FoodPageViewModel by viewModels()

    private var _binding: FragmentFoodPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val START_TIME_KEY = "start_time_key"
        const val END_TIME_KEY = "end_time_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FoodListAdapter(requireContext()) {
            UpdateFoodDialogFragment().show(childFragmentManager, it)
        }
        binding.foodList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        val startTime = arguments?.getLong(START_TIME_KEY)
        val endTime = arguments?.getLong(END_TIME_KEY)

        val listObserver = Observer<PagedList<Food>> {
            adapter.submitList(it)
            binding.emptyListMessage.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        if (startTime != null && endTime != null) {
            homeViewModel.refreshData(startTime, endTime)
            viewModel.initList(startTime, endTime, viewLifecycleOwner, listObserver)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshData(viewModel.getStartTime(), viewModel.getEndTime())
        }

        homeViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing) {
                viewModel.updateList(viewLifecycleOwner, listObserver)
            }
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