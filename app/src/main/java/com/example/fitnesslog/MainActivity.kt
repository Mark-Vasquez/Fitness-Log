package com.example.fitnesslog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setupWithNavController(navController)
//
//        val testProgram1 = Program(
//            name = "ONE",
//            restDurationSeconds = 90,
//            createdAt = System.currentTimeMillis(),
//            updatedAt = System.currentTimeMillis()
//        )
//        val testProgram2 = Program(
//            name = "TWO",
//            restDurationSeconds = 90,
//            createdAt = System.currentTimeMillis(),
//            updatedAt = System.currentTimeMillis()
//        )
//        val testProgram3 = Program(
//            name = "THREE",
//            restDurationSeconds = 90,
//            createdAt = System.currentTimeMillis(),
//            updatedAt = System.currentTimeMillis()
//        )
//        val testProgram4 = Program(
//            name = "FOUR",
//            restDurationSeconds = 90,
//            createdAt = System.currentTimeMillis(),
//            updatedAt = System.currentTimeMillis()
//        )
//
//        CoroutineScope(Dispatchers.IO).launch {
//            programModule.programUseCases.createProgram(testProgram1)
//            programModule.programUseCases.createProgram(testProgram2)
//            programModule.programUseCases.createProgram(testProgram3)
//            programModule.programUseCases.createProgram(testProgram4)
//
//        }
    }
}