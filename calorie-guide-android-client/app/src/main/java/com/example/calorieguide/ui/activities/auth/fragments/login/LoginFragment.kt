package com.example.calorieguide.ui.activities.auth.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.calorieguide.R
import com.example.calorieguide.ui.activities.auth.screens.LoginScreen
import com.example.calorieguide.ui.activities.main.MainActivity
import com.example.calorieguide.ui.utils.rememberWindowSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val windowsSize = requireActivity().rememberWindowSize()
                LoginScreen(windowsSize) {
                    findNavController().navigate(R.id.action_login_to_register)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(Intent(context, MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}