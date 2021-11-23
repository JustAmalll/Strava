package com.skillbox.strava.ui.fragment.logOut

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skillbox.strava.R
import com.skillbox.strava.ui.activity.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogOutDialogFragment : BottomSheetDialogFragment() {

    private val screenViewModel by viewModels<LogOutViewMode>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(view)
        subscribe()
    }

    private fun bind(view: View) {
        view.findViewById<Button>(R.id.bottom_no).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.bottom_yes).setOnClickListener {
            screenViewModel.exit()
        }
    }

    private fun subscribe() {
        screenViewModel.reAuthStateObserver.observe(viewLifecycleOwner, { isSuccess ->
            isSuccess?.let {
                if (isSuccess) {
                    startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                    finishAffinity(requireActivity())
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
}