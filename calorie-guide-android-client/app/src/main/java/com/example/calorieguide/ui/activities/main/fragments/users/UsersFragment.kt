package com.example.calorieguide.ui.activities.main.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorieguide.databinding.FragmentUsersBinding
import com.example.calorieguide.ui.recyclerview.user.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()

    private var _binding: FragmentUsersBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshData()

        val adapter = UserListAdapter {
            val action =
                UsersFragmentDirections.actionNavigationUsersToNavigationUsersFoodList(it)
            findNavController().navigate(action)
        }

        binding.usersList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.userList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing) {
                viewModel.updateUserList()
            }
        }

        viewModel.tokenError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}