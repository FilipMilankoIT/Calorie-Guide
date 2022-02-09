package com.example.calorieguide.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.calorieguide.R
import com.example.calorieguide.databinding.FragmentHomeBinding
import com.example.calorieguide.ui.pager.FoodPagerAdapter
import com.example.calorieguide.ui.utils.OnBackPressedListener
import com.example.calorieguide.utils.TimeUtils.toFormattedDate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnBackPressedListener {

    private val viewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        binding.pager.setCurrentItem(viewModel.currentPage, false)
        binding.pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                updatePageDate(position)
            }
        })
        updatePageDate(viewModel.currentPage)

        binding.listHeader.previousButton.setOnClickListener {
            binding.pager.currentItem--
        }

        binding.listHeader.nextButton.setOnClickListener {
            binding.pager.currentItem++
        }

        viewModel.isFilterOn.observe(viewLifecycleOwner) {
            binding.filterPage.visibility = if (it) View.VISIBLE else View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_list_header_menu, menu)
        menu[0].setIcon(
            if (viewModel.isFilterOn.value == true) R.drawable.close else R.drawable.search
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.navigation_filter -> {
                item.setIcon(
                    if (viewModel.isFilterOn.value != true) R.drawable.close else R.drawable.search
                )
                viewModel.toggleFilter()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed(): Boolean {
        return if (viewModel.isFilterOn.value == true) {
            viewModel.toggleFilter()
            true
        } else false
    }

    private fun updatePageDate(page: Int) {
        viewModel.currentPage = page
        val startTime = FoodPagerAdapter.getStartTimeFromPosition(page)
        binding.listHeader.date.text = startTime.toFormattedDate()
    }
}