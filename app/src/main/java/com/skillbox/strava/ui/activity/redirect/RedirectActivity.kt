package com.skillbox.strava.ui.activity.redirect

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.skillbox.core.extensions.show
import com.skillbox.core.platform.BaseActivity
import com.skillbox.strava.R
import com.skillbox.strava.ui.activity.OnBoardingActivity
import com.skillbox.strava.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedirectActivity : BaseActivity(R.layout.activity_redirect) {

    override val screenViewModel by viewModels<RedirectViewModel>()

    private var _ivLogin: ImageView? = null
    private val ivLogin get() = checkNotNull(_ivLogin) { "ImageView ivLogin RedirectActivity isn`t initialized" }

    private var _webView: WebView? = null
    private val webView get() = checkNotNull(_webView) { "WebView webView RedirectActivity isn`t initialized" }

    private var _toolBar: Toolbar? = null
    private val toolBar get() = checkNotNull(_toolBar) { "Toolbar toolBar RedirectActivity isn`t initialized" }

    override fun initInterface(savedInstanceState: Bundle?) {
        bind()
        checkIntent(intent)
        subscribe()
    }

    private fun bind() {
        _ivLogin = findViewById(R.id.redirect_ivLogin)
        _webView = findViewById(R.id.redirect_webView)
        _toolBar = findViewById(R.id.redirect_toolbar)

        ivLogin.setOnClickListener { auth() }
    }

    private fun subscribe() {
        screenViewModel.authStateObserver.observe(this, { result ->
            result?.let {
                if (it) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else auth()
            }
        })
    }

    private fun auth() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.putExtra(AUTH, true)
        startActivity(intent)
        finishAffinity()
    }

    private fun checkIntent(intent: Intent?) {
        intent?.action?.let { action ->
            if (action == Intent.ACTION_VIEW) {
                intent.data?.getQueryParameter(CODE)?.let {
                    screenViewModel.auth(it)
                }

                intent.data?.getQueryParameter(USER_ID)?.let { userId ->
                    webView.show()
                    toolBar.show()
                    webView.loadUrl("https://www.strava.com/athletes/$userId")
                }
            }
        }
    }

    companion object {
        const val USER_ID = "userId"
        const val CODE = "code"
        const val AUTH = "auth"
    }
}