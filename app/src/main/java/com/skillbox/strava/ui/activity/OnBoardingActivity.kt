package com.skillbox.strava.ui.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skillbox.core.extensions.setupBottomWithNavController
import com.skillbox.core.platform.BaseActivity
import com.skillbox.core_db.pref.Pref
import com.skillbox.strava.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity(R.layout.activity_app) {

    private var currentNavController: LiveData<NavController>? = null

    private var _bottomNav: BottomNavigationView? = null
    private val bottomNav get() = checkNotNull(_bottomNav) { "BottomNavigationView bottomNav OnBoardingActivity isn`t initialized" }

    override fun initInterface(savedInstanceState: Bundle?) {
        bind()
        setupBottomNavigationBar()

        if (Pref(this, application).isBoarding)
            navigateAuth()

        intent?.data?.let { data ->
            val auth: Boolean = data.getBooleanQueryParameter("auth", false)
            if (auth) navigateAuth()
        }
    }

    private fun bind() {
        _bottomNav = findViewById(R.id.bottom_nav)
    }

    private fun navigateAuth() {
        findNavController(R.id.fragmentContainer)
            .navigate(R.id.action_boardingFragment_to_authFragment)
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.onboarding
        )

        val controller = bottomNav.setupBottomWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragmentContainer,
            intent = intent
        )
        currentNavController = controller
    }
}