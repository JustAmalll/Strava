package com.skillbox.strava.ui.activity.main

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skillbox.core.extensions.setupBottomWithNavController
import com.skillbox.core.notification.NotificationChannels
import com.skillbox.core.platform.BaseActivity
import com.skillbox.core.snackbar.CustomSnackBar
import com.skillbox.core.state.StateExitProfile
import com.skillbox.shared_model.ToastModel
import com.skillbox.strava.R
import com.skillbox.strava.ui.activity.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
@SuppressLint("LogNotTimber")
class MainActivity : BaseActivity(R.layout.activity_main) {

    override val screenViewModel by viewModels<MainViewModel>()
    private var currentNavController: LiveData<NavController>? = null

    private var _bottomNav: BottomNavigationView? = null
    private val bottomNav get() = checkNotNull(_bottomNav) { "BottomNavigationView bottomNav MainActivity isn`t initialized" }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun initInterface(savedInstanceState: Bundle?) {
        bind()
        setupBottomNavigationBar()
        subscribe()
    }

    override fun localData(localToast: ToastModel) {
        if(localToast.text.isBlank()) return
        CustomSnackBar.make(
            window.decorView.rootView as ViewGroup,
            localToast.isLocal,
            localToast.text,
            localToast.isError
        ) {
            screenViewModel.exit()
        }.show()
    }

    private fun bind() {
        _bottomNav = findViewById(R.id.bottom_nav)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun subscribe() {
        screenViewModel.reAuthStateObserver.observe(this, { isSuccessReAuth ->
            isSuccessReAuth?.let {
                if(isSuccessReAuth) {
                    exitProfile()
                }
            }
        })
        screenViewModel.showNotificationObserver.observe(this, {
            showNotification()
        })

        StateExitProfile.actionExit
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { isSuccess ->
                if(isSuccess)
                    screenViewModel.exit()
            }
            .catch {
                Log.e("MainActivity", "StateTitleToolbar.titleToolbar -> ${it.localizedMessage}")
            }
            .launchIn(lifecycleScope)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showNotification() {
        val isEnable = NotificationChannels.isNotificationsEnabled(
            context = this,
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )
        if(isEnable) {
            val notification =
                NotificationChannels.buildNotificationEvent(this, getString(R.string.title_notification), getString(R.string.description_notification), R.drawable.ic_launcher_foreground)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NotificationChannels.EVENT_ID, notification)
        } else {
            CustomSnackBar.make(
                window.decorView.rootView as ViewGroup,
                false,
                getString(R.string.main_notification_warning),
                false
            ) {
                screenViewModel.exit()
            }.show()
            Log.d("CheckRunner", "Нотификации отключены пользователем")
        }
    }

    private fun exitProfile() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finishAffinity()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.home,
            R.navigation.activities
        )

        val controller = bottomNav.setupBottomWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        currentNavController = controller
    }
}