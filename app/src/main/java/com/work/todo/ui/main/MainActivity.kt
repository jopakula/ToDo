package com.work.todo.ui.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.task) {
                binding.fabAdd.hide()
            } else {
                binding.fabAdd.show()
            }
        }

        binding.fabAdd.setOnClickListener {
            navController.navigate(R.id.task)
        }

        onBackPressedDispatcher.addCallback(this) {
            val topLevelDestinations = setOf(R.id.home, R.id.calendar)

            if (navController.currentDestination?.id in topLevelDestinations) {
                finish()
            } else {
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        }
    }
}