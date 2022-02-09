package com.example.calorieguide.ui.activities.main.fragments.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.calorieguide.databinding.FragmentReportBinding
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}