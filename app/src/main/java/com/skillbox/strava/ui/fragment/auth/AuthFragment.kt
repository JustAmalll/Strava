package com.skillbox.strava.ui.fragment.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.snackbar.CustomSnackBar
import com.skillbox.core.utils.Event
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ToastModel
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentAuthBinding
import com.skillbox.strava.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationService

@AndroidEntryPoint
class AuthFragment : ViewBindingFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    override val screenViewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = getColor(requireContext(), R.color.orange)
        bind()
    }

    override fun handleState(state: Event<State>) {
        if (state.peekContent() == State.Success && Pref(
                requireContext(),
                requireActivity().application
            ).accessToken.isNotEmpty()
        ) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            finishAffinity(requireActivity())
        }
    }

    override fun localData(localToast: ToastModel) {
        if (localToast.text.isBlank()) return
        if (localToast.isError) {
            CustomSnackBar.make(
                requireActivity().window.decorView.rootView as ViewGroup,
                localToast.isLocal,
                localToast.text,
                localToast.isError
            ) {
                doAuthorization()
            }.show()
        }
    }

    private fun bind() {
        binding.authButton.setOnClickListener {
            doAuthorization()
        }

        if (Pref(requireContext(), requireActivity().application).accessToken.isNotEmpty())
            screenViewModel.getIsAthlete()
    }


    @SuppressLint("LogNotTimber")
    private fun doAuthorization() {
        val authRequest = screenViewModel.authorizationRequest()

        val authService = AuthorizationService(requireContext())
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        if (authIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(authIntent)
        } else {
            Log.e(
                "AuthFragment",
                "Не могу обработать открытие activity, не найден browser for phone"
            )
        }
    }
}