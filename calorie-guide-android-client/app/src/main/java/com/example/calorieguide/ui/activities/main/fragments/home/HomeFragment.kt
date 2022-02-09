package com.example.calorieguide.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.calorieguide.databinding.FragmentHomeBinding
import com.example.calorieguide.ui.pager.FoodPagerAdapter
import com.example.calorieguide.utils.TimeUtils.toFormattedDate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = FoodPagerAdapter(requireActivity())
        binding.pager.setCurrentItem(FoodPagerAdapter.START_POSITION, false)
        binding.pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                updatePageDate(position)
            }
        })
        updatePageDate(FoodPagerAdapter.START_POSITION)

        binding.previousButton.setOnClickListener {
            binding.pager.currentItem--
        }

        binding.nextButton.setOnClickListener {
            binding.pager.currentItem++
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.tokenError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        viewModel.waiting.observe(viewLifecycleOwner) {
            val isEnabled = !it
            binding.loader.visibility = if (isEnabled) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updatePageDate(page: Int) {
        val startTime = FoodPagerAdapter.getStartTimeFromPosition(page)
        binding.date.text = startTime.toFormattedDate()
    }
}