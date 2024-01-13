package com.example.fitnesslog

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnesslog.databinding.ActivityMainBinding
import com.example.fitnesslog.program.ui.ProgramCreateFragment
import com.example.fitnesslog.shared.ui.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Creates ViewObject instance based on the ViewBinding xml class
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedViewModel =
            ViewModelProvider(this, SharedViewModel.Factory)[SharedViewModel::class.java]

//        sharedViewModel
        setupNavigation()


    }


    // Dismisses keyboard when user touches outside of the text field
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null && ev.action == MotionEvent.ACTION_DOWN) {
            val currentFocusView = currentFocus
            if (currentFocusView is EditText) {
                val outRect = Rect()
                currentFocusView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    currentFocusView.clearFocus()
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setupWithNavController(navController)


        /**
         * Replaces recommended addOnDestinationChangedListener from docs because the callback to hide
         * bottomNav sometimes fires, too early, before creating and switching to the destination fragment
         * view. This prevents the bottomNav flickering out before the new fragment can conceal the flicker.
         * This calls the callback only when the 2nd destination View is created and on screen
         */
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                fragment: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {

                when (fragment) {
                    is ProgramCreateFragment -> {
                        bottomNavigation.visibility = View.GONE
                    }

                    else -> {
                        bottomNavigation.visibility = View.VISIBLE
                    }
                }
            }
        }, true)
    }
}
