package com.example.calorieguide.ui.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.calorieguide.R
import com.example.calorieguide.databinding.ActivityMainBinding
import com.example.calorieguide.ui.activities.auth.AuthActivity
import com.example.calorieguide.ui.activities.main.fragments.home.HomeFragment.Companion.USER_KEY
import com.example.calorieguide.ui.activities.main.fragments.profile.ProfileViewModel
import com.example.calorieguide.ui.utils.OnBackPressedListener
import com.example.core.model.User
import com.example.core.model.UserRole
import com.example.core.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: Repository

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var appBarConfiguration : AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_users,
                R.id.navigation_report,
                R.id.navigation_profile
            )
        )

        appBarConfiguration?.let {
            setupActionBarWithNavController(navController, it)
        }

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.navigation_home -> R.string.home_label
                R.id.navigation_users -> R.string.users_label
                R.id.navigation_report -> R.string.report_label
                R.id.navigation_profile -> R.string.profile_label
                R.id.navigation_users_food_list ->
                    arguments?.getParcelable<User>(USER_KEY)?.username
                else -> null
            }?.let {
                when (it) {
                    is String -> binding.appBar.title.text = it
                    is Int -> binding.appBar.title.setText(it)
                }
            }
        }

        repository.onSignOut().observe(this) {
            if (it) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        viewModel.profile.observe(this) {
            val isAdmin = it?.role == UserRole.ADMIN
            navView.menu.findItem(R.id.navigation_users).isVisible = isAdmin
            navView.menu.findItem(R.id.navigation_report).isVisible = isAdmin
        }
    }

    override fun onBackPressed() {
        val navFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        if (navFragment is NavHostFragment) {
            when (val fragment = navFragment.getChildFragmentManager().fragments[0]) {
                is OnBackPressedListener -> {
                    if (!fragment.onBackPressed()) super.onBackPressed()
                    return
                }
            }
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        appBarConfiguration?.let {
            return findNavController(R.id.nav_host).navigateUp()
        }
        return super.onSupportNavigateUp()
    }
}