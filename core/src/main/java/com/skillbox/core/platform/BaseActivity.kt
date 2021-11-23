package com.skillbox.core.platform

import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.skillbox.core.R
import com.skillbox.core.extensions.gone
import com.skillbox.core.extensions.show
import com.skillbox.core.utils.Event
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ToastModel

abstract class BaseActivity() : AppCompatActivity() {

    private var layout: Int? = null

    constructor(@LayoutRes layoutResId: Int) : this() {
        layout = layoutResId
    }

    open val setToolbar = false
    open var setLogout = false
    open val setDisplayHomeAsUpEnabled = true

    @StringRes
    open var resToolBarId: Int = 0

    private var _ivExit: ImageView? = null
    private val ivExit get() = checkNotNull(_ivExit) { "BaseFragment _ivExit isn`t initialized" }

    open val screenViewModel: BaseViewModel?
        get() = null

    abstract fun initInterface(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout?.let {
            setContentView(it)
        }
        initInterface(savedInstanceState)
        observeBaseLiveData()
        setupToolbar()
    }

    override fun onDestroy() {
        screenViewModel?.let { vm ->
            vm.mainState.removeObserver { }
            vm.localState.removeObserver { }
        }
        super.onDestroy()
    }

    open fun observeBaseLiveData() {
        screenViewModel?.let { vm ->
            vm.mainState.observe(this, ::handleState)
            vm.localState.observe(this, ::localData)
        }
    }

    open fun handleState(state: Event<State>) {}

    open fun localData(localToast: ToastModel) {}

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        _ivExit = findViewById(R.id.main_ivExit)
        toolbar?.let {
            toolbar.title = getString(resToolBarId)
            if (setToolbar) toolbar.show() else toolbar.gone()
        }
        _ivExit?.let {
            if (setLogout) ivExit.show() else ivExit.gone()
        }
    }
}
