package com.work.todo.ui.main

import android.Manifest
import android.animation.Animator
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private companion object {
        const val NOTIFICATION_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNotificationPermissions()
        checkExactAlarmPermission()

        binding.lottieContainer.isClickable = true
        binding.lottieContainer.isFocusable = true

        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                binding.lottieContainer.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        binding.lottieContainer.visibility = View.GONE
                    }
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

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

    private fun checkNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
                Toast.makeText(
                    this,
                    getString(R.string.msg_exact_alarm_permission_required),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}