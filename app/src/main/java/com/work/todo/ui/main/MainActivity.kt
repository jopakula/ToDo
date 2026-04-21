package com.work.todo.ui.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.work.todo.R
import com.work.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id == R.id.addTask) {
                navController.popBackStack()
            }

            NavigationUI.onNavDestinationSelected(item, navController)
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addTask -> {
                    binding.fabAdd.hide()
                    binding.bottomNavigationView.menu.setGroupCheckable(0, true, false)
                    for (i in 0 until binding.bottomNavigationView.menu.size) {
                        binding.bottomNavigationView.menu[i].isChecked = false
                    }
                }

                R.id.editTask -> {
                    binding.fabAdd.hide()
                    binding.bottomNavigationView.menu.setGroupCheckable(0, true, true)
                    binding.bottomNavigationView.menu.findItem(R.id.home).isChecked = true
                }

                R.id.allTasks -> {
                    binding.fabAdd.show()
                    binding.bottomNavigationView.menu.setGroupCheckable(0, true, true)
                    binding.bottomNavigationView.menu.findItem(R.id.home).isChecked = true
                }

                else -> {
                    binding.fabAdd.show()
                    binding.bottomNavigationView.menu.setGroupCheckable(0, true, true)
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            navController.navigate(R.id.addTask, null, navOptions {
                launchSingleTop = true
            })
        }

        onBackPressedDispatcher.addCallback(this) {
            val topLevelDestinations = setOf(R.id.home, R.id.calendar)
            if (navController.currentDestination?.id in topLevelDestinations) {
                finish()
            } else {
                navController.navigateUp()
            }
        }
    }
}