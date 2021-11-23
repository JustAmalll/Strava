package com.skillbox.core.platform

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.skillbox.core.R
import com.skillbox.core.extensions.gone
import com.skillbox.core.extensions.show
import com.skillbox.core.state.StateExitProfile
import com.skillbox.core.utils.Event
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ToastModel

abstract class BaseFragment() : Fragment() {

    open val screenViewModel: BaseViewModel? = null

    open val setToolbar = false
    open var setLogout = false
    open val setDisplayHomeAsUpEnabled = true

    @StringRes
    open var resToolbarId: Int = 0

    private var _ivExit: ImageView? = null
    private val ivExit get() = checkNotNull(_ivExit) { "BaseFragment _ivExit isn`n initialized" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenViewModel?.let { vm ->
            vm.mainState.observe(viewLifecycleOwner, ::handleState)
            vm.localState.observe(viewLifecycleOwner, ::localData)
        }

        setupToolbar(view)
    }

    open fun onBackPressed(): Boolean = false

    open fun handleState(state: Event<State>) {}

    open fun localData(localToast: ToastModel) {}

    override fun onStop() {
        super.onStop()
        hideSoftKeyboard()
    }

    override fun onDestroyView() {
        screenViewModel?.let { vm ->
            vm.mainState.removeObserver { }
            vm.localState.removeObserver { }
        }
        super.onDestroyView()
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.main_toolbar)
        _ivExit = view.findViewById(R.id.main_ivExit)
        toolbar?.let {
            toolbar.title = getString(resToolbarId)
            if (setToolbar) toolbar.show() else toolbar.gone()
        }
        _ivExit?.let {
            if (setLogout) ivExit.show() else ivExit.gone()
        }

        _ivExit?.setOnClickListener {
            StateExitProfile.changeToolbarTitle(true)
        }
    }

    fun showSoftKeyboard(field: View?) {
        field?.let {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideSoftKeyboard() {
        view?.let {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}