package com.example.calorieguide.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.calorieguide.R
import com.example.calorieguide.databinding.FragmentHomeBinding
import com.example.calorieguide.ui.dialogs.DialogListener
import com.example.calorieguide.ui.dialogs.addfooddialog.AddFoodDialogFragment
import com.example.calorieguide.ui.pager.FoodPagerAdapter
import com.example.calorieguide.ui.utils.OnBackPressedListener
import com.example.calorieguide.utils.TimeUtils.toFormattedDate
import com.example.core.model.Food
import com.example.core.model.User
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnBackPressedListener, DialogListener {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var user: User? = null

    companion object {
        const val USER_KEY = "user"
    }

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

        user = arguments?.getParcelable(USER_KEY)

        if (savedInstanceState == null) {
            val fragment = FilterFoodPageFragment()
            val args = Bundle()
            args.putString(FilterFoodPageFragment.USERNAME_KEY, user?.username)
            fragment.arguments = args
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.filter_page, fragment).commit()
        }

        binding.pager.adapter = FoodPagerAdapter(this, user)
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

        binding.addButton.setOnClickListener {
            AddFoodDialogFragment().show(childFragmentManager)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.tokenError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        viewModel.waiting.observe(viewLifecycleOwner) {
            val isEnabled = !it
            binding.addButton.isEnabled = isEnabled
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
            requireActivity().invalidateOptionsMenu()
            true
        } else false
    }

    override fun onDialogPositiveClick(tag: String, bundle: Bundle?) {
        val food: Food? = bundle?.getParcelable(AddFoodDialogFragment.FOOD_ITEM_KEY)
        if (food != null) {
            viewModel.addFoodItem(food, user?.username)
        }
    }

    override fun onDialogNegativeClick(tag: String, bundle: Bundle?) { }

    override fun onDialogDismiss(tag: String, bundle: Bundle?) { }

    private fun updatePageDate(page: Int) {
        viewModel.currentPage = page
        val startTime = FoodPagerAdapter.getStartTimeFromPosition(page)
        binding.listHeader.date.text = startTime.toFormattedDate()
    }
}