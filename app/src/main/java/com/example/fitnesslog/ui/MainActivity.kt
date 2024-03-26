package com.example.fitnesslog.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnesslog.R
import com.example.fitnesslog.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModels { SharedViewModel.Factory }
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Creates ViewObject instance based on the ViewBinding xml class
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        observeSharedViewModel()
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
            supportFragmentManager.findFragmentById(R.id.navHostFragmentActivityMain) as NavHostFragment
        val navController = navHostFragment.findNavController()
        bottomNavigation = binding.bottomNavActivityMain
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.programCreateFragment,
                R.id.programEditFragment,
                R.id.scheduleSelectModal,
                R.id.restTimeSelectDialog,
                R.id.workoutTemplateFragment,
                R.id.exerciseTemplatesFragment,
                R.id.exerciseTemplateEditorFragment,
                R.id.defaultExerciseInfoFragment,
                R.id.exerciseMuscleSelectDialog,
                R.id.exerciseResistanceSelectDialog,
                R.id.workoutTemplateExerciseFragment -> hideBottomNav()

                else -> showBottomNav()
            }

        }
    }

    private fun hideBottomNav() {
        // Animate the hiding of the bottom navigation
        bottomNavigation.animate()
            .translationY(bottomNavigation.height.toFloat()) // Move along y-axis down as much as height
            .alpha(0.0f) // 0f is fully transparent
            .setDuration(100) // duration in milliseconds
            .setListener(object : AnimatorListenerAdapter() {
                // Only set to visibility to gone when animation ends to avoid the large shift behavior
                override fun onAnimationEnd(animation: Animator) {
                    bottomNavigation.visibility = View.GONE
                }
            })
    }

    private fun showBottomNav() {
        // Animate the showing of the bottom navigation
        bottomNavigation.animate()
            .translationY(0f) // Move back along y-axis to original position 0f from changed position
            .alpha(1.0f) // 1.0f is fully opaque
            .setDuration(400) // duration in milliseconds
            .setListener(object : AnimatorListenerAdapter() {
                // Make it visible as soon as animation start and slowly slide back into 0f position
                override fun onAnimationStart(animation: Animator) {
                    bottomNavigation.visibility = View.VISIBLE
                }
            })
    }


    // Snackbar any errors here
    private fun observeSharedViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.stateFlow.collect { sharedState ->
                    sharedState.error?.let {
                        Snackbar.make(
                            binding.root,
                            it,
                            Snackbar.LENGTH_LONG
                        ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                sharedViewModel.onEvent(SharedEvent.ClearErrorState)
                            }
                        }).show()
                    }
                }
            }
        }
    }
}
