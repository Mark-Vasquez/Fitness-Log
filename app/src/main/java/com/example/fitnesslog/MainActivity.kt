package com.example.fitnesslog

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setupWithNavController(navController)

        val testProgram = Program(
            name = "Program1_Test",
            isSelected = true,
            restDurationSeconds = 90,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        CoroutineScope(Dispatchers.IO).launch {
            val result = programModule.programUseCases.createProgram(testProgram)
            when (result) {
                is Resource.Success -> {
                    Log.d("MainActivity", "Inserted ${result.data}")
                }

                is Resource.Error -> {
                    Log.e("MainActivity", "Error ${result.errorMessage}")
                }
            }
        }
    }
}