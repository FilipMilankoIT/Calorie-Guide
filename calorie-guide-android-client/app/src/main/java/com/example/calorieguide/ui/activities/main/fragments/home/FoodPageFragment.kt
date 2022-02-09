package com.example.calorieguide.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorieguide.databinding.FragmentFoodPageBinding
import com.example.calorieguide.ui.recyclerview.food.FoodListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodPageFragment : Fragment() {

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
            // TODO Implement item click logic
        }
        binding.foodList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        val startTime = arguments?.getLong(START_TIME_KEY)
        val endTime = arguments?.getLong(END_TIME_KEY)

        if (startTime != null && endTime != null) {
            homeViewModel.refreshData(startTime, endTime)
            viewModel.initList(startTime, endTime)
            viewModel.getFoodList().observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding.emptyListMessage.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshData(viewModel.getStartTime(), viewModel.getEndTime())
        }

        homeViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (isRefreshing) {
                viewModel.updateList(viewLifecycleOwner)
                viewModel.getFoodList().observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                    binding.emptyListMessage.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}