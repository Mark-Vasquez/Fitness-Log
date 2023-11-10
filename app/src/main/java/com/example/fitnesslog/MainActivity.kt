package com.example.fitnesslog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
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
            name = "Push Pull Legs",
            restDurationSeconds = 90,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val testProgram2 = Program(
            name = "5x5 Stronglifts",
            restDurationSeconds = 90,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        CoroutineScope(Dispatchers.IO).launch {
            programModule.programUseCases.createProgram(testProgram)
            programModule.programUseCases.createProgram(testProgram2)

        }
    }
}