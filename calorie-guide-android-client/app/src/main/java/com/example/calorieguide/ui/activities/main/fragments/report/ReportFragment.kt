package com.example.calorieguide.ui.activities.main.fragments.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorieguide.databinding.FragmentReportBinding
import com.example.calorieguide.ui.activities.main.fragments.users.UsersFragmentDirections
import com.example.calorieguide.ui.recyclerview.user.UserListAdapter
import com.example.calorieguide.ui.recyclerview.useraveragecalories.UserAverageCaloriesListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private val viewModel: ReportViewModel by viewModels()

    private var _binding: FragmentReportBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.thisWeekCount.observe(viewLifecycleOwner) {
            binding.thisWeekNumber.text = it.toString()
        }

        viewModel.lastWeekCount.observe(viewLifecycleOwner) {
            binding.lastWeekNumber.text = it.toString()
        }

        val adapter = UserAverageCaloriesListAdapter(requireContext())

        binding.usersList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.userList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshReport()
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.tokenError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshReport()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}